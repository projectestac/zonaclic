<?php

/**
 * File: build-index.php
 * 
 * Scans all 'project.json' files indicated by the repository index (projects.json)
 * storing descriptions and other relevant data on the database.
 * 
 * PHP Version 7
 * 
 * @category Service
 * @package  RepoSearch
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */

require_once '../config.php';
require_once '../log.php';

/**
 * Converts the date format used by JClic (DD/MM/YY) to YYYY/MM/DD
 * 
 * @param string $d - The date expression to be converted
 * 
 * @return string - The valid expression
 */
function adjustDate($d)
{
    $dm = explode("/", $d);
    if (count($dm)==3) {
        $d = "20$dm[2]/$dm[1]/$dm[0]";
    }
    return $d;
}

$startTime = time();

// Main process:
print("<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>Updating database</title></head><body><code><pre>\n");

// Set-up database connection:
$dbConn = new PDO(
    'mysql:dbname='.DB_NAME.
    ';host='.DB_HOST.
    ';charset=utf8',
    DB_USER, DB_PASSWORD
);

// Build prepared statements:
$stmtQuery = $dbConn->prepare('SELECT path,lastUpdated FROM projects WHERE path=:prj_path');
$stmtQuery->bindParam(':prj_path', $prj_path);
 
$stmtInsertProject = $dbConn->prepare(
    'INSERT INTO projects(path,title,date,author,school,mainFile,cover,thumbnail,zipFile,instFile,clicZoneId,orderId,files) '.
    'VALUES (:prj_path,:title,:prj_date,:author,:school,:mainFile,:cover,:thumbnail,:zipFile,:instFile,:clicZoneId,:orderId,:files)'
);
$stmtInsertProject->bindParam(':prj_path', $prj_path);
$stmtInsertProject->bindParam(':title', $title);
$stmtInsertProject->bindParam(':prj_date', $prj_date);
$stmtInsertProject->bindParam(':author', $author);
$stmtInsertProject->bindParam(':school', $school);
$stmtInsertProject->bindParam(':mainFile', $mainFile);
$stmtInsertProject->bindParam(':cover', $cover);
$stmtInsertProject->bindParam(':thumbnail', $thumbnail);
$stmtInsertProject->bindParam(':zipFile', $zipFile);
$stmtInsertProject->bindParam(':instFile', $instFile);
$stmtInsertProject->bindParam(':clicZoneId', $clicZoneId);
$stmtInsertProject->bindParam(':orderId', $orderId);
$stmtInsertProject->bindParam(':files', $files);

$stmtInsertDesc = $dbConn->prepare(
    'INSERT INTO descriptions(path,lang,title,description,languages,areas,levels,descriptors) '.
    'VALUES (:prj_path,:lang,:title,:prj_description,:languages,:areas,:levels,:descriptors)'
);
$stmtInsertDesc->bindParam(':prj_path', $prj_path);
$stmtInsertDesc->bindParam(':lang', $lang);
$stmtInsertDesc->bindParam(':title', $title);
$stmtInsertDesc->bindParam(':prj_description', $prj_description);
$stmtInsertDesc->bindParam(':languages', $languages);
$stmtInsertDesc->bindParam(':areas', $areas);
$stmtInsertDesc->bindParam(':levels', $levels);
$stmtInsertDesc->bindParam(':descriptors', $descriptors);

$stmtDeleteAllDescs = $dbConn->prepare(
    'DELETE FROM descriptions WHERE descriptions.path = :prj_path'
);
$stmtDeleteAllDescs->bindParam(':prj_path', $prj_path);

$stmtInsertCode = $dbConn->prepare(
    'INSERT INTO codes(path,type,code) '.
    'VALUES (:prj_path,:code_type,:code)'
);
$stmtInsertCode->bindParam(':prj_path', $prj_path);
$stmtInsertCode->bindParam(':code_type', $code_type);
$stmtInsertCode->bindParam(':code', $code);

$stmtDeleteAllCodes = $dbConn->prepare(
    'DELETE FROM codes WHERE codes.path = :prj_path'
);
$stmtDeleteAllCodes->bindParam(':prj_path', $prj_path);
 
// Read the index of projects included in the repository 
// Paths can be absolute (starting with '/') or relative to 'config.php', one level below this file
$basePath = realpath((substr(PROJECTS_PATH, 0, 1) === '/' ? '' : '../').PROJECTS_PATH);
$string = file_get_contents($basePath.'/'.PROJECTS_INDEX);
$projects = json_decode($string, false);
$countUpdate = 0;
$countSkip = 0;
$numProjects = count($projects);
print("Processing $numProjects projects\n------------\n");

foreach ($projects as $project) {
    $prj_path = $project->{'path'};
    $prjFileName = $basePath.'/'.$prj_path.'/project.json';
    $fileTime = filemtime($prjFileName);
    $string = file_get_contents($prjFileName);
    $projectData = json_decode($string, false);

    // Check if registry exists or is newer     
    $stmtQuery->execute();
    $row = $stmtQuery->fetch();
    $lastUpdated = $row ? strtotime($row['lastUpdated']) : 0;
    if (!$row || $fileTime > $lastUpdated) {
        print("[x] Updating project $prjFileName\n");

        // Check if file 'all-words.txt' exists
        $prjFileName = $basePath.'/'.$prj_path.'/all-words.txt';
        $allWords = file_exists($prjFileName) ? ' ' . file_get_contents($prjFileName) : '';

        // Update 'projects'
        $title = $projectData->{'title'};
        $prj_date = adjustDate($projectData->{'date'});
        $author = isset($projectData->{'author'}) ? $projectData->{'author'} : null;
        $school = isset($projectData->{'school'}) ? $projectData->{'school'} : null;
        $mainFile = $projectData->{'mainFile'};
        $cover = isset($projectData->{'cover'}) ? $projectData->{'cover'} : null;
        $thumbnail = isset($projectData->{'thumbnail'}) ? $projectData->{'thumbnail'} : null;
        $zipFile = isset($projectData->{'zipFile'}) ? $projectData->{'zipFile'} : null;
        $instFile = isset($projectData->{'instFile'}) ? $projectData->{'instFile'} : null;
        $clicZoneId = isset($projectData->{'clicZoneId'}) ? $projectData->{'clicZoneId'} : null;
        $orderId = isset($projectData->{'orderId'}) ? $projectData->{'orderId'} : null;
        $files = implode(",", $projectData->{'files'});
        $stmtInsertProject->execute();

        // Update 'descriptions'
        $stmtDeleteAllDescs->execute();         
        foreach (LANGS as $lang) {
            $prj_description = isset($projectData->{'description'}->{$lang}) ? strip_tags($projectData->{'description'}->{$lang}) : '';
            $prj_description .= $allWords;
            $languages = isset($projectData->{'languages'}->{$lang}) ? $projectData->{'languages'}->{$lang} : null;
            $areas = isset($projectData->{'areas'}->{$lang}) ? $projectData->{'areas'}->{$lang} : null;
            $levels = isset($projectData->{'levels'}->{$lang}) ? $projectData->{'levels'}->{$lang} : null;
            $descriptors = isset($projectData->{'descriptors'}->{$lang}) ? $projectData->{'descriptors'}->{$lang} : null;
            $stmtInsertDesc->execute();
        }

        // Update 'codes'
        $stmtDeleteAllCodes->execute();

        // Update language codes
        if (isset($projectData->{'langCodes'})) {
            $code_type = 'LANG';          
            foreach ($projectData->{'langCodes'} as $code) {
                $stmtInsertCode->execute();
            }
        }
        // Update area codes
        if (isset($projectData->{'areaCodes'})) {
            $code_type = 'AREA';          
            foreach ($projectData->{'areaCodes'} as $code) {
                $stmtInsertCode->execute();
            }
        }
        // Update level codes
        if (isset($projectData->{'levelCodes'})) {
            $code_type = 'LEVEL';          
            foreach ($projectData->{'levelCodes'} as $code) {
                $stmtInsertCode->execute();
            }
        }
        // Update descriptor codes
        if (isset($projectData->{'descCodes'})) {
            $code_type = 'DESC';          
            foreach ($projectData->{'descCodes'} as $code) {
                $stmtInsertCode->execute();
            }
        }
        $countUpdate++;
    } else {
        print("[ ] Project $prjFileName is up to date.\n");
        $countSkip++;
    }
    //ob_flush();
}

$timeSpent = time() - $startTime;
print("------------\n$countUpdate projects updated and $countSkip projects skipped in $timeSpent seconds.\n");
print('</pre></code></body></html>');

logMsg('INDEX', $countUpdate.' projects updated and '.$countSkip.' projects skipped in '.$timeSpent.' seconds');

 // Query model:
 // SELECT * FROM `descriptions` WHERE LANG="ca" AND MATCH(`description`,`languages`,`areas`,`levels`,`descriptors`) AGAINST ("química geologia ecosistema matemàtiques")

