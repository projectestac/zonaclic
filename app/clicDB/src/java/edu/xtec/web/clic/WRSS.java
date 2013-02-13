/*
 * WRSS.java
 *
 * Created on 7 / desembre / 2004, 13:27
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author fbusquet
 */
public class WRSS extends TableWrapper {

    public long id;
    public Date data_pub;
    public boolean valid;
    public String descripcio;
    public boolean aPortada;
    public String imatge;
    public WRSSDesc[] descripcions;
    public static final int ID = 0,
            DATA_PUB = 1,
            VALID = 2,
            DESCRIPCIO = 3,
            A_PORTADA = 4,
            IMATGE = 5,
            NUM_FIELDS = 6;
    public static final String[] FIELD_NAMES = {
        "ID",
        "DATA_PUB",
        "ES_PUBLIC",
        "DESCRIPCIO",
        "A_PORTADA",
        "IMATGE"
    };
    public static final String TABLE_NAME = "RSS_NOTICIES";
    public static final String PREFIX = "RSS";

    /**
     * Creates a new instance of WRSS
     */
    public WRSS() {
    }
    public static final String SELECT_TABLES = TABLE_NAME + " " + PREFIX;
    private static String SELECT_FIELDS;

    public static String getSelectFields() {
        if (SELECT_FIELDS == null) {
            SELECT_FIELDS = buildSelectFields(FIELD_NAMES, getPrefixedFieldNames(), PREFIX);
        }
        return SELECT_FIELDS;
    }
    private static String[] PREFIXED_FIELDS;

    public static String[] getPrefixedFieldNames() {
        if (PREFIXED_FIELDS == null) {
            PREFIXED_FIELDS = buildPrefixedFieldNames(FIELD_NAMES, PREFIX);
        }
        return PREFIXED_FIELDS;
    }

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String language) throws Exception {
        String[] fn = getPrefixedFieldNames();

        id = rs.getLong(fn[ID]);
        data_pub = rs.getTimestamp(fn[DATA_PUB]);
        valid = getBoolean(rs, fn[VALID]);
        descripcio = rs.getString(fn[DESCRIPCIO]);
        aPortada = getBoolean(rs, fn[A_PORTADA]);
        imatge = rs.getString(fn[IMATGE]);

        if (con != null) {
            descripcions = WRSSDesc.loadManyFromRss(id, con, language);
        }
    }

    public void load(long id, ConnectionBean con, String language) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM RSS_NOTICIES RSS"
                + " WHERE RSS.ID=?";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next() || !PageBean.TRUE.equals(rs.getString("RSS_ES_PUBLIC"))) {
                ex = new Exception("Invalid RSS ID: " + id);
            } else {
                load(rs, con, language);
            }
        } catch (Exception ex2) {
            ex = ex2;
        } finally {
            rs.close();
            con.closeStatement(stmt);
        }
        if (ex != null) {
            throw ex;
        }
    }

    public void loadFromAct(long id, ConnectionBean con, String language) throws Exception {
        ActBean actBean = new ActBean();
        actBean.initBundles(language);
        actBean.id = id;
        actBean.process(con);
        //WActivitats wact=new WActivitats();
        //wact.load(id, con, language);
        this.id = id;
        data_pub = actBean.act.data_revisio;
        valid = actBean.act.valid;
        if (actBean.act.imatge != null && actBean.act.imatge.length() > 0) {
            imatge = Context.cntx.getProperty("serverBase") + "/thumbs/" + actBean.act.imatge;
            imatge = imatge.substring(0, imatge.lastIndexOf('.')) + ".png";
        }
        WActivitatsDesc wactdesc = actBean.act.desc != null ? actBean.act.desc[0] : null;
        if (wactdesc != null) {
            descripcions = new WRSSDesc[1];
            descripcions[0] = new WRSSDesc();
            StringBuffer sb = new StringBuffer(wactdesc.descripcio);

            sb.append("<p>");
            String autors = actBean.getAutors();
            if (autors != null && autors.length() > 0) {
                sb.append("<strong>").append(actBean.getMsg(actBean.act.autors.length == 1 ? "author" : "authors")).append(":</strong> ");
                sb.append(autors).append("<br>");
            }
            String[][] caixes = actBean.infoCaixes;
            if (caixes != null) {
                for (int i = 0; i < caixes.length; i++) {
                    sb.append("<strong>").append(caixes[i][0]).append(":</strong> ").append(caixes[i][1]).append("<br>");
                }
            }

            String[] idiomes = actBean.getIdiomes();
            if (idiomes != null && idiomes.length > 0) {
                sb.append("<strong>").append(actBean.getMsg(idiomes.length == 1 ? "language" : "languages")).append(":</strong> ");
                for (int i = 0; i < idiomes.length; i++) {
                    sb.append(i == 0 ? "" : ", ").append(idiomes[i]);
                }
                sb.append("<br>");
            }
            sb.append("</p>");

            descripcions[0].description = sb.substring(0);


            descripcions[0].valid = true;
            descripcions[0].language = language;
            descripcions[0].title = wactdesc.titol;
            descripcions[0].link = Context.cntx.getProperty("appBase") + "/act_" + language + ".jsp?id=" + id;

            descripcio = wactdesc.descripcio;
            aPortada = true;
        }
    }

    public static WRSS[] loadMany(ConnectionBean con, String language, int maxRegs, boolean aPortada) throws Exception {
        ArrayList v = new ArrayList(Math.max(5, maxRegs));
        String sql =
                "select * from"
                + " ((select ID, 'act' as FLAG, DATA_REVISIO as DATA from ACTIVITATS where VALID=?)"
                + " union all (select ID, 'rss' as FLAG, DATA_PUB as DATA from RSS_NOTICIES where ES_PUBLIC=?";
        if (aPortada) {
            sql = sql + " and A_PORTADA=?";
        }
        sql = sql + " ))"
                + " order by DATA desc";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setString(1, "s");
        stmt.setString(2, "s");
        if (aPortada) {
            stmt.setString(3, "s");
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        int counter = 0;
        try {
            while (rs.next() && counter < maxRegs) {
                String rsFlag = rs.getString("FLAG");
                long rsId = rs.getLong("ID");
                WRSS wrss = new WRSS();
                if ("rss".equals(rsFlag)) {
                    wrss.load(rsId, con, language);
                } else if ("act".equals(rsFlag)) {
                    wrss.loadFromAct(rsId, con, language);
                }
                if (wrss.descripcions != null && wrss.descripcions.length > 0) {
                    v.add(wrss);
                    counter++;
                }
            }
        } catch (Exception ex2) {
            ex = ex2;
        } finally {
            rs.close();
            con.closeStatement(stmt);
        }
        if (ex != null) {
            throw ex;
        }

        return (WRSS[]) v.toArray(new WRSS[v.size()]);
    }

    public String getFilteredImageTag() {
        String result = "";
        if (imatge != null && imatge.length() > 0) {
            result = Utilities.xmlEncode("<div id=\"thumb\"><img src=\"" + imatge + "\"></div>");
        }
        return result;
    }
}
