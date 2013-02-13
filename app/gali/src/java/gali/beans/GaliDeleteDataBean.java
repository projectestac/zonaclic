/*
 * GaliDeleteDataBean.java
 *
 * Created on 29 de septiembre de 2003, 17:54
 */

package gali.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  fbusquet
 */
public class GaliDeleteDataBean extends GaliBean {
    
    protected boolean start(){
        boolean result=super.start() && (getUserId()!=null);
        if(result){
            try{
                result=gali.GaliDataManage.deleteUserData(getUserId(), session);
            } catch(Exception ex){
                System.err.println(ex);
                result=false;
            }
        }
        return result;
    }    
    
}
