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
require_once '../cors.php';
require_once 'userSpace.php';

$errCode = 500;
$errMsg = null;
$zipFile = null;

/**
 * Recursively adds the contents of a directory and its subirectories to a zip file
 * 
 * @param ZipArchive $zip     The object where to add the files
 * @param string     $baseDir The absolute path to the directory to be processed, ended with a slash
 * @param string     $dir     Used to identify subdirectories in iterations
 * 
 * @return void 
 */
function addDirToZip($zip, $baseDir, $dir='')
{
    if (is_dir($baseDir.$dir)) {
        $files = glob($baseDir.$dir.'*', GLOB_MARK); //GLOB_MARK adds a slash to directories returned
        foreach ($files as $file) {
            $rPath = substr($file, strlen($baseDir));
            if (is_file($file)) {
                $zip->addFile($file, $rPath);
            } else {
                addDirToZip($zip, $baseDir, $rPath);
            }
        }          
    } 
}

// Main proc

try {
    if (!isset($_GET['prj']) || preg_match('/^\w[\w\.-]*\/\w[\w\.-]*$/', $_GET['prj']) !== 1) {
        // Bad request
        $errCode = 400;
        throw new RuntimeException('epppp');
    }

    $prjDir = UserSpace::usersRoot().'/'.$_GET['prj'];
    if (!file_exists($prjDir) || !is_dir($prjDir) || !file_exists($prjDir.'/project.json')) {
        // Not found
        $errCode = 404;
        throw new RuntimeException();
    }

    // Name of the returned zip file:
    $zipFileName = substr($prjDir, strrpos($prjDir, '/') + 1).'.scorm.zip';
    // Temporary file used to store the zip data (to be deleted at end):
    $zipFile = tempnam(sys_get_temp_dir(), 'zip');
    // The zip object
    $zip = new ZipArchive();
    $zip->open($zipFile, ZipArchive::CREATE | ZipArchive::OVERWRITE);
    addDirToZip($zip, $prjDir.'/');
    $zip->close();

    // Send response
    allowOriginHeader();
    header('Content-Type: application/zip');
    header('Content-Disposition: attachment; filename="'.$zipFileName.'"');
    header('Content-Length: '.filesize($zipFile));
    readfile($zipFile);

    // Delete zip file
    unlink($zipFile);

} catch (RuntimeException $e) {
    if ($zipFile !== null && is_file($zipFile)) {
        unlink($zipFile);
    }
    http_response_code($errCode);
    print $e->getMessage();
}
