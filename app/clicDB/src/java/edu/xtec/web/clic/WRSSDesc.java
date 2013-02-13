/*
 * WRSSDesc.java
 *
 * Created on 7 / desembre / 2004, 13:12
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author fbusquet
 */
public class WRSSDesc extends TableWrapper {

    public long id;
    public String language;
    public String title;
    public String link;
    public String description;
    public boolean valid;
    public static final int ID = 0,
            LANGUAGE = 1,
            TITLE = 2,
            LINK = 3,
            DESCRIPTION = 4,
            VALID = 5,
            NUM_FIELDS = 6;
    public static final String[] FIELD_NAMES = {
        "ID",
        "LANGUAGE",
        "TITLE",
        "LINK",
        "DESCRIPTION",
        "ES_PUBLIC",};
    public static final String TABLE_NAME = "RSS_DESCRIPCIONS";
    public static final String PREFIX = "RSSD";
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

    /**
     * Creates a new instance of WRSSDesc
     */
    public WRSSDesc() {
    }

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String language_noEmprat) throws Exception {

        String[] fn = getPrefixedFieldNames();

        id = rs.getLong(fn[ID]);
        language = rs.getString(fn[LANGUAGE]);
        title = Utilities.xmlEncode(rs.getString(fn[TITLE]));
        link = Utilities.xmlEncode(rs.getString(fn[LINK]));
        description = rs.getString(fn[DESCRIPTION]);
        valid = getBoolean(rs, fn[VALID]);

    }

    public void load(long rssId, ConnectionBean con, String language) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM RSS_DESCRIPCIONS RSSD"
                + " WHERE RSSD.ID=? AND RSSD.LANGUAGE=?";

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, rssId);
        stmt.setString(2, language);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid language (" + language + ") for RSS " + rssId);
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

    public static WRSSDesc[] loadManyFromRss(long rssId, ConnectionBean con, String filtreIdioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields()
                + " FROM RSS_DESCRIPCIONS RSSD"
                + " WHERE RSSD.ID=?"
                + " AND RSSD.ES_PUBLIC=?";
        if (filtreIdioma != null) {
            sql = sql + " AND RSSD.LANGUAGE=?";
        }
        sql = sql + " ORDER BY RSSD.LANGUAGE";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, rssId);
        stmt.setString(2, "s");
        if (filtreIdioma != null) {
            stmt.setString(3, filtreIdioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WRSSDesc wrssd = new WRSSDesc();
                wrssd.load(rs, con, null);
                v.add(wrssd);
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

        return (WRSSDesc[]) v.toArray(new WRSSDesc[v.size()]);
    }

    public String getFilteredDescription() {
        return Utilities.xmlEncode(description);
    }
}
