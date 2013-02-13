/*
 * WDescriptorsAct.java
 *
 * Created on 18 / novembre / 2003, 15:26
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
public class WDescriptorsAct extends TableWrapper {

    public long activitat;
    public long descriptor;
    public String textDescriptor;
    public static final int ACTIVITAT = 0,
            DESCRIPTOR = 1,
            NUM_FIELDS = 2;
    public static final String[] FIELD_NAMES = {
        "ACTIVITAT",
        "DESCRIPTOR",};
    public static final String TABLE_NAME = "DESCRIPTORS_ACT";
    public static final String PREFIX = "DA";

    /**
     * Creates a new instance of WDescriptorsAct
     */
    public WDescriptorsAct() {
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

        activitat = rs.getLong(fn[ACTIVITAT]);
        descriptor = rs.getLong(fn[DESCRIPTOR]);
        if (idioma != null) {
            textDescriptor = WDiccionariDescriptors.getText(rs);
        }
    }

    public static WDescriptorsAct[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);

        String sql =
                "SELECT " + getSelectFields();
        if (idioma != null) {
            sql = sql + ", " + WDiccionariDescriptors.getSelectTextField()
                    + " FROM DESCRIPTORS_ACT DA, DICCIONARI_DESCRIPTORS DD "
                    + " WHERE DA.ACTIVITAT=? AND DD.IDIOMA=?"
                    + " AND DD.CODI=DA.DESCRIPTOR";
        } else {
            sql = sql
                    + " FROM DESCRIPTORS_ACT DA"
                    + " WHERE DA.ACTIVITAT=?";
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
                WDescriptorsAct wda = new WDescriptorsAct();
                wda.load(rs, con, idioma);
                v.add(wda);
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

        return (WDescriptorsAct[]) v.toArray(new WDescriptorsAct[v.size()]);
    }

    public String getMainText() {
        return textDescriptor != null ? textDescriptor : ("DESC" + descriptor);
    }
}
