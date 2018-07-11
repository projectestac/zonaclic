
<?php

$originalPath = $_SERVER['REQUEST_URI'];

// cerca expressions que comencin per '/' seguit de 'ca', 'es' o 'en',
// opcionalment seguit de qualsevol expressió que comenci per '/'
if(preg_match('/^\/(ca|es|en)(\/.*)?$/', $originalPath)) {
  // Redirecció provisional cap a '/legacy/ca/... /legacy/es/..., etc.)
  http_response_code(302);
  header('Location: /legacy'.$originalPath);    
}
else {
  var_dump($_SERVER);
  echo "<p>Redirected from: <code>".$_SERVER['REQUEST_URI']."</code></p>";
  echo "<p>Path is: <code>".$_GET['path']."</code></p>";
}
