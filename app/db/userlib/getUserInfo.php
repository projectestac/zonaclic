<?php
/**
 * File: getUserInfo.php
 * 
 * Linted with PHP_CodeSniffer (http://pear.php.net/package/PHP_CodeSniffer/)
 * against PEAR standards (https://pear.php.net/manual/en/standards.sample.php)
 * with phpcbf
 * 
 * PHP Version 7
 * 
 * @category Service
 * @package  UserLib
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */


// *****************************************************************
// ** WARNING: 'allow_url_fopen' must be set to 'On' in 'php.ini' **
// *****************************************************************

require_once '../config.php';
require_once 'userSpace.php';

// Parameter name for OAuth token
$ID_TOKEN = 'id_token';
// Endpoint to validate OAuth tokens sent by Google
$CHECK_GOOGLE_TOKEN = 'https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=';
// Valid Google Suite organisation
$HD = 'xtec.cat';

 // Miscellaneous functions

 // Gets a simplified user ID from its e-mail address
 // Only plain letters, digits and dots are allowed

/**
 * Gets a simplified user ID from its e-mail address
 * Only plain letters, digits and dots are allowed
 * 
 * @param string $email Full user's e-mail
 * @param string $hd    The G Suite organization where the user is enrolled, if any
 * 
 * @return string
 */
function getPlainId($email, $hd)
{
    $r = strtolower(trim($email));
    $p = strrpos($r, '@'.$hd);
    $r = $p === false ? str_replace('@', '.', $r) : substr($r, 0, $p);

    $result = '';
    foreach (str_split($r) as $ch) {
        $result .= (($ch < '0' || ($ch > '9' && $ch < 'a') || $ch > 'z') ? '.' : $ch);
    }
    
    return $result;
}

/**
 * Gets the value associated to $key in the $array if set, returning $default otherwise
 * 
 * @param array  $array   The array where to search for
 * @param string $key     Key to be searched
 * @param any    $default Default value to return if key not found
 * 
 * @return any
 */
function getAttr($array, $key, $default)
{
    return isset($array->{$key}) ? $array->{$key} : $default;
}

/**
 * Main function
 */
if (isset($_POST[$ID_TOKEN]) && $_POST[$ID_TOKEN] !== '') {
    try {
        $result = (object)[];

        // Read settings file
        $settingsFileName = '../'.USERS_ROOT.'/'.SETTINGS_FILE;
        $settings = json_decode(file_get_contents($settingsFileName), false);
    
        // Check token validity (Warning: external call to a Google API!)
        $raw = file_get_contents($CHECK_GOOGLE_TOKEN.$_POST[$ID_TOKEN]);
        $user = json_decode($raw, false);

        if (!isset($user->{'email'}) || $user->{'email'} === '') {
            // Invalid token!
            $result->status = 'error';
            $result->error = 'Validació incorrecta';
        } else {       
            // Check if user is valid and load email and quota
            $email = $user->email;
            $quota = getAttr($settings, 'userQuota', DEFAULT_QUOTA);
            $validUser = false;
       
            $hd = getAttr($user, 'hd', null);
            if ($hd === $HD) {
                $validUser = true;
            }
       
            if (isset($settings->users)) {
                foreach ($settings->users as $usr) {
                    if ($usr->id === $email) {
                        $validUser = true;
                        $quota = getAttr($usr, 'quota', $quota);
                        break;
                    }
                }
            }

            if ($validUser !== true) {
                // Unathorized
                $result->status = 'error';
                $result->error = 'Usuari no autoritzat';          
            } else if (!isset($user->exp) || $user->exp < (new DateTime())->getTimestamp()) {
                $result->status = 'error';
                $result->error = 'Credencial no vàlida';
            } else {
                $result->email = $email;
                $result->quota = $quota;
                $result->id = getPlainId($email, $HD);
                $result->fullUserName = $user->name;
                $result->avatar = getAttr($user, 'picture', '');
                $result->expires = date('M d, Y h:i:s A', $user->exp);
                $result->status = 'validated';

                $space = new UserSpace($result->id, '../'.USERS_ROOT);
                $space->readProjects();
                $result->projects = $space->getProjectsData();
                $result->currentSize = $space->currentSize;

                // Todo: set session

                // DEBUG
                // $result['response'] = $user;
            }
        }

        // Set response
        header('Content-Type: application/json;charset=UTF-8');
        print json_encode($result);          

    } catch (Exception $e) {
        // Internal error
        http_response_code(500);
        print 'Internal error: '.$e;
    }
} else {
    // Bad request
    http_response_code(400);
}
