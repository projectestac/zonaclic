/*
 * WActivitatsDesc.java
 *
 * Created on 16 / gener / 2004, 10:08
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
public class WActivitatsDesc extends TableWrapper {

    public long id;
    public String idioma;
    public String descripcio;
    public String titol;
    public String titolEnc;
    public static final int ID = 0,
            IDIOMA = 1,
            DESCRIPCIO = 2,
            TITOL = 3,
            NUM_FIELDS = 4;
    public static final String[] FIELD_NAMES = {
        "ID",
        "IDIOMA",
        "DESCRIPCIO",
        "TITOL"
    };
    public static final String TABLE_NAME = "ACTIVITATS_DESC";
    public static final String PREFIX = "AD";
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
     * Creates a new instance of WActivitatsDesc
     */
    public WActivitatsDesc() {
    }

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String idioma_noEmprat) throws Exception {

        String[] fn = getPrefixedFieldNames();

        id = rs.getLong(fn[ID]);
        idioma = rs.getString(fn[IDIOMA]);
        descripcio = rs.getString(fn[DESCRIPCIO]);
        //titol=Utilities.xmlEncode(rs.getString(fn[TITOL]));        
        titol = rs.getString(fn[TITOL]);
        titolEnc = Utilities.xmlEncode(titol);
    }

    public void load(long actId, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM ACTIVITATS_DESC AD"
                + " WHERE AD.ID=? AND AD.IDIOMA=?";

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        stmt.setString(2, idioma);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid language (" + idioma + ") for activity " + actId);
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

    public static WActivitatsDesc[] loadManyFromAct(long actId, ConnectionBean con, String filtreIdioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields()
                + " FROM ACTIVITATS_DESC AD"
                + " WHERE AD.ID=?";
        if (filtreIdioma != null) {
            sql = sql + " AND AD.IDIOMA=?";
        }
        sql = sql + " ORDER BY AD.IDIOMA";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        if (filtreIdioma != null) {
            stmt.setString(2, filtreIdioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WActivitatsDesc wad = new WActivitatsDesc();
                wad.load(rs, con, null);
                v.add(wad);
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

        return (WActivitatsDesc[]) v.toArray(new WActivitatsDesc[v.size()]);
    }
}
