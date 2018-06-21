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
require_once '../log.php';
require_once 'userSpace.php';

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
 * Gets the value associated to $key in the $array if set,
 * returning $default otherwise
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
 * Gets the content of a remote file, performing a network call
 * 
 * @param string $url The remote file to retrieve
 * 
 * @return string
 */
function getRemoteFile($url)
{
    $result = '';
    try {
        if (!USE_PROXY) {
            $result = file_get_contents($url);
        } else {
            $proxy = PROXY_HOST.':'.PROXY_PORT;
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_PROXY, $proxy);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            //curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            //curl_setopt($ch, CURLOPT_USERAGENT,'Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13');
            $result = curl_exec($ch);
            curl_close($ch);
            if ($result === '' || $result === false) {
                throw new Exception('No s\'ha pogut verificar l\'usuari. Proxy error.');
            }
        }
    } catch (Exception $e) { 
        throw $e;
    }
    return $result;
}

/**
 * Main function
 */
if (isset($_POST[ID_TOKEN]) && $_POST[ID_TOKEN] !== '') {
    $errMsg = '';
    try {
        $result = (object)['status'=>'processing'];

        // Read settings file
        $settingsFileName = UserSpace::usersRoot().'/'.SETTINGS_FILE;
        $settings = json_decode(file_get_contents($settingsFileName), false);
    
        // Check token validity (Warning: external call to a Google API!)
        $raw = getRemoteFile(CHECK_GOOGLE_TOKEN.$_POST[ID_TOKEN]);
        $user = json_decode($raw, false);

        if (!isset($user->{'email'}) || $user->{'email'} === '') {
            // Invalid token!
            $result->status = 'error';
            $result->error = 'Validació incorrecta';
            $errmsg = ' No email in user data';
        } else {       
            // Check if user is valid and load email and quota
            $email = $user->email;
            $quota = getAttr($settings, 'userQuota', DEFAULT_QUOTA);
            $validUser = false;
       
            $hd = getAttr($user, 'hd', null);
            if ($hd === HD) {
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
                $errMsg = 'User email: '.$email;
            } else if (!isset($user->exp) || $user->exp < (new DateTime())->getTimestamp()) {
                $result->status = 'error';
                $result->error = 'Credencial no vàlida';
                $errMsg = 'Expired session';
            } else {
                $result->email = $email;
                $result->quota = $quota;
                $result->id = getPlainId($email, HD);
                $result->fullUserName = $user->name;
                $result->avatar = getAttr($user, 'picture', '');
                $result->expires = date('M d, Y h:i:s A', $user->exp);
                $result->status = 'validated';

                $space = new UserSpace($result->id);
                $space->readProjects();
                $result->projects = $space->getProjectsData();
                $result->currentSize = $space->currentSize;

                // Set session data
                session_start();
                $_SESSION['userId'] = $result->id;
                $_SESSION['rootDir'] = $space->rootDir;                
                $_SESSION['quota'] = $result->quota;
                $_SESSION['currentSize'] = $result->currentSize;

                logMsg('LOGIN', 'user: '.$result->id.' ('.number_format($result->currentSize/MB, 2).'/'.number_format($result->quota/MB, 2).')');
            }
        }

        // Set response header and content
        header('Content-Type: application/json;charset=UTF-8');
        print json_encode($result);          

    } catch (Exception $e) {
        // Internal error
        http_response_code(500);
        print 'Internal error: '.$e->getMessage();
        logMsg('ERR-LOGIN', $e->getMessage().' '.$errMsg);
    }
} else {
    // Bad request
    http_response_code(400);
}
