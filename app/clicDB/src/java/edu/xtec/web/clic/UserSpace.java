/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import java.io.File;
import java.io.FileFilter;
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
}
