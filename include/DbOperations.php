<?php

require '../vendor/autoload.php';

use Ramsey\Uuid\Uuid;
use Ramsey\Uuid\Exception\UnsatisfiedDependencyException;

/**
 * Class to handle all db operations
 * This class will have CRUD methods for database tables
 */
class DbOperations {

    private $conn;

    function __construct() {
        require_once dirname(__FILE__) . './DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }

    /* ------------- `users` table method ------------------ */

    /**
     * Creating new user
     * @param String $name User full name
     * @param String $email User login email id
     * @param String $password User login password
     */
    public function createUser($name, $email, $password) {
        require_once 'PassHash.php';

        // First check if user already existed in db
        if (!$this->isUserExists($email)) {

            // Generating password hash
            $password_hash = PassHash::hash($password);
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO users(name, email, password_hash) values(?, ?, ?)");
            $stmt->bind_param("sss", $name, $email, $password_hash);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return 0;
            } else {
                // Failed to create user
                return -1;
            }
        } else {
            // User with same email already existed in the db
            return 1;
        }
    }
    
     /**
     * Updates or creates session data
     * @param String $email User login email id
     * @param String $uuid User login password
     * @param Boolean $state 1 means logged in, 0 logged out
     */
    public function createUpdateSession($email, $uuid,$state) {
         $user_id = $this->getUserByEmail($email);
         if($user_id==NULL){
             return -1;
         }
        if (!$this->sessionExists($email,$uuid)) {
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO user_sessions(user_id,device_uuid,is_logged_in) values(?, ?, ?)");
            $stmt->bind_param("ssi", $user_id, $uuid, $state);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) { 
                return 0;  // Session successfully inserted
            } else {
                return -1;   // Failed to create session
            }
        } else {
            $stmt = $this->conn->prepare("UPDATE user_sessions
                                          SET is_logged_in=?,last_updated=CURRENT_TIMESTAMP
                                          WHERE user_id=? and device_uuid=?");
            $stmt->bind_param("iss",$state, $user_id, $uuid);
            $result = $stmt->execute();
            $stmt->close();
            if ($result) {
                return 1;    // session updated
            } else {
                return -2; //update session failed
            }
        }
    }

    /**
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return int(-1 if username not found, 1 if wrong password)
     */
    public function checkLogin($email, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT password_hash FROM users WHERE email = ?");

        $stmt->bind_param("s", $email);

        $stmt->execute();

        $stmt->bind_result($password_hash);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {

            $stmt->fetch();
            $stmt->close();

            if (PassHash::check_password($password_hash, $password)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            $stmt->close();
            return -1;
        }
    }
    
     /**
     * Checks if user has an active session
     * @param String $email User login email id
     * @param String $uuid uuid id associated with user's app installation
     * @return int(-1 if user not found, 1 if the user has no current login session
     */
    public function autoLogin($email, $uuid) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT is_logged_in,email FROM user_sessions
                                      LEFT JOIN users ON user_sessions.user_id=users.id 
                                      WHERE user_sessions.device_uuid=? AND email=?");

        $stmt->bind_param("ss", $uuid,$email);

        $stmt->execute();

        $stmt->bind_result($is_logged_in,$email);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {

            $stmt->fetch();
            $stmt->close();

            if ($is_logged_in==1) {
                return 0;
            } else {
                return 1;
            }
        } else {
            $stmt->close();
            return -1;
        }
    }
    
    /**
     * Checking for duplicate user by email address
     * @param String $email email to check in db
     * @return boolean
     */
    private function isUserExists($email) {
        $stmt = $this->conn->prepare("SELECT id from users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
     /**
     * Checking if a user session with this uuid and email exist
     * @return boolean
     */
    private function sessionExists($email,$uuid) {
        $stmt = $this->conn->prepare("SELECT user_sessions.id,email FROM user_sessions
                                      LEFT JOIN users ON user_sessions.user_id=users.id 
                                      WHERE user_sessions.device_uuid=? AND email=?");
        $stmt->bind_param("ss", $uuid,$email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }


    /**
     * Fetching user by email
     * @param String $email User email 
     */
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT id FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        
        $stmt->execute();

        $stmt->bind_result($user);

        $stmt->store_result();

        if ($stmt->num_rows > 0) {

            $stmt->fetch();
            $stmt->close();

            return $user;
        } else {
            $stmt->close();
            return -1;
        }
    }

    /* ------------- `ticket` table method ------------------ */

    /**
     * Creating new ticket and generates RSA signature of hashed ticket data
     * @param Int $user_id ticket buyer
     * @param String $trainDesignation train identifier
     * @param boolean $validated generated tickets start with false
     * @param String $origin beginning station
     * @param String $destination end station
     * @param String $departureTime departure from origin station at this time
     * @param String $arrivalTime arrival to destination at this time
     * @param Double $price price paid for the ticket
     */
    public function generateTicket($email, $trainDesignation, $validation, $origin, $destination, $departureTime, $arrivalTime, $price) {
        $uuid4 = Uuid::uuid4();
        $userID = $this->getUserByEmail($email);
         if($userID==NULL){
             return -1;
         }
        $stmt = $this->conn->prepare("INSERT INTO ticket(id,userID,trainDesignation,validation,origin,destination,departureTime,arrivalTime,price) VALUES(?,?,?,?,?,?,?,?,?)");
        $stmt->bind_param("sisissssd", $uuid4, $userID, $trainDesignation, $validation, $origin, $destination, $departureTime, $arrivalTime, $price);
        $result = $stmt->execute();

        $stmt->close();
        $hash = sha1($uuid4);
        $encrypted_signature = null;
        if ($result) {
            $sql = "SELECT private FROM `encryption_keys`;";
            $result = $this->conn->query($sql);
            if ($result->num_rows > 0) {
                $row = $result->fetch_assoc();
                $key=$row['private'];
                $key = wordwrap($key, 65, "\n", true);
                $key = <<<EOF
-----BEGIN RSA PRIVATE KEY-----
$key
-----END RSA PRIVATE KEY-----
EOF;
                
                openssl_private_encrypt($hash, $encrypted_signature, openssl_pkey_get_private($key));                          
            } else {
                $response["error"] = true;
                $response["message"] = "Oops! An error occurred while purchasing ticket! Database may be down";
                return $response;
            }
            $signature=bin2hex($encrypted_signature);
            $response = array(
                'id' => $uuid4,
                'userID' => $userID,
                'trainDesignation' => $trainDesignation,
                'validation' => $validation,
                'origin' => $origin,
                'destination' => $destination,
                'departureTime' => $departureTime,
                'arrivalTime' => $arrivalTime,
                'price' => $price,
                'signature' => $hash,
                
            );
            $response["error"] = 0;
            $response["hash"]= $hash;
            return $response;
        } else {
            $response["error"] = -1;
            $response["message"] = "Oops! An error occurred while purchasing ticket";
            return $response;
        }
    }

    /**
     * Fetching the schedules of the service
     * @param String $user_id id of the user
     */
    public function getSchedules() {
        $sql="SELECT stationschedule.id ,train.designation AS train,
             (SELECT name FROM station WHERE stationschedule.station=station.id) AS origin,
             (SELECT name FROM station WHERE stationschedule.nextstation=station.id) AS destination,
              distance,departureTime,arrivalTime 
             FROM stationschedule 
             INNER JOIN station on stationschedule.station=station.id 
             INNER JOIN train on train.scheduleID=stationschedule.scheduleID 
             GROUP BY stationschedule.id,train.designation;";
        $result = $this->conn->query($sql);
        $schedules=array();
            if ($result->num_rows > 0) {
                 while($row = $result->fetch_assoc()) {
                        $schedules[]=$row;     
                    }
                    $schedules["error"] = 0;
                    $schedules["message"] = "success";
                    return $schedules;
            }
            else {
                $response["error"] = -1;
                $response["message"] = "Couldn't retrieve schedules! Database may be down";
                return $response;
            }
      
    }
    
     /**
     * Fetching the schedules of the service
     * @param String $train train designation
     */
    public function getTicketsTrain($train) {
     
            $sql2="SELECT ticket.id,ticket.userID,ticket.trainDesignation,ticket.validation,ticket.origin,ticket.destination,ticket.departureTime,ticket.arrivalTime,ticket.price,train.capacity
             FROM ticket
             INNER JOIN train on train.designation=ticket.trainDesignation 
             WHERE ticket.trainDesignation='".$train."' GROUP BY train.designation,ticket.id,train.capacity;
             ";
        $result2 = $this->conn->query($sql2);
        $tickets=array();
        
             if ($result2->num_rows > 0) {
                while($row2 = $result2->fetch_assoc()) {
                    $tickets[]=$row2;
                   /*     $tickets['id']=$row2['id'];
                        $tickets['userID']=$row2['userID'];
                        $tickets['trainDesignation']=$row2['trainDesignation'];
                        $tickets['origin']=$row2['origin'];
                        $tickets['destination']=$row2['destination'];
                        $tickets['departureTime']=$row2['departureTime'];
                        $tickets['arrivalTime']=$row2['arrivalTime'];
                        $tickets['price']=$row2['price'];
                        $tickets['capacity']=$row2['capacity'];*/
                }
                   $tickets["error"] = 0;
                   $tickets["message"] = "success";
                   return $tickets;
               }
             else {
                 $tickets["error"] = -1;
                 $tickets["message"] = "Couldn't retrieve schedules! Database may be down";
                 return $tickets;
            }
      
    }


      /**
     * Creating new credit card
     * @param String $name credit card owner name
     * @param String $userID user id in database associated with this card
     * @param String $number credit card number
     */
    public function addCreditCard($name, $email, $number) {
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO creditcard(name, user_email, number) values(?, ?, ?)");
            $stmt->bind_param("sss", $name, $email, $number);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // Credit card successfully created
                return 0;
            } else {
                // Failed to add credit card
                return -1;
            }
   
    }


}
