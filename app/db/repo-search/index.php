<?php

/**
 * File "index.php"
 * 
 * Makes a query to the database and returns a list of the projects that have
 * the specified words on its main description, title and/or the "areas",
 * "levels", "languages" or "descriptors" fields.
 * 
 * Params to be passed by GET:
 * - q: The query expression. Required.
 * - lang: The language to search. Current possible values are: "ca", "es" and "en" (default)
 * - method: Method to be used in full-text search. Possible values are "natural" or "boolean" (default)
 *           For more information about the differences between these methods see:
 *           https://dev.mysql.com/doc/refman/5.7/en/fulltext-search.html
 * 
 */

 require_once 'config.php';

 header('Content-Type: application/json');
 header('Access-Control-Allow-Origin: *');
 
 $result = [];

 $query = isset($_REQUEST['q']) ? $_REQUEST['q'] : null;
 if($query === null){
   print json_encode($result);
   return;
 }

 $lang = isset($_REQUEST['lang']) ? $_REQUEST['lang'] : 'en';
 if(!in_array($lang, LANGS))
   $lang = 'en';

 $method = isset($_REQUEST['method']) ? $_REQUEST['method'] : 'default';
 if(!in_array($method, FTS_METHODS))
   $method = 'natural';

 $mode = $method === 'natural' ? ' IN NATURAL LANGUAGE MODE' : $method === 'boolean' ? ' IN BOOLEAN MODE' : '';
   
 // Set-up database connection and prepared statements:
 $dbConn = new PDO('mysql:dbname='.DB_NAME.';host='.DB_HOST.';charset=utf8', DB_USER, DB_PASSWORD); 
 $stmtQuery = $dbConn->prepare("SELECT * FROM descriptions WHERE lang='". $lang ."' AND MATCH(title,description,languages,areas,levels,descriptors) AGAINST (:query" . $mode . ")");
 $stmtQuery->bindParam(':query', $query);
 
 $stmtQuery->execute();
 while ($row = $stmtQuery->fetch()){
   $result[] = $row['path'];
 }

 // TODO: Log queries?

 print(json_encode($result));
