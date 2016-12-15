/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic.userlib;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONStringer;

/**
 *
 * @author fbusquet
 */
public class UploadScormFile extends GetUserInfo {
  
  String status = "processing";
  String err;
  UserProject prj = null;
  
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter writer = response.getWriter();

    try {
      loadUserData(request);
      if (email != null && userSpace != null) {
        
      }
      writer.write(getJsonUserInfo().toString());
    } catch (Exception ex) {
      writer.print("{\"status\":\"error\",\"error\":\"" + JSONStringer.getString(ex.getMessage()) + "\"}");
    } finally {
      writer.flush();
    }
  }
  
  
  
  
}
