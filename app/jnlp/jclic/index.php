<?php
require_once 'config.php';

if(isset($_GET['path']) && preg_match('/^[\w-]*\.jnlp$/', $_GET['path']) && file_exists(JCLIC_JNLP_PATH.'/'.$_GET['path'])) {

  // Read requested file
  $jnlp = file_get_contents(JCLIC_JNLP_PATH.'/'.$_GET['path']);
  
  // Replace expressions of type "$$key" with its associated "value", based on the GET parameters passed (excluding "path")
  foreach($_GET as $key => $value){
    if($key != 'path'){
      $jnlp = str_replace('$$'.$key, $value, $jnlp);
    }
  }
  
  // Set mime-type and send the resulting file
  header('Content-type: application/x-java-jnlp-file');
  echo $jnlp;
} else {
  http_response_code(404);
}
