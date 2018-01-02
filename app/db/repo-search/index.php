<?php
/**
 * File: index.php
 * 
 * Makes a query to the database and returns a list of the projects that match
 * the specified expression against main description, title and/or the "areas",
 * "levels", "languages" and "descriptors" fields.
 * 
 * GET or POST params:
 * - q: The query expression (required)
 * - lang: The language to search. Current valid languages are: "ca", "es" and "en" (default)
 * - method: Method to be used in full-text search. Possible values are "natural" or "boolean" (default)
 *           For more information about these methods see:
 *           https://dev.mysql.com/doc/refman/5.7/en/fulltext-search.html
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

$result = [];

// Read request params
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : null;
if ($query !== null && strlen($query > 0)) {
   
    // TODO: Log queries?

    $lang = isset($_REQUEST['lang']) ? $_REQUEST['lang'] : 'en';
    if (!in_array($lang, LANGS)) {
        $lang = 'en';
    }

    $method = isset($_REQUEST['method']) ? $_REQUEST['method'] : 'default';
    if (!in_array($method, FTS_METHODS)) {
        $method = 'natural';
    }

    $mode = $method === 'natural'
        ? ' IN NATURAL LANGUAGE MODE'
        : $method === 'boolean' ? ' IN BOOLEAN MODE' : '';

    // Set-up database connection and prepared statements:
    $dbConn = new PDO('mysql:dbname='.DB_NAME.';host='.DB_HOST.';charset=utf8', DB_USER, DB_PASSWORD); 
    
    // Build prepared statement:
    $stmtQuery = $dbConn->prepare(
        "SELECT * FROM descriptions WHERE lang='".$lang.
        "' AND MATCH(title,description,languages,areas,levels,descriptors) AGAINST (:query".$mode.")"
    );

    // Launch query
    $stmtQuery->bindParam(':query', $query);
    $stmtQuery->execute();
    
    // Collect results
    while ($row = $stmtQuery->fetch()) {    
        $result[] = $row['path'];
    }
}

// Send response
header('Content-Type: application/json;charset=UTF-8');
header('Access-Control-Allow-Origin: *');
print(json_encode($result));
