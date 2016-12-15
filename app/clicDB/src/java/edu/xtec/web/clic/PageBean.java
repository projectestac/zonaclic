/*
 * PageBean.java
 *
 * Created on 30 de junio de 2003, 11:41
 */
package edu.xtec.web.clic;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author fbusquet
 */
public abstract class PageBean {

  // Par�metres
  public static final String LANG = "lang";
  public static final String CHECKING = "checking";
  // constants
  public static final String TRUE = "s";
  public static final int CA = 0, ES = 1, EN = 2, NUM_LANGS = 3;
  public static final String[] LANGS = {"ca", "es", "en"};
  // static objects
  private static final HashMap BUNDLES = new HashMap();
  private static final DateFormat[] DATE_FORMATS = new DateFormat[NUM_LANGS];
  private static final NumberFormat[] NUMBER_FORMATS = new NumberFormat[NUM_LANGS];
  // Members
  protected int errCode;
  protected String errDescription;
  public String lang = LANGS[EN];
  public String requestedLang = lang;
  public ResourceBundle bundle;
  public DateFormat dateFormat;
  public NumberFormat numberFormat;
  public boolean checking;

  /**
   * Creates a new instance of PageBean
   */
  public PageBean() {
  }

  //private static edu.xtec.util.db.ConnectionBeanProvider db;
  private static edu.xtec.util.db.ConnectionBeanProvider getDB() throws Exception {
    return Context.cntx.getDB();
    /*
         * if(db==null){
         * db=edu.xtec.util.db.ConnectionBeanProvider.getConnectionBeanProvider(true,
         * Context.cntx); } return db;
     */
  }

  public final boolean init(HttpServletRequest request, HttpServletResponse response, String lang) {
    boolean result = false;
    edu.xtec.util.db.ConnectionBean con = null;
    this.lang = lang;
    try {
      con = getDB().getConnectionBean();
      if (con == null) {
        errCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        errDescription = "Unable to connect to database!";
      } else {
        getRequestParams(request);
        process(con);
        result = true;
      }
    } catch (Exception ex) {
      if (errCode == 0) {
        errCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
      }
      if (errDescription == null) {
        errDescription = ex.getMessage();
      }
      System.err.println("ERROR: " + errDescription);
      if (request != null) {
        request.setAttribute("ERROR", errDescription);
        ex.printStackTrace(System.err);
      }
    } finally {
      if (con != null) {
        try {
          getDB().freeConnectionBean(con);
        } catch (Exception ex) {
          System.err.println("ClicDB: Error - Unable to access ConnectionBeanProvider!");
        }
      }
    }
    return result;
  }

  protected void getRequestParams(HttpServletRequest request) throws Exception {

    String chk = request.getParameter(CHECKING);
    checking = chk != null && chk.equals("true");

    if (lang == null) {
      lang = request.getParameter(LANG);
    }
    initBundles(lang);
  }

  protected void initBundles(String pLang) throws Exception {

    lang = pLang;
    int bi = EN;
    for (int i = 0; i < NUM_LANGS; i++) {
      if (LANGS[i].equals(lang)) {
        bi = i;
        break;
      }
    }
    requestedLang = lang;
    lang = LANGS[bi];
    String kl = getClass().getName();
    ResourceBundle[] rb = (ResourceBundle[]) BUNDLES.get(kl);
    if (rb == null) {
      rb = new ResourceBundle[NUM_LANGS];
      BUNDLES.put(kl, rb);
    }

    bundle = rb[bi];
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(getMainBundle(), new Locale(LANGS[bi], ""));
      rb[bi] = bundle;
    }
    dateFormat = DATE_FORMATS[bi];
    if (dateFormat == null) {
      dateFormat = new SimpleDateFormat("dd/MM/yy", new Locale(LANGS[bi], ""));
      DATE_FORMATS[bi] = dateFormat;
    }
    numberFormat = NUMBER_FORMATS[bi];
    if (numberFormat == null) {
      numberFormat = NumberFormat.getInstance(new Locale(LANGS[bi], ""));
      NUMBER_FORMATS[bi] = numberFormat;
    }
  }

  public String getMsg(String key) {
    String result;
    try {
      result = bundle.getString(key);
    } catch (Exception mre) {
      result = "Missing resource text: " + key;
    }
    return result;
  }

  static public String str(String text) {
    return text == null ? "" : text;
  }

  static public String em(String m) {
    StringBuffer result = new StringBuffer();
    if (m != null) {
      for (int i = m.length() - 1; i >= 0; i--) {
        char ch = m.charAt(i);
        switch(ch){
          case '@':
            result.append("-at-");
            break;
          case '.':
            result.append("-dot-");
            break;
          default:
            result.append(ch);
        }
      }
    }
    return result.substring(0);
  }

  public String getServerBase() {
    return Context.getServerBase();
  }

  public String getAppBase() {
    return Context.getAppBase();
  }

  // Abstract methods:
  protected abstract String getMainBundle();

  protected abstract void process(edu.xtec.util.db.ConnectionBean con) throws Exception;
}
