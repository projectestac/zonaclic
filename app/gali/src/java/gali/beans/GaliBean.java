/*
 * GaliBean.java
 *
 * Created on 30 / juny / 2003, 10:46
 */

package gali.beans;

import javax.servlet.http.*;

/**
 *
 * @author  allastar
 */
public abstract class GaliBean extends GaliCtt{

    //public static final String CHECK_IDENT_URL_KEY="CHECK_IDENT_URL";
    //public static final String SERVER_DOMAIN_KEY="SERVER_DOMAIN";
    //public static final String REPORTER_PATH_KEY="REPORTER_PATH";
    //public static final int CACHED_PORT=7777, DEFAULT_PORT=80;
    
    public boolean ok;
    
    protected boolean start(){
        writeCacheInfo();
        return true;
    }
    
    public void writeCacheInfo(){
        if(isNoCache() && !response.isCommitted()){
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);        
        }        
    }
    
    protected boolean isNoCache(){
        return true;
    }
    
    private String serverDomain=getSetting("serverDomain");
    private String serverBaseUrl;
    public String getServerBaseUrl(){
        if(serverBaseUrl==null){        

            serverBaseUrl=getSetting("servletBaseUrl");

            // VERSIO AMB SERVLETS NOUS:
            /*
            String s=request.getRequestURL().toString();
            serverBaseUrl=s.substring(0, s.lastIndexOf('/'));
            try{
                StringBuffer result=new StringBuffer("http://");
                java.net.URL url=new java.net.URL(serverBaseUrl);
                String host=url.getHost();
                if(!host.endsWith(serverDomain)){
                    int l=host.indexOf('.');
                    if(l>0)
                        host=host.substring(0, l);
                    host=host+"."+serverDomain;
                }
                result.append(host);
                int port=url.getPort();
                if(port>0 && port!=CACHED_PORT)
                    result.append(":").append(port);
                result.append(url.getFile());
                serverBaseUrl=result.toString();
            }
            catch(Exception ex){
                System.err.println("Error corregint la URL "+serverBaseUrl+"\n"+ex);
            }
             */
        }        
        return serverBaseUrl;
    }
    
    public String getPackageURL(){
        int var=getVar();
        return getSetting(var==VAR_NOC ? "packageNOCUrl" :
            var==VAR_ALG ? "packageALGUrl" : "packageUrl");
    }
    
    private String getValidateURL(String redirectPage){
        //String s=getSetting("checkIdentUrl")+"?p_username=&p_password=&p_url="+getServerBaseUrl()+"/"+redirectPage;
        String s=getSetting("checkIdentUrl")+"?logo="+getSetting("galiLogo")+"&url="+getServerBaseUrl()+"/"+redirectPage;
        return s;
    }
    
    public void redirectToValidation(String returnPage){
        try{
            response.sendRedirect(getValidateURL(returnPage));
        }
        catch (Exception ex){
            System.err.println("Error regirigint la resposta a "+returnPage+":\n"+ex);
        }        
    }    
}
