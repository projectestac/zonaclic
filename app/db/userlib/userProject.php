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
     $this->$name = UserProject::getValidName($name);
     $this->$parent = $parent;
     $this->$mainFile = $this->$name.'.jclic';
     $this->$prjRoot = $parent->$root.'/'.$this->$name;
     if(!is_dir($this->$prjRoot))
       mkdir($this->$prjRoot, 0776, true);
   }

   public function getValidName($proposedName) {
     $result = '';
     $r = trim(strtolower($proposedName));
     if ($r === '')
       $result = 'unnamed';
     else
       foreach(str_split($r) as $ch)
          $result = $result. (($ch < '0' || ($ch > '9' && $ch < 'a') || $ch > 'z') ? '_' : $ch);
     
     return $result;
   }
   
 }