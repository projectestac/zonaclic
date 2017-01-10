/**
 * GetUserInfo.java
 * 
 * Checks if the token provided by the user is valid and, when true, returns
 * information about the user's zone status and projects currently published on
 * it.
 *
 **/
package edu.xtec.web.clic.userlib;

import edu.xtec.util.db.ConnectionBean;
import edu.xtec.web.clic.Context;
import edu.xtec.web.clic.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author fbusquet
 */
public class GetUserInfo extends HttpServlet {

  // Default disk quota is 50 MB
  public static final int DEFAULT_QUOTA = 52428800;

  // Parameter name for OAuth token
  public static final String ID_TOKEN = "id_token";
  // Endpoint to validate OAuth tokens sent by Google
  public static final String CHECK_GOOGLE_TOKEN = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
  // Root directory of user's zone in the local filesystem
  private static File ROOT_BASE = null;

  // Information fields about the current user
  public String email;
  public String userId;
  public String fullUserName;
  public String avatar;
  public Date expires;
  public String expDate;
  public long quota = DEFAULT_QUOTA;

  // UserSpace object assignated to the current user
  public UserSpace userSpace;

  // Main servlet method. Reacts only to "POST" queries.
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Response will be always a JSON
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();
    try {
      loadUserData(request);
      writer.write(getJsonUserInfo().toString());
      logMsg("INFO", "User logged in");
    } catch (Exception ex) {
      // Hand-made JSON, used to return the error message
      writer.print("{\"status\":\"error\",\"error\":\"" + JSONStringer.getString(ex.getMessage()) + "\"}");
      logMsg("ERROR", "Login incorrect: " + ex.getMessage());
    } finally {
      writer.flush();
    }
  }

  // Loads the data associated with the current user
  protected void loadUserData(HttpServletRequest request) throws Exception {
    String token = Utilities.getParam(request, ID_TOKEN, null);
    if (token != null) {
      // Validate token
      URL verifyURL = new URL(CHECK_GOOGLE_TOKEN + token);
      JSONObject json = Utilities.readJSON(verifyURL.openStream());

      // Check for valid email
      email = json.getString("email");
      if (email == null) {
        throw new Exception("Usuari desconegut");
      }
      // "hd" field is used only with GSuite users. Contains the domain name.
      String hd = json.optString("hd", "");

      // Read global settings
      File settingsFile = new File(Context.getStaticFileBase(), Context.cntx.getProperty("userLibCfg", "users/settings.json"));
      JSONObject settings = Utilities.readJSON(new FileInputStream(settingsFile));
      // Get the current generic disk quota
      quota = settings.optLong("quota", quota);
      // Check if user is allowed to login, and load specific user settings
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
        // Unathorized used
        throw new Exception("Usuari no autoritzat: " + email);
      }

      // Get user ID from email
      userId = getPlainId(email, "xtec.cat");
      // Initialize the UserSpace object
      userSpace = new UserSpace(userId, new File(getRootBase(), userId));
      userSpace.readProjects();

      // Get additional data
      fullUserName = json.getString("name");
      avatar = json.optString("picture", null);
      expires = new Date(json.getLong("exp") * 1000);

      // Create a session object and save current user's data on it
      setSessionAttributes(request.getSession(true));

    } else {
      // No token presented
      // Try to read settings from a stored session, if any
      getSessionAttributes(request.getSession(false));
    }
  }

  // Encapsulates current user data in a JSON expression, ready to be sent
  public JSONObject getJsonUserInfo() throws Exception {
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
    return json;
  }

  // Save current user data in current session object
  protected void setSessionAttributes(HttpSession session) throws Exception {
    if (session != null) {
      // Retain sessions only for 30'
      session.setMaxInactiveInterval(1800);

      // Save user settings in the HttpSession object
      session.setAttribute("email", email);
      session.setAttribute("id", userId);
      session.setAttribute("quota", new Long(quota));
      session.setAttribute("fullUserName", fullUserName);
      session.setAttribute("avatar", avatar == null ? "" : avatar);
      session.setAttribute("expires", expires);
      session.setAttribute("userSpace", userSpace);
    }
  }

  // Retrieve user data from current session object, if any
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

  // Miscellaneous functions
  // Gets a simplified user ID from its e-mail address
  // Only plain letters, digits and dots are allowed
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

  // Initialize the ROOT_BASE variable, reading it from "Context" if needed
  public static File getRootBase() throws IOException {
    if (ROOT_BASE == null) {
      ROOT_BASE = new File(Context.getStaticFileBase(), Context.cntx.getProperty("userLibRoot", "users"));
      if (!ROOT_BASE.canWrite()) // Root directory must be writable
      {
        throw new IOException("Invalid root base!");
      }
    }
    return ROOT_BASE;
  }

  /* ---------------------------------------

Log table created with:
------------------------------------------  
CREATE TABLE  "LOG_USERACTIONS" 
   (	"LOG_DATE" DATE NOT NULL ENABLE, 
	"LOG_TYPE" CHAR(5) NOT NULL ENABLE, 
	"LOG_USER" VARCHAR2(100), 
	"LOG_MSG" VARCHAR2(1024)
   )
/

CREATE INDEX  "LOG_USERACTIONS_LOG_DATE_IDX" ON  "LOG_USERACTIONS" ("LOG_DATE")
/

CREATE INDEX  "LOG_USERACTIONS_LOG_TYPE_IDX" ON  "LOG_USERACTIONS" ("LOG_TYPE")
/
------------------------------------------ */
  // Appends a message to the log table
  protected void logMsg(String type, String msg) {
    ConnectionBean con = null;
    PreparedStatement stmt = null;
    try {
      con = Context.cntx.getDB().getConnectionBean();
      stmt = con.getPreparedStatement("INSERT INTO CLIC.LOG_USERACTIONS (LOG_DATE, LOG_TYPE, LOG_USER, LOG_MSG) VALUES (?,?,?,?)");
      stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
      stmt.setString(2, type);
      stmt.setString(3, userId == null ? "unknown" : userId);
      stmt.setString(4, msg);
      stmt.execute();
    } catch (Exception ex) {
      System.err.println("Unable to write logs to DB due to: " + ex.getMessage());
    } finally {
      if (con != null) {
        try {
          con.closeStatement(stmt);
          Context.cntx.getDB().freeConnectionBean(con);
        } catch (Exception ex) {
          System.err.println("Unable to close DB connection due to: " + ex.getMessage());
        }
      }
    }
  }

}
