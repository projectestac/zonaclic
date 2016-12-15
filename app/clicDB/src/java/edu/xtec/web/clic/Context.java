/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.web.clic;

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
    * public static int DB_MAX_CONNS=3; // dbMaxConns public static boolean
    * DB_LOG_APPEND=true; // dbLogAppend public static int
    * DB_MAX_CHECKOUT_SECONDS=60; // dbMaxCheckoutSeconds public static String
    * DB_LOG_FILE="webClicDBConnection.log"; // dbLogFile public static int
    * DB_DEBUG_LEVEL=2; // dbDebugLevel public static double
    * DB_MAX_CONN_DAYS=1.0; // dbMaxConnDays public static int DB_MIN_CONNS=1;
    * // dbMinConns public static String DB_DRIVER="JNDI"; // dbDriver public
    * static String DB_SERVER="jdbc/pool/ClicConnectionPoolDS"; // dbServer
    * public static String DB_CONTEXT=""; // dbContext public static boolean
    * DB_MAP_STATEMENTS=false; // dbMapStatements public static String
    * SERVER_BASE="http://clic.xtec.cat"; // serverBase public static String
    * APP_BASE="http://clic.xtec.cat/db"; // appBase public static final
    * Properties PROPERTIES=new Properties();
    */

    private static final String DBCONF = "webClic.properties";
    public static edu.xtec.web.clic.Context cntx = null;
    private ConnectionBeanProvider db = null;

    public static final String DEFAULT_SERVER_BASE = "http://clic.xtec.cat";
    public static final String DEFAULT_APP_BASE = "http://clic.xtec.cat/db";
    public static final String DEFAULT_FILE_BASE = "/tmp";
    private static File fileBase = null;

    public void contextInitialized(ServletContextEvent sce) {

      System.out.println("=========Initializing context db");

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
            System.err.println("Error creating ConnectionBeanProvider");
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

    public static String getServerBase() {
        return cntx == null ? DEFAULT_SERVER_BASE : cntx.getProperty("serverBase", DEFAULT_SERVER_BASE);
    }

    public static String getAppBase() {
        return cntx == null ? DEFAULT_APP_BASE : cntx.getProperty("appBase", DEFAULT_APP_BASE);
    }
    
    public static File getStaticFileBase(){
      if(fileBase == null)
        fileBase = new File(cntx == null ? DEFAULT_FILE_BASE : cntx.getProperty("staticFilesPath", DEFAULT_FILE_BASE));      
      return fileBase;
    }
        
}