/*
 * WDiccionari.java
 *
 * Created on 18 / novembre / 2003, 12:20
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;

/**
 *
 * @author fbusquet
 */
public class WDiccionari extends TableWrapper {

    public String codi;
    public String idioma;
    public String text;
    public static final int CODI = 0,
            IDIOMA = 1,
            TEXT = 2,
            NUM_FIELDS = 3;
    public static final String[] FIELD_NAMES = {
        "CODI",
        "IDIOMA",
        "TEXT"
    };
    public static final String TABLE_NAME = "DICCIONARI";
    public static final String PREFIX = "D";
    public static final String TNP = TABLE_NAME + " " + PREFIX;

    /**
     * Creates a new instance of WDiccionari
     */
    public WDiccionari() {
    }
    private static String SELECT_FIELDS;

    public static String getSelectFields() {
        if (SELECT_FIELDS == null) {
            SELECT_FIELDS = buildSelectFields(FIELD_NAMES, getPrefixedFieldNames(), PREFIX);
        }
        return SELECT_FIELDS;
    }
    private static String SELECT_TEXT_FIELD;

    public static String getSelectTextField() {
        if (SELECT_TEXT_FIELD == null) {
            String[] st = new String[]{FIELD_NAMES[TEXT]};
            SELECT_TEXT_FIELD = buildSelectFields(st, buildPrefixedFieldNames(st, PREFIX), PREFIX);
        }
        return SELECT_TEXT_FIELD;
    }
    private static String[] PREFIXED_FIELDS;

    public static String[] getPrefixedFieldNames() {
        if (PREFIXED_FIELDS == null) {
            PREFIXED_FIELDS = buildPrefixedFieldNames(FIELD_NAMES, PREFIX);
        }
        return PREFIXED_FIELDS;
    }

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String idioma_dummy) throws Exception {

        String[] fn = getPrefixedFieldNames();

        codi = rs.getString(fn[CODI]);
        idioma = rs.getString(fn[IDIOMA]);
        text = rs.getString(fn[TEXT]);
    }

    public void load(String codi, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM " + TABLE_NAME + " " + PREFIX
                + " WHERE " + PREFIX + ".CODI=? AND " + PREFIX + ".IDIOMA=?";
        java.sql.PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setString(1, codi);
        stmt.setString(2, idioma);
        java.sql.ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid code: " + codi);
            } else {
                load(rs, con, idioma);
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
    private static WDiccionari WD;

    public static String getText(String codi, ConnectionBean con, String idioma) throws Exception {
        if (WD == null) {
            WD = new WDiccionari();
        }
        WD.load(codi, con, idioma);
        return WD.text;
    }

    public static String getText(java.sql.ResultSet rs) throws Exception {
        return rs.getString(getPrefixedFieldNames()[TEXT]);
    }
}
