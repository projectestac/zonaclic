/*
 * GaliPackageBean.java
 *
 * Created on 30 / juny / 2003, 11:44
 */

package gali.beans;

/**
 *
 * @author  allastar
 */
public class GaliPackageBean extends GaliBean{
    
    public int light, iPage, iPackage, iLevel;
    public String[][] packInfo;
    public String sKey;


    /*
    public GaliPackageBean(HttpServletRequest request, HttpSession session, HttpServletResponse response) {
        super(request, session, response);
    } 
     */   
    
    @Override
    protected boolean start() {
        boolean result=super.start();
        if(result){
            iPage = getIntParameter(PAGE, -1);
            iPackage = getIntParameter(PACK, -1);
            iLevel = getIntParameter(LEVEL, INI_LEVEL);
            light = getIntParameter(LIGHT, UNDEFINED);
            packInfo = gali.GaliDataManage.getPackageInfo(iPage, iLevel);
            if(iPage<0 || iPackage<0 || packInfo==null)
                result=false;
        }
        
        if(result && !isCons())
            gali.GaliDataManage.setPassedPackage(getUserId(), iPage, iPackage, iLevel, null, session); //Esborro de la sessió la info referent al package que vol fer. (Es tornarà a calcular)
        sKey = createRandomKey();
        return result;
    }
    
    /*
    public void init() {
        db.setSession(session);
        sUserId=getEduUser(request,session);
        iPage=getIntParameter(request,session,"page",-1);
        iPackage=getIntParameter(request,session,"pack",-1);
        iLevel=getIntParameter(request,session,"level",db.INI_LEVEL);
        light=getIntParameter(request, session, LIGHT, UNDEFINED);
        session.setAttribute(LIGHT, Integer.toString(light));
        bCons= sUserId==null || getBooleanParameter(request,session,"cons",false);
        packInfo=db.getPackageInfo(iPage,iLevel);
        if (!bCons) db.setPassedPackage(sUserId, iPage, iPackage, iLevel, null); //Esborro de la sessió la info referent al package que vol fer. (Es tornarà a calcular)
        sKey=createRandomKey(session);
        init=true;
    }
     */
    
    @Override
    public String getUrl(String url){
        //if(!init) init();
        return getUrl(url, iPage, iPackage, iLevel, isCons());
        //return "page="+iPage+"&pack="+iPackage+"&level="+iLevel+"&cons="+bCons;
    } 
    
    public String getPackageName(){
        //if (!init) init();
        return (iPackage>=0 && iPackage<packInfo.length) ? packInfo[iPackage][1] : "";
    }
    
    public String getExitURL(){
        //if (!init) init();
        String exitUrl = getServerBaseUrl() + "/" + (!isCons() ? JSP_RESULT : JSP_PAGE);
        //String exitUrl=(!bCons)?SERVER_URL_BASE+RESULT:SERVER_URL_BASE+PAGE; //Si és una consulta no podrà veure el resultat
        return exitUrl;
    }
    
    public String getKey(){
        //if (!init) init();
        return sKey;
    }

    /*
    public boolean isConsulta(){
        //if (!init) init();
        return bCons;
    }
     */
    
    public int getLight(){
        //if(!init) init();
        return light;
    }
    
    public boolean isLight(){
        return getLight()==YES;
    }
    
    public String getReporterParams(){
        StringBuilder sb=new StringBuilder("path=");
        sb.append(getSetting("reporterPath"));
        sb.append(";user=").append(getUserId());
        sb.append(";key=").append(getKey());
        sb.append(";code=gali");
        return sb.toString();
    }
    
}
