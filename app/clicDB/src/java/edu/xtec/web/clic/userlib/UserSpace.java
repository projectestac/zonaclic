/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintWriter;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UserSpace implements java.io.Serializable {

  public File root;
  public String userId;
  public UserProject[] projects = new UserProject[0];
  public long currentSize = 0;

  public UserSpace(String userId, File root) throws Exception {
    this.root = root;
    this.userId = userId;
    root.mkdir();
  }

  public UserProject[] getProjects() {
    return projects;
  }

  public JSONArray getProjectsJSON() throws Exception {
    JSONArray prj = new JSONArray();
    for (int i = 0; i < projects.length; i++) {
      prj.put(projects[i].getJSON());
    }
    return prj;
  }

  public UserProject[] readProjects() throws Exception {
    File[] prjFiles = root.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory();
      }
    });
    projects = new UserProject[prjFiles.length];
    currentSize = 0;
    for (int i = 0; i < projects.length; i++) {
      projects[i] = new UserProject(prjFiles[i].getName(), this);
      projects[i].readProjectData();
      currentSize += projects[i].checkFiles();
    }
    return getProjects();
  }

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

  public boolean removeProject(String name) throws Exception {
    boolean result = false;
    UserProject prj = getProject(name);
    if (prj != null) {
      FileUtils.deleteDirectory(prj.prjRoot);
      UserProject[] currentprojects = projects;
      projects = new UserProject[projects.length - 1];
      for (int i = 0, k = 0; i < currentprojects.length; i++) {
        if (currentprojects[i] != prj) {
          projects[k++] = currentprojects[i];
        }
      }
      currentSize -= prj.totalFileSize;
      result = true;
      saveProjectsList();
    }
    return result;
  }
  
  public void addProject(UserProject prj) throws Exception {
    if(prj!=null && getProject(prj.name)==null) {
      UserProject[] currentprojects = projects;
      projects = new UserProject[projects.length + 1];
      if(currentprojects.length > 0)
        System.arraycopy(currentprojects, 0, projects, 0, currentprojects.length);
      projects[currentprojects.length] = prj;
      prj.readProjectData();
      currentSize += prj.checkFiles();
      saveProjectsList();
    }
  }
  
  public void saveProjectsList() throws Exception {
    File projectsList = new File(root, "projects.json");
    JSONArray list = new JSONArray();
    for(int i=0; i<projects.length; i++) {
      list.put(projects[i].getInfo());
    }
    FileUtils.writeStringToFile(projectsList, list.toString(1));
  }
}
