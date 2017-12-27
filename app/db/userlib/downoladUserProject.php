<?php

/**
 * File: downloadUserproject.php
 * 
 * Sends the content of a specific user project as a ".scorm.zip" file
 * 
 * PHP Version 7
 * 
 * @category Service
 * @package  UserLib
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */

// TODO: Log actions!

require_once '../config.php';
require_once 'userSpace.php';

$result = (object)['status'=>'processing'];

try {

  /*
  header('Content-Type: application/zip');
  header('Content-Disposition: attachment; filename="'.$filename.'"');
  header('Content-Length: '.filesize($filepath) );
  readfile($filepath);
  */


} catch (RuntimeException $e) {
    $result->status = 'error';
    $result->error = $e->getMessage();
}
    // Bad request
    http_response_code(400);
