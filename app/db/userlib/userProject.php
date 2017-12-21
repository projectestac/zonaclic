<?php
/**
 * File: userProject.php
 * 
 * Linted with PHP_CodeSniffer (http://pear.php.net/package/PHP_CodeSniffer/)
 * against PEAR standards (https://pear.php.net/manual/en/standards.sample.php)
 * with phpcbf
 * 
 * PHP Version 7
 * 
 * @category Class
 * @package  UserLib
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */


/**
 * Class UserProject
 * Encapsulates information about a specific JClic project
 * 
 * @category Class
 * @package  UserLib
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */
class UserProject
{
    // {{{ properties
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
    // }}}

    /**
     * Class constructor
     * 
     * @param string    $name   Folder name of this project
     * @param UserSpace $parent Parent user space
     */
    public function __construct($name, $parent)
    {
        $this->$parent = $parent;
        $this->$name = UserProject::getValidName($name);
        $this->$mainFile = $this->$name.'.jclic';
        $this->$prjRoot = $parent->$root.'/'.$this->$name;
        if (!is_dir($this->$prjRoot)) {
            mkdir($this->$prjRoot, 0776, true);
        }
    }

    /**
     * Gets an associative array with all relevant data
     * about this project
     * 
     * @return array
     */
    public function getData() 
    {
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
        $result['description'] = $this->toJSONObject($this->$description);
        $result['languages'] = $this->toJSONObject($this->$languages);
        $result['langCodes'] = $this->$langCodes;
        $result['areas'] = $this->toJSONObject($this->$areas);
        $result['areaCodes'] = $this->$areaCodes;
        $result['levels'] = $this->toJSONObject($this->$levels);
        $result['levelCodes'] = $this->$levelCodes;
        $result['totalFileSize'] = $this->$totalFileSize;
        $result['basePath'] = $this->$prjRoot;
        return $result;
    }

    /**
     * Converts an array of attributes into an associative array
     * linked to `metaLangs`
     * 
     * @param array $attribute An array of attributes
     * 
     * @return array
     */
    public function toJSONObject($attribute) 
    {
        if ($attribute === null || $this->$metaLangs === null || count($this.$metaLangs) !== count($attribute)) {
            return null;
        }

        $result = array();
        $n = count($this->$metaLangs);
        for ($i=0; $i<$n; $i++) {
            $result{$this->$metaLangs[i]} = $attribute[i];
        }
        return $result;
    }

    /**
     * Reads the project data from "project.json"
     * 
     * @return void
     */
    public function readProjectData()
    {
        $prjJson = $this->$prjRoot.'/project.json';
        if (file_exists($prjJson)) {
            $data = file_get_contents($prjJson);
            $prj = json_decode($data);
            $this->$title = $prj->title ?: 'No title';
            $this->$author = $prj->author ?: '';
            $this->$school = $prj->school ?: '';
            $this->$date = $prj->date ?: '';
            $this->$cover = $prj->cover ?: '';
            $this->$thumbnail = $prj->thumbnail ?: '';
            $this->$mainFile = $prj->mainFile ?: '';
            $this->$metaLangs = $prj->meta_langs ?: array();
            if (count($this->$metaLangs)>0) {
              

            }
          


        }


    }

    /**
     * Gets a valid project name, replacing special chars with '_'
     * 
     * @param string $proposedName The name to be checked
     * 
     * @return string
     */  
    public static function getValidName($proposedName) 
    {
        $result = '';
        $r = trim(strtolower($proposedName));
        if ($r === '') {
            $result = 'unnamed';
        } else {
            foreach (str_split($r) as $ch) {
                $result = $result.(($ch < '0' || ($ch > '9' && $ch < 'a') || $ch > 'z') ? '_' : $ch);
            }
        }
     
        return $result;
    }

    /**
     * Checks if the provided string is valid to be used as a project name
     * 
     * @param string $str The string to be checked
     * 
     * @return boolean
     */
    public static function isValidName($str) 
    {
        $result = false;
        if ($str !== null && strlen($str) > 0) {
            $result = true;
            foreach (str_split($str) as $ch) {
                if (c != '.' && c != '_' && (c < '0' || c > 'z' || (c > '9' && c < 'a'))) {
                    $result = false;
                    break;
                }
            }
        }
        return result;
    }
   
}