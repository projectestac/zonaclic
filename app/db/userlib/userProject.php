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
     * Renames the project (and the directory containing it) to a new name
     * 
     * @param string $newName The new name assigned to this project
     * 
     * @return boolean true if successfull
     */
    public function renameTo($newName)
    {
        $newName = UserProject::getValidName($newName);
        $newRoot = dirname($this->prjRoot).'/'.$newName;
        $result = rename($this->prjRoot, $newRoot);
        if ($result) {
            $this->name = $newName;
            $this->prjRoot = $newRoot;
        }
        return $result;
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
     * $prjRoot itself is not deleted
     * 
     * @return void
     */
    public function clean()
    {
        UserProject::_removeDir($this->prjRoot.'/', false);
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

    /**
     * Recursively deletes the content of a directory and its subdirectories
     * Thanks to Lewis Cowles and Paulund:
     * https://paulund.co.uk/php-delete-directory-and-files-in-directory
     * 
     * @param string  $target     The directory to remove. IMPORTANT: Must be ended with '/'
     * @param boolean $deleteRoot When `true`, the provided directory is also removed
     * 
     * @return void
     */
    private static function _removeDir($target, $deleteRoot=false)
    {
        if (is_dir($target)) {
            $files = glob($target.'*', GLOB_MARK); //GLOB_MARK adds a slash to directories returned
            foreach ($files as $file) {
                UserProject::_removeDir($file, true);
            }          
            if ($deleteRoot) {
                rmdir($target);
            }
        } elseif (is_file($target)) {
            unlink($target);
        }
    }
}