/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import edu.xtec.web.clic.Utilities;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.JSONStringer;

/**
 *
 * @author Francesc Busquets <francesc@gmail.com>
 */
public class DeleteProject extends GetUserInfo {

  String status;
  String err;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    try {
      loadUserData(request);
      if (email != null && userSpace != null) {
        if (userSpace.removeProject(Utilities.getParam(request, "project", null))) {
          setSessionAttributes(request.getSession(false));
          status = "ok";
        } else {
          status = "error";
          err = "Transacció incorrecta";
        }
      }
      writer.write(getJsonResponse().toString());
    } catch (Exception ex) {
      writer.print("{\"status\":\"error\",\"error\":\"" + JSONStringer.getString(ex.getMessage()) + "\"}");
    } finally {
      writer.flush();
    }
  }

  public JSONObject getJsonResponse() throws Exception {
    JSONObject json = new JSONObject();
    if (email == null || userSpace == null) {
      json.put("status", "error");
      json.put("error", "Usuari incorrecte");
    } else {
      json.put("status", status);
      if (!status.equals("ok") && err != null) {
        json.put("error", err);
      }
    }
    return json;
  }

}
