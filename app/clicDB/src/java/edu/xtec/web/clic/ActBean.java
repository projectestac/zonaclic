/*
 * ActBean.java
 *
 * Created on 4 de julio de 2003, 12:44
 */
package edu.xtec.web.clic;

//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public class ActBean extends PageBean {

    // Par�metres
    public static final String ID = "id";
    // Constants
    public static final String ROL_AUTOR = "aut", JCLIC = "j", CLIC = "c";
    public static final String[] LLOCS = {"MUNICIPI", "COMARCA", "COMUNITAT", "PAIS"};
    public static final String[] NO_COMARCA = {"Barcelona", "Tarragona", "Lleida", "Girona"};
    public long id;
    public String title;
    public boolean firstTR;
    public int imgCounter = 0;
    public WActivitats act;
    public String[][] infoCaixes;

    /**
     * Creates a new instance of ActBean
     */
    public ActBean() {
        super();
    }
    
    protected String getMainBundle() {
        return "edu.xtec.resources.messages.activityMessages";
    }
    
    protected void getRequestParams(HttpServletRequest request) throws Exception {
        super.getRequestParams(request);
        String s = getParam(request, ID, null);
        id = 1148;
        if (s != null && s.length() > 0) {
            try {
                id = Long.parseLong(s);
            } catch (NumberFormatException ex) {
                System.err.println((new java.util.Date()).toString() + " - ActBean - Invalid ID param: " + s);
            }
        }
    }
    
    public WVersions getFirstJClic() {
        WVersions result = null;
        if (act != null && act.versions != null) {
            for (int i = 0; i < act.versions.length; i++) {
                WVersions wv = act.versions[i];
                if ((wv.esPublic || checking) && "j".equals(wv.tipus)) {
                    if (wv.hasLang(lang)) {
                        result = wv;
                        break;
                    } else if (result == null) {
                        result = wv;
                    }
                }
            }
        }
        return result;
    }
    
    protected void process(edu.xtec.util.db.ConnectionBean con) throws Exception {
        act = new WActivitats();
        act.load(id, con, lang);
        if (!checking && !act.valid) {
            throw new Exception("Invalid activity ID: " + id);
        }
        infoCaixes = getInfoCaixes();
    }
    
    public String getTitle() {
        return act.titolEnc;
        //return Utilities.xmlEncode(act.titol);
    }
    
    public String getDescriptors() {
        return enumItems(act.descriptors);
    }
    
    public String getTopicTitle() {
        return getMsg(act.arees.length > 1 ? "topics" : "topic");
    }
    
    public String getLevelTitle() {
        return getMsg(act.nivells.length > 1 ? "levels" : "level");
    }
    
    public String getVerTitle(WVersions v) {
        
        StringBuffer sb = new StringBuffer();
        String d = v.descripcio;
        if (d != null && d.length() > 0) {
            sb.append(Utilities.xmlEncode(d)).append(" - ");
        }
        
        sb.append(bundle.getString("j".equals(v.tipus) ? "ver_jclic" : "ver_clic"));
        String ident = enumItems(v.idiomes);
        if (ident != null && ident.length() > 0) {
            sb.append(" - ").append(ident);
        }
        
        String et = v.etiqueta;
        if (et != null && et.length() > 0) {
            sb.append(" (").append(et).append(")");
        }
        
        return sb.substring(0);
    }
    
    public String getAppletExtraParams(WVersions v) {
        StringBuffer sb = new StringBuffer();
        if (v.idiomes.length > 0) {
            sb.append("&lang=").append(v.idiomes[0].codiIdioma);
        }
        if (act.titol != null && act.titol.length() > 0) {
            //sb.append("&title=").append(URLEncoder.encode(act.titol.replace('\'', '\u2019')));
            sb.append("&title=").append(Utilities.urlEncode(act.titol, true));
        }
        return sb.toString();
    }
    
    public String[][] getInfoCaixes() {
        ArrayList v = new ArrayList(4);
        v.add(new String[]{getTopicTitle(), enumItems(act.arees)});
        v.add(new String[]{getLevelTitle(), enumItems(act.nivells)});
        v.add(new String[]{getMsg("date"), dateFormat.format(act.data_creacio)});
        if (act.data_revisio != null && act.data_revisio.after(act.data_creacio)) {
            v.add(new String[]{getMsg("last_revision"), dateFormat.format(act.data_revisio)});
        }
        
        StringBuffer sb = new StringBuffer();
        if (act.flag1) {
            sb.append(getMsg("ctm"));
        }
        if (act.flag_nou) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(getMsg("nou"));
        }
        if (sb.length() > 0) {
            v.add(new String[]{getMsg("observacions"), sb.substring(0)});
        }
        
        return (String[][]) v.toArray(new String[v.size()][]);
    }
    
    public String[] getIdiomes() {
        String[] result = null;
        if (act != null && act.versions != null) {
            ArrayList v = new ArrayList(20);
            for (int i = 0; i < act.versions.length; i++) {
                WVersions wv = act.versions[i];
                if (wv.idiomes != null) {
                    for (int j = 0; j < wv.idiomes.length; j++) {
                        String tx = wv.idiomes[j].textIdioma;
                        if (!v.contains(tx)) {
                            v.add(tx);
                        }
                    }
                }
            }
            result = (String[]) v.toArray(new String[v.size()]);
        }
        return result;
    }
    
    public String getAutors() {
        StringBuffer sb = new StringBuffer();
        if (act.autors != null) {
            ArrayList v = new ArrayList(Arrays.asList(act.autors));                        
            
            for (int i = 0; i < act.versions.length; i++) {
                WVersions wv = act.versions[i];
                for (int j = 0; j < wv.autors.length; j++) {
                    v.add(wv.autors[j]);
                }
            }
            int l = v.size();
            if (l > 0) {
                int ic = 0;
                String rol = "aut";
                for (int i = 0; i < l; i++, ic++) {
                    Autor aut = (Autor) v.get(i);
                    if (!rol.equals(aut.getRolId())) {
                        sb.append(". ").append(aut.getRol()).append(": ");
                        ic = 0;
                        rol = aut.getRolId();
                    }
                    boolean first = (ic == 0);
                    boolean last = (i == l - 1) || !rol.equals(((Autor) v.get(i + 1)).getRolId());
                    if (last && !first) {
                        sb.append(" ").append(getMsg("and")).append(" ");
                    } else if (!first) {
                        sb.append(", ");
                    }
                    sb.append(aut.getUsuari().getNom());
                }
            }
        }
        return sb.substring(0);
    }
}
