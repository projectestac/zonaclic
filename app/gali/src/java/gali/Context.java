/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gali;

import edu.xtec.util.db.ConnectionBeanProvider;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author fbusquet
 */
public class Context extends java.util.Properties implements ServletContextListener {

    /*
    public static int DB_MAX_CONNS=3; // dbMaxConns
    public static boolean DB_LOG_APPEND=true; // dbLogAppend
    public static int DB_MAX_CHECKOUT_SECONDS=60; // dbMaxCheckoutSeconds
    public static String DB_LOG_FILE="galiDBConnection.log"; // dbLogFile
    public static int DB_DEBUG_LEVEL=2; // dbDebugLevel
    public static double DB_MAX_CONN_DAYS=1.0; // dbMaxConnDays
    public static int DB_MIN_CONNS=1; // dbMinConns
    public static String DB_DRIVER="JNDI"; // dbDriver
    public static String DB_SERVER="jdbc/pool/ClicConnectionPoolDS"; // dbServer
    public static String DB_CONTEXT=""; // dbContext
    public static boolean DB_MAP_STATEMENTS=true; // dbMapStatements
    public static String TABLE_PREFIX="EDU_JCLIC_"; // tablePrefix
    public static String CHECK_IDENT_URL="http://www.edu365.cat/pls/edu365/edu_sec_plsql_2.login"; // checkIdentUrl
    public static String REPORTER_NAME="TCPReporter"; // reporterName
    public static String REPORTER_PATH="clic.edu365.cat/reports"; // reporterPath
    public static String PACKAGE_URL="http://clic.edu365.cat/fgali/"; // packageUrl
    public static String PACKAGE_NOC_URL="http://clic.edu365.cat/fgalino/"; // packageNOCUrl
    public static String PACKAGE_ALG_URL="http://clic.edu365.cat/fgalial/"; // packageALGUrl
    public static String SERVLET_BASE_URL="http://clic.edu365.cat/gali/gali"; // servletBaseUrl
    public static String SERVER_DOMAIN="clic.edu365.cat"; // serverDomain
    public static final Properties PROPERTIES=new Properties();
    */

    private static final String DBCONF = "gali.properties";
    public static gali.Context cntx = null;
    private ConnectionBeanProvider db = null;


    public void contextInitialized(ServletContextEvent sce) {

      System.out.println("Initializing context Gali");

        if (cntx == null) {
            cntx = this;
        }

      //clear();

        javax.servlet.ServletContext sc = sce.getServletContext();
        Enumeration en = sc.getInitParameterNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            if (!contains(key)) {
                String value = (String) sc.getInitParameter(key);
                put(key, value);
            }
        }

        File f = new File(System.getProperty("user.home"), DBCONF);
        if (f.exists()) {
            try {
                FileInputStream is = new FileInputStream(f);
                load(is);
                is.close();
            } catch (Exception ex) {
                System.err.println("Error loading " + f.toString());
                System.err.println(ex);
            }
        }

        try {
            db = ConnectionBeanProvider.getConnectionBeanProvider(true, this);
        } catch (Exception ex) {
            System.err.println("GALI: Error creating ConnectionBeanProvider");
            //ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        //System.out.println("=========Destroying context db");
        if (db != null) {
            ConnectionBeanProvider.freeConnectionBeanProvider(db);
        }
        db = null;
        cntx = null;
    }

    public ConnectionBeanProvider getDB() throws Exception {
        if (db == null) {
            throw new Exception("Database connection not available!!");
        }
        return db;
    }

}