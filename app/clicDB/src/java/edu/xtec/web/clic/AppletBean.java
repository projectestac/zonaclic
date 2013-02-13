/*
 * AppletBean.java
 *
 * Created on 3 de julio de 2003, 17:58
 */
package edu.xtec.web.clic;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public class AppletBean extends PageBean {

    public static final String DEFAULT_REPORTER_CLASS = "TCPReporter";
    public static final String PROJECT = "project";
    public static final String TITLE = "title", SKIN = "skin", REPORTER = "rep", REPORTER_CLASS = "reporterClass";
    public static final String EXIT_URL = "exitUrl", INFO_URL_FRAME = "infoUrlFrame";
    public static final String SEQUENCE = "sequence", SYSTEM_SOUNDS = "systemSounds";
    public static final String COMPRESS_IMAGES = "compressImages";
    public String project;
    public String title;
    public String skin;
    public String reporter;
    public String reporterClass;
    public String exitUrl;
    public String infoUrlFrame;
    public String sequence;
    public String systemSounds;
    public String compressImages;

    /**
     * Creates a new instance of AppletBean
     */
    public AppletBean() {
        super();
    }
    private static String codeBase;

    public String getCodeBase() {
        if (codeBase == null) {
            codeBase = Context.cntx.getProperty("serverBase") + "/dist/jclic";
        }
        return codeBase;
    }

    protected String getMainBundle() {
        return "edu.xtec.resources.messages.listMessages";
    }

    protected void getRequestParams(HttpServletRequest request) throws Exception {
        super.getRequestParams(request);

        project = getParam(request, PROJECT, "");
        title = getParam(request, TITLE, "");
        if (title.length() > 0) {
            title = ": " + title;
        }
        skin = getParam(request, SKIN, null);
        reporter = getParam(request, REPORTER, null);
        if (reporter != null) {
            reporterClass = getParam(request, REPORTER, DEFAULT_REPORTER_CLASS);
        }
        exitUrl = getParam(request, EXIT_URL, null);
        infoUrlFrame = getParam(request, INFO_URL_FRAME, null);
        sequence = getParam(request, SEQUENCE, null);
        systemSounds = getParam(request, SYSTEM_SOUNDS, null);
        compressImages = getParam(request, COMPRESS_IMAGES, null);

    }

    protected void process(edu.xtec.util.db.ConnectionBean con) throws Exception {
    }
}
