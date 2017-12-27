<?php

/**
 * File: index.php
 * 
 * Redirector of old calls to /db/xxx to the new JClic repostory
 * 
 * PHP Version 7
 * 
 * @category Service
 * @package  DB
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */

require_once 'projects-id.php';

// Utility functions

/**
 * Gets the value of a GET parameter, returning a default value when not found
 * 
 * @param string $name    The parameter name
 * @param string $default The default value
 * 
 * @return string
 */
function getParam($name, $default)
{
    return isset($_GET[$name]) ? $_GET[$name] : $default;
}


/**
 * Builds an url param from a GET param
 * 
 * @param string $name The GET parameter name
 * 
 * @return string
 */
function buildParam($name)
{
    return (isset($_GET[$name]) && $_GET[$name] !== '') ? '&'.$name.'='.urlencode($_GET[$name]) : '';
}

/**
 * Builds an url param from a GET param value, translating the param name to a new value
 * 
 * @param string $oldId The GET parameter name
 * @param string $newId The parameter name to be used in the URL
 * 
 * @return string
 */
function buildListParam($oldId, $newId)
{
    return (isset($_GET[$oldId]) && $_GET[$oldId] !== '*' && $_GET[$oldId] !== '') ? '&'.$newId .'='.urlencode($_GET[$oldId]) : '';
}

/**
 * Gets the path of the main project of an old JClic database record
 * See projects-id.php for the full equivalence table
 * 
 * @param integer $id The originai identifier of the JClic DB record
 * 
 * @return integer
 */
function getPathFromId($id)
{
    return isset(PROJECTS[$id]) ? PROJECTS[$id] : '';
}


// Main process >>>

$flag = false;

if (isset($_GET['path'])) {
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
