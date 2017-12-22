<?php
/**
 * File: userSpace.php
 * 
 * Linted with PHP_CodeSniffer (http://pear.php.net/package/PHP_CodeSniffer/)
 * against PEAR standards (https://pear.php.net/manual/en/standards.sample.php)
 * with phpcbf
 * 
 * PHP Version 7
 * 
 * @category Class
 * @package  UserSpace
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */

require_once 'userProject.php';

/**
 * Class UserSpace
 * Encapsulates information about all projects related to a single user
 * 
 * @category Class
 * @package  UserSpace
 * @author   Francesc Busquets <francesc@gmail.com>
 * @license  https://www.tldrlegal.com/l/eupl-1.1 EUPL-1.1
 * @link     https://github.com/projectestac/zonaclic
 */
class UserSpace
{
    // {{{ properties
    public $root = '/tmp';
    public $userId = 'test-user';
    public $projects = array();
    public $currentSize = 0;
    // }}}
    
    /**
     * Class constructor
     * 
     * @param string $userId User id
     * @param string $root   Root directory of user's space
     */
    public function __construct($userId, $root)
    {
        $this->$userId = $userId;
        $this->$root = $root;
        if (!is_dir($this->$root)) {
            mkdir($this->$root, 0776, true);
        }
    }

    /**
     * Gets an array with the data of all projects belonging to this user
     * 
     * @return array
     */
    public function getProjects()
    {
        $result = array();
        foreach ($this->$projects as $prj) {
            array_push($result, $prj);
        }
        return $result;
    }

    /**
     * Initializes the $projects property, loading all project data
     * 
     * @return array
     */
    public function readProjects()    
    {
        $this->$projects = array();
        foreach (new FileSystemIterator($this->$root, FilesystemIterator::SKIP_DOTS) as $file) {
            if ($file->isDir() && file_exists($file->getPath().'/project.json')) {
                $prj = new UserProject($file->getFilename(), $this);
                $prj->readProjectData();
                $this->$currentSize += $prj->$totalFileSize;
                array_push($this->$projects, $prj);
            }
        }
        return $this->$projects;
    }

    /**
     * Gets the index of $name on the list of current projects
     * 
     * @param string $name The name of the requested project
     * 
     * @return integer
     */
    public function getProjectIndex($name)    
    {
        $name = UserProject::getValidName($name);
        for ($i=0; $i<count($this->$projects); $i++) {
            if ($this->$projects[$i]->$name === $name) {
                return $i;
            }
        }
        return -1;
    }

    /**
     * Gets $name from the list of current projects
     * 
     * @param string $name The name of the requested project
     * 
     * @return UserProject
     */
    public function getProject($name)    
    {   
        $p = $this->getProjectIndex($name);
        return $p >= 0 ? $this->$projects[i] : null;
    }

    /**
     * Deletes from disk the project identified bu $name and removes all its
     * references from $projects
     * 
     * @param string $name The name of the project to be deleted
     * 
     * @return boolean - true if successfully deleted
     */
    public function removeProject($name)
    {
        $result = false;
        $p = $this->getProjectIndex($name);
        if ($p>=0) {
            $prj = $this->$projects[$i];
            $prj->clean();
            rmdir($prj->$prjRoot);
            array_splice($this->$projects, $p, 1);
            $result = true;
        }
        return $result;
    }

    /**
     * Adds a new project to this user space
     * 
     * @param UserProject $prj The project to add
     * 
     * @return boolean - true if successfully added
     */
    public function addProject($prj)
    {
        $result= false;
        if ($prj !== null && $this->getProjectIndex($prj->$name)<0) {
            array_push($this->$projects, $prj);

// Seguir aquí....


        }
        return $result;        
    }



}