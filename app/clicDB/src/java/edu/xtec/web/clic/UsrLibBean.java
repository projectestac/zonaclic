package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.io.BufferedReader;
import java.io.File;
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
  public static File ROOT_BASE = null;

  public String email;
  public String userId;
  public String fullUserName;
  public String avatar;
  public Date expires;
  public String expDate;
  public long quota = DEFAULT_QUOTA;

  public UserSpace userSpace;

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

    String token = getParam(request, ID_TOKEN, null);
    if (token != null) {
      // Validate token
      URL verifyURL = new URL(CHECK_GOOGLE_TOKEN + token);
      JSONObject json = readJSON(verifyURL.openStream());
      
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
      userSpace = new UserSpace(userId, new File(ROOT_BASE, userId));
      userSpace.readProjects();

      fullUserName = json.getString("name");
      avatar = json.optString("picture", null);
      expires = new Date(json.getLong("exp") * 1000);

      setSessionAttributes(request.getSession(true));

    } else {
      getSessionAttributes(request.getSession(false));
    }
  }

  protected void setSessionAttributes(HttpSession session) throws Exception {
    if (session != null) {
      session.setAttribute("email", email);
      session.setAttribute("id", userId);
      session.setAttribute("quota", new Long(quota));
      session.setAttribute("fullUserName", fullUserName);
      session.setAttribute("avatar", avatar == null ? "" : avatar);
      session.setAttribute("expires", expires);
      session.setAttribute("userSpace", userSpace);
    }
  }

  protected void getSessionAttributes(HttpSession session) throws Exception {
    if (session != null) {
      email = (String) session.getAttribute("email");
      userId = (String) session.getAttribute("id");
      quota = ((Long) session.getAttribute("quota")).longValue();
      fullUserName = (String) session.getAttribute("fullUserName");
      avatar = (String) session.getAttribute("avatar");
      expires = (Date) session.getAttribute("expires");
      userSpace = (UserSpace) session.getAttribute("userSpace");
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
        json.put("projects", userSpace.getProjectsJSON());
        json.put("currentSize", userSpace.currentSize);
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

}
