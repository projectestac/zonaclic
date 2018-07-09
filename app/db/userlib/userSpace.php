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

 require_once '../config.php';
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
    public $rootDir = '/tmp/test-user';
    public $userId = 'test-user';
    public $projects = array();
    public $currentSize = 0;
    // }}}
    
    /**
     * Class constructor
     * 
     * @param string $userId    User id
     * @param string $usersRoot Root directory of user's space. Defaults to UserSpace::usersRoot()
     */
    public function __construct($userId, $usersRoot = null)
    {
        if($usersRoot === null) {
            $usersRoot = self::usersRoot();
        }
        $this->userId = $userId;
        $this->rootDir = $usersRoot.'/'.$userId;
        if (!is_dir($this->rootDir)) {
            mkdir($this->rootDir, 0777, true);
        }
    }

    /**
     * Gets an array with the data of all projects belonging to this user
     * 
     * @return array
     */
    public function getProjectsData()
    {
        $result = array();
        foreach ($this->projects as $project) {
            array_push($result, $project->prj);
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
        $this->projects = array();
        foreach (new FileSystemIterator($this->rootDir, FilesystemIterator::SKIP_DOTS) as $file) {
            if ($file->isDir() && file_exists($file->getPathname().'/project.json')) {
                $prj = new UserProject($file->getFilename(), $this->rootDir);
                $prj->readProjectData();
                $this->currentSize += $prj->totalFileSize;
                array_push($this->projects, $prj);
            }
        }
        return $this->projects;
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
        for ($i=0; $i<count($this->projects); $i++) {
            if ($this->projects[$i]->name === $name) {
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
        return $p >= 0 ? $this->projects[$p] : null;
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
            $prj = $this->projects[$p];
            $prj->clean();
            rmdir($prj->prjRoot);
            array_splice($this->projects, $p, 1);
            $result = true;
        }
        return $result;
    }

    /**
     * Creates a new UserProject and adds it to this user space
     * 
     * @param string $name The name of the project to create
     * 
     * @return UserProject The created project if successfully, null otherwise
     */
    public function createProject($name)
    {
        $result= null;
        if ($this->getProjectIndex($name)<0) {
            $result = new UserProject($name, $this->rootDir);
            array_push($this->projects, $result);
        }
        return $result;        
    }

    /**
     * Updates the file "projects.json" with data from currently registered projects
     * 
     * @return boolean
     */
    public function updateIndex()
    {
        $projectList = array();
        foreach ($this->projects as $prj) {
            $prj = $prj->prj;
            $prjReg = (object)[];
            $prjReg->path = $prj->name;
            UserSpace::copyAttr($prj, $prjReg, 'title', 'Sense nom');
            UserSpace::copyAttr($prj, $prjReg, 'author');
            UserSpace::copyAttr($prj, $prjReg, 'date');
            UserSpace::copyAttr($prj, $prjReg, 'langCodes');
            UserSpace::copyAttr($prj, $prjReg, 'mainFile');
            UserSpace::copyAttr($prj, $prjReg, 'cover');
            UserSpace::copyAttr($prj, $prjReg, 'thumbnail');
            array_push($projectList, $prjReg);
        }
        $fname = $this->rootDir.'/projects.json';
        $fp = fopen($fname, 'w');
        $result = fwrite($fp, json_encode($projectList, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE)) !== false;
        fclose($fp);
        if($result)
            UserProject::safeChmod($fname, 0666);
        return $result;
    }

    /**
     * Copy the $attr attribute of $src to $dest only if it's set or $default provided
     * 
     * @param object $src     The source object
     * @param object $dest    The destination object
     * @param string $attr    The name of the attribute to be copied
     * @param any    $default An optional default value
     * 
     * @return object The $dest object
     */
    public static function copyAttr($src, $dest, $attr, $default = null)
    {
        if (!property_exists($src, $attr)) {
            if ($default !== null) {
                $dest->$attr = $default;
            }
        } else {
            $dest->$attr = $src->$attr;
        }

        return $dest;
    }

    public static function usersRoot()
    {
        return realpath((substr(USERS_ROOT, 0, 1) === '/' ? '' : '../').USERS_ROOT);
    }

}