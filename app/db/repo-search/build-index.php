<?php

/**
 * build-index.php
 * 
 * Scans all 'project.json' files found on the repository tree,
 * storing descriptions and other relevant data on the database.
 * 
 */

 require_once('config.php');

 $base = realpath(dirname(__FILE__) . DIRECTORY_SEPARATOR . PROJECTS_PATH);
 
 // TODO: Make it recursive!
 $allFiles = scandir($base);

 echo(count($allFiles));

 foreach($allFiles as $file) {
     echo($file.'<br>');
 }

 echo('<hr>');

 $projects_string = file_get_contents($base . DIRECTORY_SEPARATOR . PROJECTS_INDEX);

 
 //var_dump(json_decode($projects_string));
 $obj = json_decode($projects_string);

 foreach($obj as $prj){
   echo($prj->{'title'} . '<br>');
 }
 
 