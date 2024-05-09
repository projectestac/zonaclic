<?php

$originalPath = $_SERVER['REQUEST_URI'];
$path = $_GET['path'];

// cerca expressions que comencin per '/' seguit de 'ca', 'es' o 'en',
// opcionalment seguit de qualsevol expressió que comenci per '/'
if(preg_match('/^\/(ca|es|en)(\/.*)?$/', $originalPath)) {
  $lang = substr($originalPath, 1, 2);
  http_response_code(301);
  header('Location: https://projectes.xtec.cat/clic/'.$lang.'/');
}
else if(preg_match('/^\/repo/', $originalPath)) {
  // Redirigeix crides de l'antic repositori al nou

}
else {
  var_dump($_SERVER);
  echo "<p>Redirected from: <code>".$_SERVER['REQUEST_URI']."</code></p>";
  echo "<p>Path is: <code>".$_GET['path']."</code></p>";
}
