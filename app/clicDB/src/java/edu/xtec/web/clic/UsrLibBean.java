package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author fbusquet
 */
public class UsrLibBean extends PageBean {

  public static final String ID_TOKEN = "id_token";
  public static final String CHECK_GOOGLE_TOKEN = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
  public boolean status = true;
  public String email;
  public String fullUserName;
  public String avatar;
  public Date expires;
  public String expDate;

  public UsrLibBean() {
    super();
  }

  protected String getMainBundle() {
    return "edu.xtec.resources.messages.usrLibMessages";
  }

  //@Override
  protected void getRequestParams(HttpServletRequest request) throws Exception {
    super.getRequestParams(request);
    HttpSession session;
    String token = getParam(request, ID_TOKEN, null);
    if (token != null) {
      URL verifyURL = new URL(CHECK_GOOGLE_TOKEN + token);
      BufferedReader in = new BufferedReader(
              new InputStreamReader(verifyURL.openStream()));
      StringBuffer sb = new StringBuffer(500);
      String inputLine;
      while ((inputLine = in.readLine()) != null) {
        sb.append(inputLine);
      }
      JSONObject json = new JSONObject(sb.toString());
      session = request.getSession(true);
      email = json.getString("email");
      session.setAttribute("email", email);
      // TODO: Check email
      fullUserName = json.getString("name");
      session.setAttribute("fullUserName", fullUserName);
      avatar = json.optString("picture", null);
      session.setAttribute("avatar", avatar == null ? "" : avatar);
      expires = new Date(json.getLong("exp") * 1000);
      session.setAttribute("expires", expires);
    } else {
      session = request.getSession(false);
      if (session != null) {
        email = (String) session.getAttribute("email");
        fullUserName = (String) session.getAttribute("fullUserName");
        avatar = (String) session.getAttribute("avatar");
        expires = (Date) session.getAttribute("expires");
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
        json.put("fullUserName", fullUserName);
        json.putOpt("avatar", avatar);
        json.put("expires", DateFormat.getDateTimeInstance().format(expires));
        json.put("status", "validated");
      }
      result = json.toString(1);
    } catch (Exception ex) {
      result = "{\"status\":\"error\",\"error\":\"invalid data\"}";
    }
    return result;
  }

}
