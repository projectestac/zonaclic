/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import edu.xtec.web.clic.Context;

/**
 *
 * @author fbusquet
 */
public class UsrLibBean extends PageBean {

  public UsrLibBean() {
    super();
  }

  protected String getMainBundle() {
    return "edu.xtec.resources.messages.usrLibMessages";
  }

  protected void process(ConnectionBean con) throws Exception {
    // does nothing
  }
  
  public String getGoogleToken() {
    return Context.cntx.getProperty("googleClientId", "");
  }

}
