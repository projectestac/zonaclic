/*
 * WActivitatsArees.java
 *
 * Created on 18 / novembre / 2003, 13:49
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
public class WActivitatsArees extends TableWrapper {

    public long id;
    public String area;
    public String textArea;
    public static final int ID = 0,
            AREA = 1,
            NUM_FIELDS = 2;
    public static final String[] FIELD_NAMES = {
        "ID",
        "AREA",};
    public static final String TABLE_NAME = "ACTIVITATS_AREES";
    public static final String PREFIX = "AAR";

    /**
     * Creates a new instance of WActivitatsArees
     */
    public WActivitatsArees() {
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
        area = rs.getString(fn[AREA]);
        if (idioma != null) {
            textArea = WDiccionari.getText(rs);
        }

    }

    public static WActivitatsArees[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {
        
        ArrayList v = new ArrayList(3);
        
        String sql =
                "SELECT " + getSelectFields();

        if (idioma != null) {
            sql = sql + ", " + WDiccionari.getSelectTextField()
                    + " FROM ACTIVITATS_AREES AAR, AREES AR, DICCIONARI D "
                    + " WHERE AAR.ID=? AND D.IDIOMA=?"
                    + " AND D.CODI=AR.CODI_DIC AND AR.ID=AAR.AREA";
        } else {
            sql = sql
                    + " FROM ACTIVITATS_AREES AAR"
                    + " WHERE AAR.ID=?";
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
                WActivitatsArees waa = new WActivitatsArees();
                waa.load(rs, con, idioma);
                v.add(waa);
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

        return (WActivitatsArees[]) v.toArray(new WActivitatsArees[v.size()]);
    }

    //@Override
    public String getMainText() {
        return textArea != null ? textArea : area;
    }
}
