/*
 * GaliDummyBean.java
 *
 * Created on 1 / juliol / 2003, 09:13
 */

package gali.beans;

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
    
    @Override
    protected boolean start() {
        super.start();
        getVar();
        return true;
    }
    
}
