<?php

/**
 * File: cors.php
 * 
 * Functions for dealing with HTTP response headers related to Cross Origin resource Sharing (CORS)
 * 
 * PHP Version 7
 * 
 * @category Service
 * @package  DB
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */

require_once 'config.php';

/**
 * Emits the appropiate Access-Control-Allow-Origin headers for valid origins
 *  
 * @return void
 */
function allowOriginHeader()
{
    // Get the request origin
    $httpOrigin = isset($_SERVER['HTTP_ORIGIN']) ? $_SERVER['HTTP_ORIGIN'] : null;

    // Check against valid origins and set headers (see VALID_ORIGINS in config.php)
    if (in_array($httpOrigin, VALID_ORIGINS)) {
        header('Access-Control-Allow-Credentials: true');
        header("Access-Control-Allow-Origin: ${httpOrigin}");
        header('Vary: Origin');
    }
}
