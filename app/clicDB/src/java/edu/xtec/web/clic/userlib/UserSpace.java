/**
 * UserSpace.java
 *
 * Encapsulates information about the project's library associated to a single user
 *
 */
package edu.xtec.web.clic.userlib;

import java.io.File;
import java.io.FileFilter;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UserSpace implements java.io.Serializable {

  // Root directory (on local filesystem) of this user space
  public File root;
  // Current user unique identifier
  public String userId;
  // Array of current projects published by the user
  public UserProject[] projects = new UserProject[0];
  // Current amount of bytes being used on disk
  public long currentSize = 0;

  // Creates a new UserSpace, initializing the directory if needed
  public UserSpace(String userId, File root) throws Exception {
    this.root = root;
    this.userId = userId;
    root.mkdir();
  }

  // Get all projects data as a JSON array
  public JSONArray getProjectsJSON() throws Exception {
    JSONArray prj = new JSONArray();
    for (int i = 0; i < projects.length; i++) {
      prj.put(projects[i].getJSON());
    }
    return prj;
  }

  // Looks for "project.json" files in all subdirectories
  public UserProject[] readProjects() throws Exception {
    File[] prjFiles = root.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory() && (new File(file, "project.json")).exists();
      }
    });
    // Creates an "UserProject" object for each directory and initializes it
    projects = new UserProject[prjFiles.length];
    currentSize = 0;
    for (int i = 0; i < projects.length; i++) {
      projects[i] = new UserProject(prjFiles[i].getName(), this);
      projects[i].readProjectData();
      currentSize += projects[i].checkFiles();
    }
    return projects;
  }

  // Retrieves the "UserProject" object associated to the given name from
  // the collection of current projects
  public UserProject getProject(String name) {
    UserProject result = null;
    if (name != null) {
      name = UserProject.getValidName(name);
      for (int i = 0; i < projects.length; i++) {
        if (projects[i].name.equals(name)) {
          result = projects[i];
          break;
        }
      }
    }
    return result;
  }

  // Deletes requested project from disk, removes it from
  // "projects" and updates disk quota usage
  public boolean removeProject(String name) throws Exception {
    boolean result = false;
    UserProject prj = getProject(name);
    if (prj != null) {
      // Avoid exceptions due to timeouts in NFS volumes
      // FileUtils.deleteDirectory(prj.prjRoot);
      FileUtils.deleteQuietly(prj.prjRoot);

      UserProject[] currentprojects = projects;
      projects = new UserProject[projects.length - 1];
      for (int i = 0, k = 0; i < currentprojects.length; i++) {
        if (currentprojects[i] != prj) {
          projects[k++] = currentprojects[i];
        }
      }
      currentSize -= prj.totalFileSize;
      result = true;
      // Update "projects.json"
      saveProjectsList();
    }
    return result;
  }

  // Adds a new "UserProject" object to "projects" and updates
  // disk quota usage
  public void addProject(UserProject prj) throws Exception {
    if (prj != null && getProject(prj.name) == null) {
      UserProject[] currentprojects = projects;
      projects = new UserProject[projects.length + 1];
      if (currentprojects.length > 0) {
        System.arraycopy(currentprojects, 0, projects, 0, currentprojects.length);
      }
      projects[currentprojects.length] = prj;
      prj.readProjectData();
      currentSize += prj.checkFiles();
      // Update "projects.json"
      saveProjectsList();
    }
  }

  // Saves the curent list of projects, with basic information about it
  public void saveProjectsList() throws Exception {
    File projectsList = new File(root, "projects.json");
    JSONArray list = new JSONArray();
    for (int i = 0; i < projects.length; i++) {
      list.put(projects[i].getInfo());
    }
    FileUtils.writeStringToFile(projectsList, list.toString(1));
  }
}
