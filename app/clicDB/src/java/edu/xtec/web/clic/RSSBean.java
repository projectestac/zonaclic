/*
 * RSSBean.java
 *
 * Created on 7 / desembre / 2004, 13:51
 */
package edu.xtec.web.clic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public class RSSBean extends PageBean {

    public WRSS[] rss;
    public boolean aPortada;
    public Date lastDate;
    public static DateFormat ISODateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+01:00'");
    public static final int NUM_ITEMS = 20;

    /**
     * Creates a new instance of RSSBean
     */
    public RSSBean() {
        super();
    }

    protected String getMainBundle() {
        return "edu.xtec.resources.messages.rssMessages";
    }

    @Override
    protected void getRequestParams(HttpServletRequest request) throws Exception {
        if (lang == null) {
            String uri = request.getRequestURI();
            int p = uri.lastIndexOf('_');
            if (p >= 0 && uri.length() > p + 2) {
                lang = uri.substring(p + 1, p + 3);
            }
        }
        super.getRequestParams(request);
    }

    protected void process(edu.xtec.util.db.ConnectionBean con) throws Exception {
        rss = WRSS.loadMany(con, lang, NUM_ITEMS, aPortada);
    }

    public Date getLastDate() {
        if (lastDate == null && rss != null && rss.length > 0) {
            lastDate = new Date(0);
            for (int i = 0; i < rss.length; i++) {
                if (rss[i].data_pub.after(lastDate)) {
                    lastDate = rss[i].data_pub;
                }
            }
        }
        return lastDate;
    }
}
