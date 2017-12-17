<?php

 // *****************************************************************
 // ** WARNING: 'allow_url_fopen' must be set to 'On' in 'php.ini' **
 // *****************************************************************

 require_once '../config.php';

 // Parameter name for OAuth token
 $ID_TOKEN = 'id_token';
 // Endpoint to validate OAuth tokens sent by Google
 $CHECK_GOOGLE_TOKEN = 'https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=';
 // Valid Google Suite organisation
 $HD = 'xtec.cat';

 // Miscellaneous functions

 // Gets a simplified user ID from its e-mail address
 // Only plain letters, digits and dots are allowed
 function getPlainId($email, $hd) {
   $r = strtolower(trim($email));
   $p = strrpos($r, '@'.$hd);
   $r = $p === false ? str_replace('@', '.', $r) : substr($r, 0, $p + 1);

   $result = '';
   foreach(str_split($r) as $ch)
     $result = $result. ($ch < '0' || ($ch > '9' && $ch < 'a') || $ch > 'z') ? '.' : $ch ;
    
   return $result;
 }

 // Gets the value associated to $key in the $array if set, returning $default otherwise
 function getAttr($array, $key, $default) {
   return isset($array->{$key}) ? $array->{$key} : $default;
 }
 
 if(isset($_POST[$ID_TOKEN]) && $_POST[$ID_TOKEN] !== '') {
   try {
     $result = [];

     // Read settings file
     $settingsFileName = '../'.USERS_ROOT.'/'.SETTINGS_FILE;
     $settings = json_decode(file_get_contents($settingsFileName), false);
    
     // Check token validity (Warning: external call to a Google API!)
     $raw = file_get_contents($CHECK_GOOGLE_TOKEN.$_POST[$ID_TOKEN]);
     $user = json_decode($raw, false);

     if(!isset($user->{'email'}) || $user->{'email'} === '') {
       // Invalid token!
       $result['status'] = 'error';
       $result['error'] = 'Validació incorrecta';
     } else {
       
      // Check if user is valid and load email and quota
       $email = $user->{'email'};
       $quota = getAttr($settings, 'userQuota', DEFAULT_QUOTA);
       $validUser = false;
       
       $hd = getAttr($user, 'hd', null);
       if($hd === $HD)
         $validUser = true;
       
       if(isset($settings->{'users'})){
         foreach($settings->{'users'} as $validUser) {
           if($validUser->{'id'} === $email) {
             $validUser = true;
             $quota = getAttr($validUser, 'quota', $quota);
             break;
           }
         }
       }

       if($validUser !== true) {
         // Unathorized user
         $result['status'] = 'error';
         $result['error'] = 'Usuari no autoritzat';          
       } else {
         $result['email'] = $email;
         $result['quota'] = $quota;
         $result['id'] = getPlainId($email, $HD);
         $result['fullUserName'] = $user->{'name'};
         $result['avatar'] = getAttr($user, 'picture', '');
         $result['expires'] = date('M d, Y h:i:s A', $user->{'exp'} * 1000);
         $result['status'] = 'validated';
         // Todo: Check projects
         $result['projects'] = [];
         $result['currentSize'] = 0;
         // Todo: set session

       }
     }
     header('Content-Type: application/json;charset=UTF-8');
     print json_encode($result);          

   } catch (Exception $e) {
     // Internal error
     http_response_code(500);
     print 'Internal error: '.$e;
   }
 }
 else {
   // Bad request
   http_response_code(400);
 }
