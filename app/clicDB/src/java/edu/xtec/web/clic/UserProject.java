/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import java.io.File;
import java.io.FileInputStream;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UserProject implements java.io.Serializable {

  public File prjRoot = null;

  public String name = "noname";
  public String title = "Untitled";
  public String author = "unknown";
  public String school = "";
  public String date = "";
  public String cover = "";
  public String thumbnail = "";
  public String[] metaLangs = null;
  public String[] descriptions = null;
  public String[] languages = null;
  public String[] areas = null;
  public String[] levels = null;

  public long totalFileSize = 0;
  public int numFiles = 0;

  public UserProject(String name, UserSpace parent) throws Exception {
    this.name = UsrLibBean.getPlainId(name, "");
    prjRoot = new File(parent.root, this.name);
    prjRoot.mkdir();
  }

  public JSONObject getJSON() throws Exception {
    JSONObject obj = new JSONObject();

    obj.putOpt("name", name);
    obj.putOpt("title", title);
    obj.putOpt("author", author);
    obj.putOpt("school", school);
    obj.putOpt("date", date);
    obj.putOpt("cover", cover);
    obj.putOpt("thumbnail", thumbnail);
    obj.putOpt("meta_langs", metaLangs);
    obj.putOpt("descriptions", descriptions);
    obj.putOpt("languages", languages);
    obj.putOpt("areas", areas);
    obj.putOpt("levels", levels);

    obj.put("numFiles", numFiles);
    obj.put("totalFileSize", totalFileSize);

    return obj;
  }

  public void readProjectData() throws Exception {
    File prjJson = new File(prjRoot, "project.json");
    if (prjJson.exists()) {
      JSONObject json = UsrLibBean.readJSON(new FileInputStream(prjJson));
      title = json.optString("title", title);
      author = json.optString("author", author);
      school = json.optString("school", school);
      date = json.optString("date", date);
      cover = json.optString("cover", cover);
      thumbnail = json.optString("thumbnail", thumbnail);
      // Read meta_langs
      JSONArray ml = json.optJSONArray("meta_langs");
      if (ml != null && ml.length() > 0) {

        metaLangs = new String[ml.length()];
        for (int i = 0; i < ml.length(); i++) {
          metaLangs[i] = ml.getString(i);
        }

        // Read descriptions
        JSONObject obj = json.optJSONObject("description");
        if (obj != null && obj.length() > 0) {
          descriptions = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            descriptions[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read languages
        obj = json.optJSONObject("languages");
        if (obj != null && obj.length() > 0) {
          languages = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            languages[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read areas
        obj = json.optJSONObject("areas");
        if (obj != null && obj.length() > 0) {
          areas = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            areas[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read levels
        obj = json.optJSONObject("levels");
        if (obj != null && obj.length() > 0) {
          levels = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            levels[i] = obj.optString(metaLangs[i]);
          }
        }
      }
    }
  }

  public static final int MAX_DEPTH = 4;

  public long scanDir(File base, int depth) throws Exception {
    long size = 0;
    File[] files = base.listFiles();
    for (int i = 0; i < files.length; i++) {
      if (files[i].isDirectory()) {
        if (depth < MAX_DEPTH) {
          size += scanDir(files[i], ++depth);
        } else {
          throw new Exception("Max folder depth exceeded!");
        }
      } else if (files[i].isFile() && !files[i].isHidden()) {
        numFiles++;
        size += files[i].length();
      }
    }
    return size;
  }

  public long checkFiles() throws Exception {
    numFiles = 0;
    totalFileSize = scanDir(prjRoot, 0);
    return totalFileSize;
  }
  
  public void rmFiles() throws Exception {
    
    
    
    
  }
}
