/*
 * GaliDiagnosiBean.java
 *
 * Created on 30 / juny / 2003, 12:07
 */

package gali.beans;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  allastar
 */
public class GaliDiagnosiBean extends GaliBean{
    
    public static final String PACKAGE_NAME="td";
    
    protected boolean start(){
        boolean result=super.start();
        if(result && getUserId()!=null)
            gali.GaliDataManage.setPassedGaliPackages(getUserId(), null, session); //Esborro de la sessió la info referent al test de diagnosi. (Es tornarà a calcular)        
        return result;
    }
    
    public String getExitURL(){
        return getServerBaseUrl()+"/"+JSP_RESULT_TD;
    }
    
    public String getKey(){
        return createRandomKey();
    }
        
    public String getReporterParams(){
        StringBuffer sb=new StringBuffer("path=");
        sb.append(getSetting("reporterPath"));
        sb.append(";user=").append(getUserId());
        sb.append(";key=").append(getKey());
        sb.append(";code=gali");
        return sb.toString();
    }
}
