/*
 * GaliDummyBean.java
 *
 * Created on 1 / juliol / 2003, 09:13
 */

package gali.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  allastar
 */
public class GaliDummyBean extends GaliBean{
    
    /*
    public GaliDummyBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    } 
     */   
    
    protected boolean start() {
        super.start();
        getVar();
        return true;
    }
    
}
