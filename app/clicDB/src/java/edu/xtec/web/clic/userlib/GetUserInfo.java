/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    try {
      loadUserData(request);
      writer.write(getJsonUserInfo().toString());
      logMsg("INFO", userId, "User logged in");
    } catch (Exception ex) {
      writer.print("{\"status\":\"error\",\"error\":\"" + JSONStringer.getString(ex.getMessage()) + "\"}");
      logMsg("ERROR", userId == null ? "unknown" : userId, "Log in incorrect: "+ex.getMessage());
    } finally {
      writer.flush();
    }
  }

  protected void loadUserData(HttpServletRequest request) throws Exception {

    // Read root base location
    if (ROOT_BASE == null) {      
      ROOT_BASE = new File(Context.getStaticFileBase(), Context.cntx.getProperty("userLibRoot", "users"));
      if (!ROOT_BASE.canWrite()) {
        throw new Exception("Invalid root base!");
      }
    }

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
      String hd = json.optString("hd", "");

      // Read settings
      File settingsFile = new File(Context.getStaticFileBase(), Context.cntx.getProperty("userLibCfg", "users/settings.json"));
      JSONObject settings = Utilities.readJSON(new FileInputStream(settingsFile));
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
        throw new Exception("Usuari no autoritzat: "+email);
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
  
  /**
   * TAULA CREADA AMB:
   * CREATE TABLE CLIC.LOG_USERACTIONS
   * (
   *   LOG_DATE DATE,
   *   LOG_TYPE CHAR(5),
   *   LOG_USER VARCHAR2(100),
   *   LOG_MSG VARCHAR2(2014)
   * )
   */  
  protected void logMsg(String type, String user, String msg){
    ConnectionBean con = null;
    PreparedStatement stmt = null;
    try {
      con = Context.cntx.getDB().getConnectionBean();
      stmt = con.getPreparedStatement("INSERT INTO CLIC.LOG_USERACTIONS (LOG_DATE, LOG_TYPE, LOG_USER, LOG_MSG) VALUES (?,?,?,?)");
      stmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
      stmt.setString(2, type);
      stmt.setString(3, user==null ? "" : user);
      stmt.setString(4, msg);
      stmt.execute();
    } catch(Exception ex) {
      System.err.println("Unable to write logs to DB due to: "+ex.getMessage());      
    } finally {
      if(con!=null) {
        try {
          con.closeStatement(stmt);
          Context.cntx.getDB().freeConnectionBean(con);
        } catch(Exception ex) {
          System.err.println("Unable to close DB connection due to: "+ex.getMessage());
        }
      }
    }
  }

}
