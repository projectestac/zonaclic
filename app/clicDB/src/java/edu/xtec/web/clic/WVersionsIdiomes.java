/*
 * WVersionsIdiomes.java
 *
 * Created on 18 / novembre / 2003, 17:33
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
public class WVersionsIdiomes extends TableWrapper {

    public long id;
    public String codiIdioma;
    public String textIdioma;
    public static final int ID = 0,
            IDIOMA = 1,
            NUM_FIELDS = 2;
    public static final String[] FIELD_NAMES = {
        "ID",
        "IDIOMA"
    };
    public static final String TABLE_NAME = "VERSIONS_IDIOMES";
    public static final String PREFIX = "VI";

    /**
     * Creates a new instance of WVersionsIdiomes
     */
    public WVersionsIdiomes() {
    }
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

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String idioma) throws Exception {

        String[] fn = getPrefixedFieldNames();

        id = rs.getLong(fn[ID]);
        codiIdioma = rs.getString(fn[IDIOMA]);

        if (idioma != null) {
            textIdioma = WDiccionari.getText(rs);
        }

    }

    public static WVersionsIdiomes[] loadManyFromVer(long verId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields();
        if (idioma != null) {
            sql = sql + ", " + WDiccionari.getSelectTextField()
                    + " FROM VERSIONS_IDIOMES VI, IDIOMES I, DICCIONARI D "
                    + " WHERE VI.ID=? AND D.IDIOMA=?"
                    + " AND D.CODI=I.CODI_DIC AND I.ID=VI.IDIOMA";
        } else {
            sql = sql
                    + " FROM VERSIONS_IDIOMES VI"
                    + " WHERE VI.ID=?";
        }

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, verId);
        if (idioma != null) {
            stmt.setString(2, idioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WVersionsIdiomes wvi = new WVersionsIdiomes();
                wvi.load(rs, con, idioma);
                v.add(wvi);
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

        return (WVersionsIdiomes[]) v.toArray(new WVersionsIdiomes[v.size()]);
    }

    @Override
    public String getMainText() {
        return textIdioma != null ? textIdioma : codiIdioma;
    }
}
