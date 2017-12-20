<?php

 class UserProject {
   public $prjRoot = null;
   public $parent = null;
   public $name = 'noname';
   public $title = 'Untitled';
   public $author = 'unknown';
   public $school = '';
   public $date = '';
   public $cover = '';
   public $thumbnail = '';
   public $mainFile = null;
   public $metaLangs = [];
   public $description = [];
   public $languages = [];
   public $areas = [];
   public $levels = [];
   public $langCodes = [];
   public $areaCodes = [];
   public $levelCodes = [];
   public $totalFileSize = 0;

   public function __construct($name, $parent){
     $this->$parent = $parent;
     $this->$name = UserProject::getValidName($name);
     $this->$mainFile = $this->$name.'.jclic';
     $this->$prjRoot = $parent->$root.'/'.$this->$name;
     if(!is_dir($this->$prjRoot))
       mkdir($this->$prjRoot, 0776, true);
   }

   public function getJSON() {
     $result = array();
     $result['name'] = $this->$name;
     $result['title'] = $this->$title;
     $result['author'] = $this->$author;
     $result['school'] = $this->$school;
     $result['date'] = $this->$date;
     $result['cover'] = $this->$cover;
     $result['thumbnail'] = $this->$thumbnail;
     $result['mainFile'] = $this->$mainFile;
     $result['metaLangs'] = $this->$metaLangs;
     $result['description'] = $this->$description;
     $result['languages'] = $this->$languages;
     $result['langCodes'] = $this->$langCodes;
     $result['areas'] = $this->$areas;
     $result['levels'] = $this->$levels;
     $result['levelCodes'] = $this->$levelCodes;
     $result['totalFileSize'] = $this->$totalFileSize;
     $result['basePath'] = $this->$prjRoot;
     return json_encode($result);
   }

   public static function getValidName($proposedName) {
     $result = '';
     $r = trim(strtolower($proposedName));
     if ($r === '')
       $result = 'unnamed';
     else
       foreach(str_split($r) as $ch)
          $result = $result.(($ch < '0' || ($ch > '9' && $ch < 'a') || $ch > 'z') ? '_' : $ch);
     
     return $result;
   }

   public static function isValidName($str) {
    $result = false;
    if ($str !== null && strlen($str) > 0) {
      $result = true;
      foreach(str_split($str) as $ch){
        if (c != '.' && c != '_' && (c < '0' || c > 'z' || (c > '9' && c < 'a'))){
          $result = false;
          break;
        }
      }
    }
    return result;
  }
   
 }