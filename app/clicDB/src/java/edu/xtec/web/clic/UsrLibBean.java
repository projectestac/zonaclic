package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author fbusquet
 */
public class UsrLibBean extends PageBean {

  public static final int DEFAULT_QUOTA = 52428800;
  /* 50 MB */
  public static final String ID_TOKEN = "id_token";
  public static final String CHECK_GOOGLE_TOKEN = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
  public boolean status = true;
  public String email;
  public String userId;
  public String fullUserName;
  public String avatar;
  public Date expires;
  public String expDate;
  public long quota = DEFAULT_QUOTA;
  public long currentSize = 0;
  public static File ROOT_BASE = null;
  public File root = null;
  public Project[] projects = new Project[0];

  public UsrLibBean() {
    super();
  }

  protected String getMainBundle() {
    return "edu.xtec.resources.messages.usrLibMessages";
  }

  //@Override
  protected void getRequestParams(HttpServletRequest request) throws Exception {
    super.getRequestParams(request);

    // Read root base location
    if (ROOT_BASE == null) {
      ROOT_BASE = new File(Context.cntx.getProperty("userLibRoot", "."));
      if (!ROOT_BASE.canWrite()) {
        throw new Exception("Invalid root base!");
      }
    }

    HttpSession session;
    String token = getParam(request, ID_TOKEN, null);
    if (token != null) {
      // Validate token
      URL verifyURL = new URL(CHECK_GOOGLE_TOKEN + token);
      JSONObject json = readJSON(verifyURL.openStream());
      // Get current session
      session = request.getSession(true);
      // Check for valid email
      email = json.getString("email");
      if (email == null) {
        throw new Exception("Invalid user");
      }
      String hd = json.optString("hd", "");

      // Read settings
      File settingsFile = new File(Context.cntx.getProperty("userLibCfg", "settings.json"));
      JSONObject settings = readJSON(new FileInputStream(settingsFile));
      quota = settings.optLong("quota", quota);
      // Find user 
      boolean validUser = "xtec.cat".equals(hd);
      JSONArray usr = settings.getJSONArray("users");
      for (int i = 0; i < usr.length(); i++) {
        JSONObject usrObj = usr.getJSONObject(i);
        String id = usrObj.optString("id", "");
        if (email.equals(id)) {
          validUser = true;
          quota = usrObj.optLong("quota", quota);
          break;
        }
      }
      if (!validUser) {
        throw new Exception("Invalid user!");
      }

      userId = getPlainId(email, "xtec.cat");
      root = new File(ROOT_BASE, userId);
      root.mkdir();
      readProjects();

      session.setAttribute("email", email);
      session.setAttribute("id", userId);
      session.setAttribute("quota", new Long(quota));
      fullUserName = json.getString("name");
      session.setAttribute("fullUserName", fullUserName);
      avatar = json.optString("picture", null);
      session.setAttribute("avatar", avatar == null ? "" : avatar);
      expires = new Date(json.getLong("exp") * 1000);
      session.setAttribute("expires", expires);
      session.setAttribute("projects", projects);
    } else {
      session = request.getSession(false);
      if (session != null) {
        email = (String) session.getAttribute("email");
        userId = (String) session.getAttribute("id");
        root = new File(ROOT_BASE, userId);
        quota = ((Long) session.getAttribute("quota")).longValue();
        fullUserName = (String) session.getAttribute("fullUserName");
        avatar = (String) session.getAttribute("avatar");
        expires = (Date) session.getAttribute("expires");
        projects = (Project[]) session.getAttribute("projects");
      }
    }
  }

  protected void process(ConnectionBean con) throws Exception {
  }

  public String getGoogleToken() {
    return Context.cntx.getProperty("googleClientId", "");
  }

  public String getJsonUserInfo() {
    String result;
    try {
      JSONObject json = new JSONObject();
      if (email == null) {
        json.put("status", "error");
        json.put("error", "invalid user");
      } else {
        json.put("email", email);
        json.put("id", userId);
        json.put("quota", quota);
        json.put("fullUserName", fullUserName);
        json.putOpt("avatar", avatar);
        json.put("expires", DateFormat.getDateTimeInstance().format(expires));
        json.put("status", "validated");
        JSONArray prj = new JSONArray();
        for (int i = 0; i < projects.length; i++) {
          prj.put(projects[i].getJSON());
        }
        json.put("projects", prj);
        json.put("currentSize", currentSize);
      }
      result = json.toString(1);
    } catch (Exception ex) {
      result = "{\"status\":\"error\",\"error\":\"invalid data\"}";
    }
    return result;
  }

  public static JSONObject readJSON(InputStream is) throws Exception {
    BufferedReader in = new BufferedReader(
            new InputStreamReader(is));
    StringBuffer sb = new StringBuffer(500);
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      sb.append(inputLine);
    }
    return new JSONObject(sb.toString());
  }

  public static String getPlainId(String email, String hd) {
    email = email.trim().toLowerCase();
    int p = email.lastIndexOf("@" + hd);
    if (p > 0) {
      email = email.substring(0, p);
    } else {
      email = email.replace('@', '.');
    }
    char[] ch = email.toCharArray();
    for (int i = 0; i < ch.length; i++) {
      char c = ch[i];
      if (c < '0' || (c > '9' && c < 'a') || c > 'z') {
        ch[i] = '.';
      }
    }
    return new String(ch);
  }

  public void readProjects() throws Exception {
    File[] prjFiles = root.listFiles(new FileFilter() {
      public boolean accept(File file) {
        return file.isDirectory();
      }
    });
    projects = new Project[prjFiles.length];
    currentSize = 0;
    for (int i = 0; i < projects.length; i++) {
      projects[i] = new Project(prjFiles[i].getName());
      currentSize += projects[i].totalFileSize;
    }
  }

  public class Project {

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

    public Project(String name) throws Exception {
      this.name = getPlainId(name, "");
      prjRoot = new File(root, this.name);
      prjRoot.mkdir();
      read();
      checkFiles();
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

    public void read() throws Exception {
      File prjJson = new File(prjRoot, "project.json");
      if (prjJson.exists()) {
        JSONObject json = readJSON(new FileInputStream(prjJson));
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

    public void checkFiles() throws Exception {
      numFiles = 0;
      totalFileSize = scanDir(this.prjRoot, 0);
    }
  }

}
