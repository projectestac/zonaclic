/*
 * GaliIndexBean.java
 *
 * Created on 1 / juliol / 2003, 08:54
 */

package gali.beans;

/**
 *
 * @author  allastar
 */
public class GaliGuidedBean extends GaliBean{
    

    /*
    public GaliGuidedBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    } 
     */   
    
    //@Override
    protected boolean start() {
        return super.start();
    }
    
    public boolean isValidated(){
        //if (!init) init();
        return (getUserId()!=null && getUserId().length()>0);
    }
    
    public boolean hasDoneGaliActivity(){
        //if (!init) init();
        boolean result=isValidated();
        if(result){
            try{
                result=result && gali.GaliDataManage.hasDoneGaliActivity(getUserId());
            } catch(Exception ex){
                System.err.println(ex);
                result=false;
            }
        }
        return result;
    }
}
