/*
 * GaliResultBean.java
 *
 * Created on 30 / juny / 2003, 11:56
 */

package gali.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  allastar
 */
public class GaliResultBean extends GaliBean{
    
    public double qualification;
    public String continueUrl;
    public boolean isOk;
    public boolean incomplete;
    
    /*
    public GaliResultBean() {
        super(null, null, null);
    }
     */
    
    /*
    public GaliResultBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    }
     */
    
    protected boolean start(){
        boolean result=super.start();
        if(result){
            int iPage=getIntParameter(PAGE, -1);
            int iPackage=getIntParameter(PACK, -1);
            int iLevel=getIntParameter(LEVEL, INI_LEVEL);
            String sKey=getParameter(KEY);
            if(!isCons()){
                if(sKey!=null && sKey.trim().length()>0)
                    try{
                        qualification=gali.GaliDataManage.getPackageQualificationSessionKey(getUserId(), sKey);
                    } catch(Exception ex){
                        System.err.println(ex);
                    }
                if(iPage>=0)
                    continueUrl=gali.GaliDataManage.hasPassedAllPackages(getUserId(), session) ? JSP_FI : JSP_PAGE;
                else // Ve del test de diagnosi
                    continueUrl=gali.GaliDataManage.hasPassedAllPackages(getUserId(), session) ? JSP_FI : JSP_INDEX;
            
                //String sPackName;
                if(iPage>=0){ // Ve d'un paquet Galí (no test de diagnosi)
                    String[][] packInfo=gali.GaliDataManage.getPackageInfo(iPage, iLevel);
                    if(iPackage>=0 && iPackage<packInfo.length){
                        //sPackName=packInfo[iPackage][1];
                        try{
                            incomplete=!gali.GaliDataManage.consolidateGaliInfo(getUserId(), packInfo[iPackage][1]); // Consolidem la informació d'aquest usuari a aquest paquet
                        } catch(Exception ex){
                            System.err.println(ex);
                        }
                        isOk=qualification>=getMinimumPassedQualification();
                    }
                }                
            }
            else
                continueUrl=JSP_PAGE;
        }        
        return result;
    }
    
    /*
    public void init() {
        db.setSession(session);
        String sUserId=getEduUser(request,session);
        int iPage=getIntParameter(request,session,"page",-1);
        int iPackage=getIntParameter(request,session,"pack",-1);
        int iLevel=getIntParameter(request,session,"level",db.INI_LEVEL);
        boolean bCons=(sUserId!=null)?getBooleanParameter(request,session,"cons",false):true;
        String sKey=getParameter(request,session,"key");
     
        qualification=(sKey!=null && sKey.trim().length()>0)?db.getPackageQualificationSessionKey(sUserId, sKey):0;
     
        if (iPage>=0)
            continueUrl=(!bCons && db.hasPassedAllPackages(sUserId))?FI:PAGE;
        else // Ve del test de diagnosi
            continueUrl=(!bCons && db.hasPassedAllPackages(sUserId))?FI:PAGE_INDEX;
     
        if (!bCons){
            String sPackName;
            if (iPage>0){ // Ve d'un paquet Galí (no test de diagnosi)
                String[][] packInfo=db.getPackageInfo(iPage,iLevel);
                sPackName=packInfo[iPackage][1];
                db.consolidateGaliInfo(sUserId, sPackName); // Consolidem la informació d'aquest usuari a aquest paquet
            }
            else sPackName="td"; // De moment, no es consoliden les del test de diagnosi
            //db.consolidateGaliInfo(sUserId, sPackName); // Consolidem la informació d'aquest usuari a aquest paquet
        }
        init=true;
    }
     */
    
    public String getContinueURL(){
        //if (!init) init();
        return continueUrl;
    }
    
    public double getQualification(){
        //if (!init) init();
        return qualification;
    }
    
    public double getMinimumPassedQualification(){
        return gali.GaliDataManage.MINIMUM_PASSED_QUALIFICATION;
    }
}
