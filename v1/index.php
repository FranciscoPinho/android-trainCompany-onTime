<?php
require_once '../include/DbOperations.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';
 
\Slim\Slim::registerAutoloader();
 
$app = new \Slim\Slim();

     
// User id from db - Global Variable
$user_id = NULL;
 
/**
 * Verifying required params posted or not
 */
function verifyRequiredParams($required_fields) {
    $error = false; 
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
 
    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoRespnse(400, $response);
        $app->stop();
    }
}
 
/**
 * Validating email address
 */
function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = 'Email address is not valid';
        echoRespnse(400, $response);
        $app->stop();
    }
}
 
/**
 * Echoing json response to client
 * @param String $status_code Http response code
 * @param Int $response Json response
 */
function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);
 
    // setting response content type to json
    $app->contentType('application/json');
 
    echo json_encode($response);
}
 


/**
 * User Registration
 * url - /register
 * method - POST
 * params - name, email, password
 */
$app->post('/register', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('name', 'email', 'password','creditname','creditnumber','uuid'));
 
            $response = array();
 
            // reading post params
            $name = $app->request->post('name');
            $email = $app->request->post('email');
            $password = $app->request->post('password');
            $creditname = $app->request->post('creditname');
            $creditnumber = $app->request->post('creditnumber');
            $uuid = $app->request->post('uuid');
 
            // validating email address
            validateEmail($email);
 
            $db = new DbOperations();
            $res = $db->createUser($name, $email, $password);
 
            if ($res == 0) {
                $response["error"] = 0;
                $response["message"] = "You are successfully registered";
                $res3=$db->createUpdateSession($email, $uuid,1);
                $res2=$db->addCreditCard($creditname, $email, $creditnumber);
                $response["creditcard"]=$res2;
                $response["session"]=$res3;
                echoRespnse(201, $response);
            } else if ($res == -1) {
                $response["error"] = -1;
                $response["message"] = "Database connection failed";
                echoRespnse(200, $response);
            } else if ($res == 1) {
                $response["error"] = 1;
                $response["message"] = "Sorry, this email already existed";
                echoRespnse(200, $response);
            }
        });
    
        
/**
 * Ticket purchase/generation
 * url - /ticket
 * method - POST
 * params - userID,trainDesignation,validation,origin,destination,departuretime,arrivaltime,price
 */
$app->post('/ticket', function() use ($app) {
            // check for required params
            verifyRequiredParams(array('userID', 'trainDesignation', 'validation', 'origin', 'destination', 'departureTime', 'arrivalTime', 'price'));
 
            $response = array();
 
            // reading post params
            $userID = $app->request->post('userID');
            $trainDesignation  = $app->request->post('trainDesignation');
            $validation = $app->request->post('validation');
            $origin = $app->request->post('origin');
            $destination = $app->request->post('destination');
            $departureTime = $app->request->post('departureTime');
            $arrivalTime = $app->request->post('arrivalTime');
            $price = $app->request->post('price');
          
            $db = new DbOperations();
            echoRespnse(200, $db->generateTicket($userID, $trainDesignation, $validation, $origin, $destination, $departureTime, $arrivalTime, $price));

         
        });

 /**
 * Ticket purchase/generation
 * url - /schedules
 * method - GET
 */
$app->get('/schedules', function() use ($app) {         
            $db = new DbOperations();
            echoRespnse(200, $db->getSchedules());

        });
 /**
 * Login
 * url - /login
 * method - POST
 */
$app->post('/login', function() use ($app) {   
           verifyRequiredParams(array('email', 'password','uuid'));
            // reading post params
            $email = $app->request->post('email');
            $password  = $app->request->post('password');
            $uuid  = $app->request->post('uuid');
            $db = new DbOperations();
            $res=$db->checkLogin($email, $password);
            switch($res){
             case 0:
                $response["error"] = false;
                $response["message"] = "Login successful";
                $db->createUpdateSession($email, $uuid,1);
                echoRespnse(200, $response);
                break;
            case 1:
                $response["error"] = true;
                $response["message"] = "email/password combination not found";
                echoRespnse(201, $response);
                break;
            case -1:
                $response["error"] = true;
                $response["message"] = "User not found";
                echoRespnse(202, $response);
                break;
            }
        });
        
         /**
 * autoLogin
 * url - /autologin
 * method - POST
 */
$app->post('/autologin', function() use ($app) {   
           verifyRequiredParams(array('email', 'uuid'));
            // reading post params
            $email = $app->request->post('email');
            $uuid  = $app->request->post('uuid');
            $db = new DbOperations();
            $res=$db->autoLogin($email, $uuid);
            switch($res){
             case 0:
                $response["error"] = false;
                $response["message"] = "Login successful";
                $response["session"]=$db->createUpdateSession($email, $uuid,1);
                echoRespnse(200, $response);
                break;
            case 1:
                $response["error"] = true;
                $response["message"] = "No current session";
                echoRespnse(201, $response);
                break;
            case -1:
                $response["error"] = true;
                $response["message"] = "User not found";
                echoRespnse(202, $response);
                break;
            }
        });
        $app->run();