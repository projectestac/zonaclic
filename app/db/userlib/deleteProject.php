<?php

/**
 * File: deleteProject.php
 * 
 * Deletes the specified project from the user space
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

session_start();

try {
    // Check that there is a valid user
    if (!isset($_SESSION['userId']) || !isset($_SESSION['rootDir']) || !isset($_SESSION['quota'])) {
        throw new RuntimeException('Usuari no autoritzat, o heu excedit el temps màxim de la sessió. Recarregueu la pàgina.');
    }
    $userId = $_SESSION['userId'];

    // Check if a project name is set
    if (!isset($_REQUEST['project']) || strlen(trim($_REQUEST['project']))<1) {
        throw new RuntimeException('Paràmetres invàlids.');
    }
    $projectName = UserProject::getValidName($_REQUEST['project']);

    // Initialize the user space and check if the requested project exists
    $space = new UserSpace($userId, '../'.USERS_ROOT);
    $space->readProjects();
    if ($space->getProjectIndex($projectName) < 0) {
        throw new RuntimeException('Aquest projecte no existeix. Potser ja l\'heu esborrat?');
    }

    // Remove project
    if (!$space->removeProject($projectName)) {
        throw new RuntimeException('No ha estat possible esborrar el projecte.');
    }

    // Update user space index
    $space->updateIndex();
    $result->status = 'ok';

} catch (RuntimeException $e) {
    $result->status = 'error';
    $result->error = $e->getMessage();
}

// Set response header and content
header('Content-Type: application/json;charset=UTF-8');
print json_encode($result);          
