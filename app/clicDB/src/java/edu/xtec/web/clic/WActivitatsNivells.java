/*
 * WActivitatsNivells.java
 *
 * Created on 18 / novembre / 2003, 14:03
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
public class WActivitatsNivells extends TableWrapper {

    public long id;
    public String nivell;
    public String textNivell;
    public static final int ID = 0,
            NIVELL = 1,
            NUM_FIELDS = 2;
    public static final String[] FIELD_NAMES = {
        "ID",
        "NIVELL",};
    public static final String TABLE_NAME = "ACTIVITATS_NIVELLS";
    public static final String PREFIX = "ANI";

    /**
     * Creates a new instance of WActivitatsNivells
     */
    public WActivitatsNivells() {
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
        nivell = rs.getString(fn[NIVELL]);
        if (idioma != null) {
            textNivell = WDiccionari.getText(rs);
        }

    }

    public static WActivitatsNivells[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields();
        if (idioma != null) {
            sql = sql + ", " + WDiccionari.getSelectTextField()
                    + " FROM ACTIVITATS_NIVELLS ANI, NIVELLS NI, DICCIONARI D "
                    + " WHERE ANI.ID=? AND D.IDIOMA=?"
                    + " AND D.CODI=NI.CODI_DIC AND NI.ID=ANI.NIVELL";
        } else {
            sql = sql
                    + " FROM ACTIVITATS_NIVELLS ANI"
                    + " WHERE ANI.ID=?";
        }

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        if (idioma != null) {
            stmt.setString(2, idioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WActivitatsNivells wan = new WActivitatsNivells();
                wan.load(rs, con, idioma);
                v.add(wan);
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

        return (WActivitatsNivells[]) v.toArray(new WActivitatsNivells[v.size()]);
    }

    //@Override
    public String getMainText() {
        return textNivell != null ? textNivell : nivell;
    }
}
