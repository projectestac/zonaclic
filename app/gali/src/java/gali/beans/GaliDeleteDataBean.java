/*
 * GaliDeleteDataBean.java
 *
 * Created on 29 de septiembre de 2003, 17:54
 */

package gali.beans;

/**
 *
 * @author  fbusquet
 */
public class GaliDeleteDataBean extends GaliBean {
    
    @Override
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
