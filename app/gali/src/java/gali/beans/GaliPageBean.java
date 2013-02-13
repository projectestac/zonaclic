/*
 * GaliPageBean.java
 *
 * Created on 30 / juny / 2003, 11:23
 */

package gali.beans;

/**
 *
 * @author  allastar
 */
public class GaliPageBean extends GaliBean{
    
    public int iPage, iLevel;
    public String[][] packInfo;

    /*
    public GaliPageBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    } 
     */   
    
    protected boolean start(){
        boolean result=super.start();
        if(result){
            iPage = getIntParameter(PAGE, -1);
            iLevel = getIntParameter(LEVEL, INI_LEVEL);
            packInfo = gali.GaliDataManage.getPackageInfo(iPage, iLevel);
            result=(iPage>=0 && packInfo!=null);
        }
        return result;
    }
    
    /*
    public void init(){
        db.setSession(session);
        sUserId=getEduUser(request,session);
        bCons=(sUserId!=null)?getBooleanParameter(request,session,"cons",false):true;
        iPage=getIntParameter(request,session,"page",-1);
        iLevel=getIntParameter(request,session,"level",db.INI_LEVEL);
        packInfo=db.getPackageInfo(iPage,iLevel);
        init=true;
    }
     */
    
    public int getPackagesLength(){
        return packInfo.length;
    }
    
    public boolean hasPassed(int iPackage){
        //if (!init) init();
        return !isCons() && gali.GaliDataManage.hasPassedPackage(getUserId(), iPage, iPackage, iLevel, session);
    }
    
    public String getIconUrl(int iPackage){
        return getIconUrl(hasPassed(iPackage), iLevel);
    }
    
    public String getDescription(int iPackage){
        //if (!init) init();
        String result="-";
        if(iPackage>=0 && iPackage<packInfo.length)
            result=gali.GaliDataManage.getPackageDescription(packInfo[iPackage][0], getVar());
        return result;
    }
    
    public String getPageDescription(){
        //if (!init) init();
        String result="-";
        if(iPage>=0 && iPage<PAGES.length)
            result=gali.GaliDataManage.getPackageDescription(PAGES[iPage], getVar());
        return result;
    }
    
    public String getIconUrl(){
        //if (!init) init();
        String result="";
        if(iPage>=0 && iPage<PAGES.length)
            result=gali.GaliDataManage.getPageIconURL(PAGES[iPage]);
        return result;
    }
}
