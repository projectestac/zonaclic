/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

/**
 *
 * @author fbusquet
 */
public class UsrLibBean extends PageBean {

  public static final String ID_TOKEN = "id_token";
  public static final String CHECK_GOOGLE_TOKEN = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
  public String token;
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
    token = getParam(request, ID_TOKEN, null);
  }

  protected void process(ConnectionBean con) throws Exception {
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
      email = json.optString("email", "-");
      // TODO: Check email
      fullUserName = json.getString("name");
      avatar = json.optString("picture", "");
      expires = new Date(json.getLong("exp"));
      expDate = DateFormat.getDateTimeInstance().format(expires);
    }
  }

  public String getGoogleToken() {
    return Context.cntx.getProperty("googleClientId", "");
  }

}
