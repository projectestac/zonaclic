/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class UserProject implements java.io.Serializable {

  public File prjRoot = null;
  public UserSpace parent;

  public String name = "noname";
  public String title = "Untitled";
  public String author = "unknown";
  public String school = "";
  public String date = "";
  public String cover = "";
  public String thumbnail = "";
  public String[] metaLangs;
  public String[] descriptions;
  public String[] languages;
  public String[] areas;
  public String[] levels;
  

  public long totalFileSize = 0;

  public UserProject(String name, UserSpace parent) throws Exception {
    this.parent = parent;
    this.name = UserProject.getValidName(name);    
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
    if(metaLangs!=null)
      obj.put("meta_langs", new JSONArray(metaLangs));
    obj.putOpt("descriptions", toJSONObject(descriptions));
    obj.putOpt("languages", toJSONObject(languages));
    obj.putOpt("areas", toJSONObject(areas));
    obj.putOpt("levels", toJSONObject(levels));
    obj.put("totalFileSize", totalFileSize);
    obj.put("basePath", parent.userId + "/" + name);

    return obj;
  }
  
  public JSONObject toJSONObject(String[] attribute) throws Exception{
    
    if(attribute==null || metaLangs==null || metaLangs.length!=attribute.length)
      return null;
    
    JSONObject result = new JSONObject();    
    for(int i=0; i<metaLangs.length; i++){
      result.put(metaLangs[i], attribute[i]);
    }
    return result;
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

  public long checkFiles() throws Exception {
    totalFileSize = FileUtils.sizeOfDirectory(prjRoot);
    return totalFileSize;
  }

  public void clean() throws Exception {
    FileUtils.deleteDirectory(prjRoot);
    prjRoot.mkdirs();
    title = "Untitled";
    author = "unknown";
    school = "";
    date = "";
    cover = "";
    thumbnail = "";
    metaLangs = null;
    descriptions = null;
    languages = null;
    areas = null;
    levels = null;
    totalFileSize = 0;
  }

  public static String getValidName(String proposedName) {
    String result = proposedName.trim().toLowerCase();
    if (result.equals("")) {
      result = "unnamed";
    }
    char[] ch = result.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (c < '0' || (c > '9' && c < 'a') || c > 'z') {
        ch[i] = '_';
      }
    }
    return new String(ch);
  }

}
