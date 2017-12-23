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
    public $name = 'noname';
    public $prj = null;
    public $totalFileSize = 0;
    // }}}

    /**
     * Class constructor
     * 
     * @param string $name      Folder name of this project
     * @param string $parentDir Root folder for all user spaces
     */
    public function __construct($name, $parentDir)
    {
        $this->name = UserProject::getValidName($name);
        $this->prjRoot = $parentDir.'/'.$this->name;
        if (!is_dir($this->prjRoot)) {
            mkdir($this->prjRoot, 0776, true);
        }
        $this->prj = (object)[];
    }

    /**
     * Reads the project data from "project.json"
     * 
     * @return void
     */
    public function readProjectData()
    {
        $prjJson = $this->prjRoot.'/project.json';
        if (file_exists($prjJson)) {
            $data = file_get_contents($prjJson);
            $this->setProjectData(json_decode($data));
        }
    }

    /**
     * Set the project data from a $prj object
     * 
     * @param object $prj The project data
     * 
     * @return void
     */
    public function setProjectData($prj)
    {
        $this->prj = $prj;
        $this->totalFileSize = UserProject::getDirectorySize($this->prjRoot);
        // Copy data to $prj
        $this->prj->name = $this->name;
        $this->prj->totalFileSize = $this->totalFileSize;
        $this->prj->basePath = $this->name;
    }

    /**
     * Get the total file size of the files existing in a specific directory and its subdirectories
     * From https://stackoverflow.com/a/21409562/3588740
     * 
     * @param string $path The path to be recursivelly scanned
     * 
     * @return integer
     */
    public static function getDirectorySize($path)
    {
        $bytestotal = 0;
        $path = realpath($path);
        if ($path!==false && $path!='' && file_exists($path)) {
            foreach (new RecursiveIteratorIterator(new RecursiveDirectoryIterator($path, FilesystemIterator::SKIP_DOTS)) as $object) {
                $bytestotal += $object->getSize();
            }
        }
        return $bytestotal;
    }

    /**
     * Deletes all content of root project's directory and its subdirectories
     * 
     * @return void
     */
    public function clean()
    {
        // Delete all files and subsirectories
        foreach (new RecursiveIteratorIterator(new RecursiveDirectoryIterator($this->prjRoot, FilesystemIterator::SKIP_DOTS), RecursiveIteratorIterator::CHILD_FIRST) as $path) {
            $path->isDir() && !$path->isLink() ? rmdir($path->getPathname()) : unlink($path->getPathname());
        }
        // Don't delete root dir!
        // rmdir($dirPath);
        $this->totalFileSize = 0;
        $this->prj = (object)[];
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