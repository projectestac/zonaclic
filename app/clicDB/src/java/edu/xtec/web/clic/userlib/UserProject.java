/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import edu.xtec.web.clic.Utilities;
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
  public String mainFile;
  public String[] metaLangs;
  public String[] description;
  public String[] languages;
  public String[] areas;
  public String[] levels;
  public String[] langCodes;
  public String[] areaCodes;
  public String[] levelCodes;

  public long totalFileSize = 0;

  public UserProject(String name, UserSpace parent) throws Exception {
    this.parent = parent;
    this.name = UserProject.getValidName(name);
    mainFile = this.name + ".jclic";
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
    obj.putOpt("mainFile", mainFile);
    if (metaLangs != null) {
      obj.put("meta_langs", new JSONArray(metaLangs));
    }
    obj.putOpt("description", toJSONObject(description));
    obj.putOpt("languages", toJSONObject(languages));
    obj.putOpt("langCodes", langCodes);
    obj.putOpt("areas", toJSONObject(areas));
    obj.putOpt("areaCodes", areaCodes);
    obj.putOpt("levels", toJSONObject(levels));
    obj.putOpt("levelCodes", levelCodes);
    obj.put("totalFileSize", totalFileSize);
    obj.put("basePath", parent.userId + "/" + name);

    return obj;
  }

  public JSONObject toJSONObject(String[] attribute) throws Exception {
    if (attribute == null || metaLangs == null || metaLangs.length != attribute.length) {
      return null;
    }

    JSONObject result = new JSONObject();
    for (int i = 0; i < metaLangs.length; i++) {
      result.put(metaLangs[i], attribute[i]);
    }
    return result;
  }

  public void readProjectData() throws Exception {
    File prjJson = new File(prjRoot, "project.json");
    if (prjJson.exists()) {
      JSONObject json = Utilities.readJSON(new FileInputStream(prjJson));
      title = json.optString("title", title);
      author = json.optString("author", author);
      school = json.optString("school", school);
      date = json.optString("date", date);
      cover = json.optString("cover", cover);
      thumbnail = json.optString("thumbnail", thumbnail);
      mainFile = json.optString("mainFile", mainFile);
      // Read meta_langs
      JSONArray ml = json.optJSONArray("meta_langs");
      if (ml != null && ml.length() > 0) {
        metaLangs = Utilities.readJSONArray(ml);

        // Read description
        JSONObject obj = json.optJSONObject("description");
        if (obj != null && obj.length() > 0) {
          description = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            description[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read languages
        langCodes = Utilities.readJSONArray(json.optJSONArray("langCodes"));
        obj = json.optJSONObject("languages");
        if (obj != null && obj.length() > 0) {
          languages = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            languages[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read areas
        areaCodes = Utilities.readJSONArray(json.optJSONArray("areaCodes"));
        obj = json.optJSONObject("areas");
        if (obj != null && obj.length() > 0) {
          areas = new String[metaLangs.length];
          for (int i = 0; i < metaLangs.length; i++) {
            areas[i] = obj.optString(metaLangs[i]);
          }
        }

        // Read levels
        levelCodes = Utilities.readJSONArray(json.optJSONArray("levelCodes"));
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
    long fs = totalFileSize;
    FileUtils.deleteDirectory(prjRoot);
    parent.currentSize -= fs;
    prjRoot.mkdirs();
    title = "Untitled";
    author = "unknown";
    school = "";
    date = "";
    cover = "";
    thumbnail = "";
    mainFile = "";
    metaLangs = null;
    description = null;
    languages = null;
    areas = null;
    levels = null;
    langCodes = null;
    areaCodes = null;
    levelCodes = null;
    totalFileSize = 0;
  }

  public JSONObject getInfo() throws Exception {
    JSONObject result = new JSONObject();
    result.put("path", name);
    result.put("title", title);
    result.putOpt("author", author);
    result.putOpt("date", date);
    if (langCodes != null) {
      result.put("langCodes", new JSONArray(langCodes));
    }
    if (levelCodes != null) {
      result.put("levelCodes", new JSONArray(levelCodes));
    }
    if (areaCodes != null) {
      result.put("areaCodes", new JSONArray(areaCodes));
    }
    result.putOpt("mainFile", mainFile);
    result.putOpt("cover", cover);
    result.putOpt("thumbnail", thumbnail);
    return result;
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

  public static boolean isValidName(String str) {
    boolean result = false;
    if (str != null && str.length() > 0) {
      char[] ch = str.toCharArray();
      for (int i = 0; i < ch.length; i++) {
        char c = ch[i];
        if (c != '.' && c != '_' && (c < '0' || c > 'z' || (c > '9' && c < 'a'))) {
          break;
        }
      }
      result = true;
    }
    return result;
  }

}
