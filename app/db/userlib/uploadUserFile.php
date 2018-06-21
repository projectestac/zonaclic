<?php
/**
 * File: uploadUserFile.php
 * 
 * This script gets a JClic project file of type ".scorm.zip" and
 * adds it to the user space, replacing the existing one if a project
 * with the same name was already set.
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
require_once '../log.php';
require_once 'userSpace.php';
require_once 'userProject.php';

$result = (object)['status'=>'processing'];
$userId = 'unknown';
$projectName = 'noproject';

session_start();

try {
    // Check that there is a valid user
    if (!isset($_SESSION['userId']) || !isset($_SESSION['rootDir']) || !isset($_SESSION['quota'])) {
        throw new RuntimeException('Usuari no autoritzat, o heu excedit el temps màxim de la sessió. Recarregueu la pàgina.');
    }

    // Check if there is a single file uploaded
    if (!isset($_FILES['scormFile']['error']) || is_array($_FILES['scormFile']['error'])) {
        throw new RuntimeException('Paràmetres invàlids.');
    }

    // Check also if a project name is set
    if (!isset($_REQUEST['project']) || strlen(trim($_REQUEST['project']))<1) {
        throw new RuntimeException('Paràmetres invàlids.');
    }

    // Initialize main variables
    $file = $_FILES['scormFile'];
    $projectName = UserProject::getValidName($_REQUEST['project']);
    $userId = $_SESSION['userId'];
    $quota = $_SESSION['quota'];

    // Perform additional checks about the uploaded file. Only 'UPLOAD_ERR_OK' error type is acceptable.
    switch ($file['error']) {
    case UPLOAD_ERR_OK:
        break;
    case UPLOAD_ERR_NO_FILE:
        throw new RuntimeException('No s\'ha enviat cap fitxer.');
    case UPLOAD_ERR_INI_SIZE:
    case UPLOAD_ERR_FORM_SIZE:
        throw new RuntimeException('S\'ha excedit la mida màxima del fitxer.');
    default:
        throw new RuntimeException('Error desconegut.');
    }

    // Check mime type and extension
    $tmpFilePath = $file['tmp_name'];
    $finfo = new finfo(FILEINFO_MIME_TYPE);    
    if ($finfo->file($tmpFilePath)!=='application/zip' || substr(strtolower($file['name']), -10)!=='.scorm.zip') {
        throw new RuntimeException('Format de fitxer invàlid.');
    };

    // Open the zip file
    $zip = new ZipArchive;
    if (!$zip->open($tmpFilePath)) {
        throw new RuntimeException('Format de fitxer invàlid.');
    }

    // Calculate the used space and check if there already esists a project with the same name
    $space = new UserSpace($userId);
    $space->readProjects();
    $existingPrj = $space->getProject($projectName);

    // Check if compressed file has a size that fits into user's disk quota (ignore compression gain)
    $spaceUsed = $space->currentSize - ($existingPrj !== null ? $existingPrj->totalFileSize : 0);
    if ($spaceUsed + $file['size'] > $quota) {
        throw new RuntimeException('Heu excedit l\'espai de disc disponible.');
    }

    // Rename existing project
    if ($existingPrj !== null && !$existingPrj->renameTo($projectName.'_'.time())) {
        throw new RuntimeException('No ha estat possible sobreescriure el projecte existent.');
    }

    // Create a new project and extract the zip data onto it
    $prj = $space->createProject($projectName);
    if (!$zip->extractTo($prj->prjRoot)) {
        // Something wrong happened! Remove the new project and rename back the existing one
        $space->removeProject($projectName);
        if ($existingPrj !== null) {
            $existingPrj->renameTo($projectName);
        }
        throw new RuntimeException('No s\'ha pogut extreure el contingut del fitxer.');
    }

    // Close the zip file and update global space data
    $zip->close();
    if ($existingPrj !== null) {
        $space->removeProject($existingPrj->name);
    }
    $prj->readProjectData();
    $space->updateIndex();
    $result->status = 'ok';
    $result->project = $prj->prj;
    logMsg('UPLOAD', 'user: '.$userId.' project: '.$projectName.' size: '.number_format($prj->totalFileSize/MB, 2));

} catch (RuntimeException $e) {
    $result->status = 'error';
    $result->error = $e->getMessage();
    logMsg('ERR-UPLOAD', $e->getMessage().' user: '.$userId.' project: '.$projectName);
}

// Set response header and content
header('Content-Type: application/json;charset=UTF-8');
print json_encode($result);          
