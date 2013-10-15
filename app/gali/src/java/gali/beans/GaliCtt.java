/*
 * GaliCtt.java
 *
 * Created on 30 / juny / 2003, 10:56
 */

package gali.beans;

import javax.servlet.http.*;

/**
 *
 * @author  allastar
 */
public abstract class GaliCtt {
        
    public static final String KEY="key";
    public static final String DB="db";
    public static final String CONS="cons";
    public static final String PAGE="page";
    public static final String PACK="pack";    
    public static final String LEVEL="level";
    public static final String LIGHT="light";
    public static final String VAR="var";
    public static final int UNDEFINED=0, NO=1, YES=2;
    public static final String EQ="=", SI="si", NULL="null";
    // Variants Oriental i Nord-occidental
    public static final String OR="or", NOC="noc", ALG="alg", OC="oc";
    public static final int VAR_OR=0, VAR_NOC=1, VAR_ALG=2, VAR_OC=3;
    
    public static final String PASSED_ICON_INI_URL="led_verd_ok.gif";
    public static final String NORMAL_ICON_INI_URL="led_verd.gif";
    public static final String PASSED_ICON_PROF_URL="led_taronja_ok.gif";
    public static final String NORMAL_ICON_PROF_URL="led_taronja.gif";
    
    public static final String[] PAGES={
        "com_escriu", "qui_soc", "quina_hora", "menjo", "faig", "morfosintaxi"
    };

    public static final String JSP_INDEX="galiMain.jsp";
    public static final String JSP_GUIDED="galiGuided.jsp";
    public static final String JSP_PAGE="galiPage.jsp";
    public static final String JSP_RESULT="galiResult.jsp";
    public static final String JSP_RESULT_TD="galiResultTD.jsp";
    public static final String JSP_FI="galiFi.jsp";
    public static final String JSP_ERROR="error.html";

    public static final int INI_LEVEL=0;
    public static final int PROF_LEVEL=1;

    //private static Properties settings;
    //public static final String SETTINGS_PATH="/gali/settings.properties";
    
    public static final String USER_KEY="usuari-xtec";

    HttpServletRequest request;
    HttpServletResponse response;
    HttpSession session;
    private String sUserId;
    private Boolean cons;
    private Integer var;
    protected boolean initiated;

    /*
    static{
        settings=new Properties();
        try{
            settings.load(GaliCtt.class.getResourceAsStream(SETTINGS_PATH));
        } catch(Exception ex){
            System.err.println("ERROR FATAL!\nNo s'ha pogut llegir el fitxer "+SETTINGS_PATH);
            ex.printStackTrace(System.err);
        }
    }
     */
    
    public boolean init(HttpServletRequest request, HttpSession session, HttpServletResponse response){
        boolean result=initiated;
        if(!initiated){
            this.request=request;
            this.session=session;
            this.response=response;
            result=start();
            initiated=true;
        }
        return result;
    }
    
    protected abstract boolean start();

    public static String getSetting(String key){
        //return settings.getProperty(key);
        return gali.Context.cntx.getProperty(key);
    }
    
    public String getBaseUrl(boolean bCons){
        return getUrl(JSP_INDEX, -1, -1, -1, bCons);
    }
    
    public String getUrl(String sPage){
        return getUrl(sPage, -1, -1, -1, false);
    }
    
    public String getUrl(String sPage, int iPage, int iPackage, int iLevel, boolean bCons){
        /* Retorna la url que apunta a sPage amb els paràmetres adients. Només 
         contindrà els paràmetres no nuls i no negatius. */
        StringBuffer sb=new StringBuffer(sPage);
        int iParams=0;
        if (iPage>=0){
            sb.append(iParams++==0 ? '?' : '&');
            sb.append(PAGE).append(EQ).append(iPage);
        }
        if (iPackage>=0){
            sb.append(iParams++==0 ? '?' : '&');
            sb.append(PACK).append(EQ).append(iPackage);
        }
        if (iLevel>=0){
            sb.append(iParams++==0 ? '?' : '&');
            sb.append(LEVEL).append(EQ).append(iLevel);
        }
        if (bCons){
            sb.append(iParams++==0 ? '?' : '&');
            sb.append(CONS).append(EQ).append(SI);
        }
        return sb.toString();
    }
    
    public String getIconUrl(boolean passed, int iLevel){
        if (iLevel==INI_LEVEL)
            return passed ? PASSED_ICON_INI_URL : NORMAL_ICON_INI_URL;
        else
            return passed ? PASSED_ICON_PROF_URL : NORMAL_ICON_PROF_URL;
    }
    
    public String getUserId(){
        if(sUserId==null){
            Cookie[] cookies=request.getCookies();
            if(cookies!=null){
                for(int i=0; i<cookies.length; i++){
                    Cookie c=cookies[i];
                    if(c.getName().equals(USER_KEY) && c.getValue()!=null){
                        sUserId=c.getValue().trim();
                        break;
                    }
                }
            }                    
        }        
        return sUserId;
    }
    
    public String getLiteralUserId(){        
        return isCons() ? "&nbsp;" : getUserId();        
    }
    
    public boolean isCons(){
        if(cons==null){
            cons = new Boolean(getUserId()==null || getBooleanParameter(CONS, false));
        }
        return cons.booleanValue();
    }
    
    public int getVar(){
        if(var==null){
            int result=VAR_OR;
            String s=getParameter(VAR);
            if(s!=null && s.equals(NOC))
                result=VAR_NOC;
            else if(s!=null && s.equals(ALG))
                result=VAR_ALG;
            else if(s!=null && s.equals(OC))
                result=VAR_OC;
            var=new Integer(result);
            
        }
        return var.intValue();    
    }
    
    public int getIntParameter(String sParam, int iDefault){
        /* Retorna un paràmetre enter. Veure 'getParameter'.*/
        int result=iDefault;
        try{
            String s=getParameter(sParam);
            if (s!=null)
                result=Integer.parseInt(s);
        }
        catch (Exception e){
            // Bad param!
        }
        return result;
    }

    public boolean getBooleanParameter(String sParam, boolean bDefault){
        /* Retorna un paràmetre booleà. Veure 'getParameter'.*/
        boolean result=bDefault;
        try{
            String s=getParameter(sParam);
            if (s!=null)
                result=s.trim().equalsIgnoreCase(SI);            
        }
        catch (Exception e){
            // Bad param!
        }
        return result;
    }

    public String getParameter(String sParam){
        /* Si existeix un paràmetre amb nom='sParam' l'afegeix a la sessió i el
        retorna. Si no existeix, el busca a la sessió. */
        String result=request.getParameter(sParam);
        if (result==null || result.trim().equalsIgnoreCase(NULL)){
            Object o=session.getAttribute(sParam);
            if(o!=null)
                result=o.toString();
        }
        else 
            session.setAttribute(sParam, result);
        return result;
    }

    protected String createRandomKey(){
        /* Retorna un enter aleatori [0,1024^3] i l'afegeix a la sessió amb clau='key' */
        String result=((int)(Math.random()*(1024*1024*1024)))+"";
        session.setAttribute(KEY, result);
        return result;
    }
}
