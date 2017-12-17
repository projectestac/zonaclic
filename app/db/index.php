<?php

/**
 * MAIN REDIRECTOR
 */

function getParam($name, $default) {
  return isset($_GET[$name]) ? $_GET[$name] : $default;
}

function buildParam($name) {
  return (isset($_GET[$name]) && $_GET[$name] !== '') ? '&'.$name.'='.urlencode($_GET[$name]) : '';
}

function buildListParam($oldId, $newId) {
  return (isset($_GET[$oldId]) && $_GET[$oldId] !== '*' && $_GET[$oldId] !== '') ? '&'.$newId .'='.urlencode($_GET[$oldId]) : '';
}

function getPathFromId($id) {
  // TODO: Build list of equivalences!
  return $id == 1000 ? 'clicinfa' : 'observa';
}

$flag = false;

if(isset($_GET['path'])) {
  switch($_GET['path']){
    case 'jclicApplet.jsp':
      $url = '/jnlp/jclic/play.jnlp?'
        .buildParam('project')
        .buildParam('title')
        .buildParam('sequence')
        .buildParam('lang')
        .buildParam('country')
        .buildParam('variant')
        .buildParam('skin')
        .buildParam('reporter')
        .buildParam('rParams')
        .buildParam('systemSounds');

      http_response_code(301);
      header('Location: '.$url);
      break;

    case 'act.jsp':
      $flag = true;
    case 'act_ca.jsp':
    case 'act_en.jsp':
    case 'act_es.jsp':
      $lang = $flag ? 'en' : substr($_GET['path'], 4, 2);
      $id = getParam('id', 1000);
      $url = '/repo/index.html?lang='.$lang.'&prj='.getPathFromId($id);

      http_response_code(301);
      header('Location: '.$url);      
      break;
    
    case 'listact.jsp':
      $flag = true;
    case 'listact_ca.jsp':
    case 'listact_en.jsp':
    case 'listact_es.jsp':
      $lang = $flag ? 'en' : substr($_GET['path'], 8, 2);
      $url = '/repo/index.html?lang='.$lang
        .buildListParam('idioma', 'language')
        .buildListParam('area', 'subject')
        .buildListParam('nivell', 'level')
        .buildListParam('text_titol', 'title')
        .buildListParam('text_aut', 'author')
        .buildListParam('text_desc', 'desc');

      http_response_code(301);
      header('Location: '.$url);
      break;

    default:
      http_response_code(404);
  }
} else {
  http_response_code(404);
}