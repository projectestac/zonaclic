/*
 * GaliMainBean.java
 *
 * Created on 30 / juny / 2003, 10:45
 */

package gali.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  allastar
 */
public class GaliMainBean extends GaliBean{


    /*
    public GaliMainBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    } 
     */   
    
    protected boolean start() {
        return super.start();
    }
    
    /*
    public void init(){
        db.setSession(session);
        sUserId=getEduUser(request,session);
        bCons=(sUserId!=null)?getBooleanParameter(request,session,"cons",false):true;
        //System.out.println("bCons és "+bCons);
        init=true;
    }
     */
    
    /*
    public int getPagesLength(){
        return PAGES.length;
    }
     */
    
    public boolean hasPassed(int iPage, int iLevel){
        //if (!init) init();
        return !isCons() && gali.GaliDataManage.hasPassedPage(iPage, iLevel, getUserId(), session);
    }
    
    public String getIconUrl(int page, int level){
        return getIconUrl(hasPassed(page, level), level);
    }
    
    public String getDescription(int iPage){
        //if (!init) init();
        String result="-";
        if(iPage>=0 && iPage<PAGES.length)
            result=gali.GaliDataManage.getPackageDescription(PAGES[iPage], getVar());
        return result;
    }
    
}
