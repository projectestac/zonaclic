/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

/**
 *
 * @author fbusquet
 */
public class UsrDeleteBean extends UsrLibBean {
  String status = "processing";
  String err;
  UserProject prj = null;

  //@Override
  protected void getRequestParams(HttpServletRequest request) throws Exception {
    super.getRequestParams(request);
    if (email != null && userSpace != null) {
      status = "error";
      err = "bad request";
      if(userSpace.removeProject(Utilities.getParam(request, "project", null))){
        setSessionAttributes(request.getSession(false));
        status = "ok";
        err = null; 
      }
    } else {
      status = "error";
      err = "bad user";
    }
  }
  
  public String getJsonResponse() {
    String result;
    try {
      JSONObject json = new JSONObject();
      if (email == null) {
        json.put("status", "error");
        json.put("error", "invalid user");
      } else {
        json.put("status", status);
        if (!status.equals("ok") && err != null) {
          json.put("error", err);
        }
      }
      result = json.toString(1);
    } catch (Exception ex) {
      result = "{\"status\":\"error\",\"error\":\"invalid data\"}";
    }
    return result;
  }
  
}
