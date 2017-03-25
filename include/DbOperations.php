<?php
 
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
     * Checking user login
     * @param String $email User login email id
     * @param String $password User login password
     * @return boolean/int(-1 if username not found, false if wrong password)
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
                return TRUE;
            } else {
                return FALSE;
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
     * Fetching user by email
     * @param String $email User email id
     */
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT name, email FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
 
    /* ------------- `ticket` table method ------------------ */
 
    /**
     * Creating new ticket
     * @param String $user_id user id to whom task belongs to
     * @param String $task task text
     */
    public function generateTicket($userID, $trainDesignation,$validation,$origin,$destination,$departureTime,$arrivalTime,$price) {        
        $stmt = $this->conn->prepare("INSERT INTO ticket(id,userID,trainDesignation,validation,origin,destination,departureTime,arrivalTime,price) VALUES(UUID(),?,?,?,?,?,?,?,?)");
        $stmt->bind_param("isissssd", $userID, $trainDesignation,$validation,$origin,$destination,$departureTime,$arrivalTime,$price);
        $result = $stmt->execute();
        $stmt->close();
 
        if ($result) {
            
        } else {
            // task failed to create
            return NULL;
        }
    }
 
   
 
    /**
     * Fetching all user tasks
     * @param String $user_id id of the user
     */
    public function getAllTickets($user_id) {
        $stmt = $this->conn->prepare("SELECT t.* FROM tasks t, user_tasks ut WHERE t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("i", $user_id);
        $stmt->execute();
        $tasks = $stmt->get_result();
        $stmt->close();
        return $tasks;
    }
 
    /**
     * Updating task
     * @param String $task_id id of the task
     * @param String $task task text
     * @param String $status task status
     */
    public function updateTask($user_id, $task_id, $task, $status) {
        $stmt = $this->conn->prepare("UPDATE tasks t, user_tasks ut set t.task = ?, t.status = ? WHERE t.id = ? AND t.id = ut.task_id AND ut.user_id = ?");
        $stmt->bind_param("siii", $task, $status, $task_id, $user_id);
        $stmt->execute();
        $num_affected_rows = $stmt->affected_rows;
        $stmt->close();
        return $num_affected_rows > 0;
    }
 
    
 

}
 


