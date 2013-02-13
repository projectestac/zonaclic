/*
 * WCentresAct.java
 *
 * Created on 18 / novembre / 2003, 16:04
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
public class WCentresAct extends TableWrapper {

    public String centreId;
    public long activitat;
    public int nivell;
    public WCentres centre;
    public static final int CENTRE = 0,
            ACTIVITAT = 1,
            NIVELL = 2,
            NUM_FIELDS = 4;
    public static final String[] FIELD_NAMES = {
        "CENTRE",
        "ACTIVITAT",
        "NIVELL"
    };
    public static final String TABLE_NAME = "CENTRES_ACT";
    public static final String PREFIX = "CA";

    /**
     * Creates a new instance of WCentresAct
     */
    public WCentresAct() {
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
        centreId = rs.getString(fn[CENTRE]);
        activitat = rs.getLong(fn[ACTIVITAT]);
        nivell = rs.getInt(fn[NIVELL]);

        centre = new WCentres();
        centre.load(rs, con, idioma);
    }

    public static WCentresAct[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields() + ", " + WCentres.getSelectFields()
                + " FROM CENTRES_ACT CA, " + WCentres.TNP
                + " WHERE CA.ACTIVITAT=? "
                + " AND C.ID=CA.CENTRE AND CM.ID(+)=C.COMUNITAT"
                + " ORDER BY CA.NIVELL, C.NOM";

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WCentresAct wca = new WCentresAct();
                wca.load(rs, con, idioma);
                v.add(wca);
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

        return (WCentresAct[]) v.toArray(new WCentresAct[v.size()]);
    }
}
