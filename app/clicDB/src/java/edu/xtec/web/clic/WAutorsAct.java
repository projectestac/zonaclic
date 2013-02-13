/*
 * WAutorsAct.java
 *
 * Created on 17 / novembre / 2003, 15:03
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
public class WAutorsAct extends TableWrapper implements Autor {

    public long autorId;
    public long activitatId;
    public String rolId;
    public java.sql.Date data;
    public String rol;
    public WUsuaris autor;
    public static final int AUTOR = 0,
            ACTIVITAT = 1,
            DATA = 2,
            ROL = 3,
            NUM_FIELDS = 4;
    public static final String[] FIELD_NAMES = {
        "AUTOR",
        "ACTIVITAT",
        "DATA",
        "ROL"
    };
    public static final String TABLE_NAME = "AUTORS_ACT";
    public static final String PREFIX = "AA";
    public static final String TNP = TABLE_NAME + " " + PREFIX;

    /**
     * Creates a new instance of WAutorsAct
     */
    public WAutorsAct() {
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
        autorId = rs.getLong(fn[AUTOR]);
        activitatId = rs.getLong(fn[ACTIVITAT]);
        data = rs.getDate(fn[DATA]);

        rolId = rs.getString(fn[ROL]);
        if (idioma != null) {
            rol = WDiccionari.getText(rs);
        }

        autor = new WUsuaris();
        autor.load(rs, con, idioma);
    }

    public void load(long actId, long usrId, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields() + ", " + WUsuaris.getSelectFields();
        if (idioma != null) {
            sql = sql
                    + ", " + WDiccionari.getSelectTextField()
                    + " FROM AUTORS_ACT AA, USUARIS U, ROLS R, DICCIONARI D"
                    + " WHERE AA.ACTIVITAT=? AND AA.AUTOR=? AND D.IDIOMA=?"
                    + " AND U.ID=AA.AUTOR AND R.ID=AA.ROL AND D.CODI=R.CODI_DIC";
        } else {
            sql = sql
                    + " FROM AUTORS_ACT AA, USUARIS U"
                    + " WHERE AA.ACTIVITAT=? AND AA.AUTOR=?"
                    + " AND U.ID=AA.AUTOR";
        }

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        stmt.setLong(2, usrId);
        if (idioma != null) {
            stmt.setString(3, idioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid UserID (" + usrId + ") for activity " + actId);
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

    public static WAutorsAct[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {

        ArrayList v = new ArrayList(3);

        String sql =
                "SELECT " + getSelectFields() + ", " + WUsuaris.getSelectFields();

        if (idioma != null) {
            sql = sql
                    + ", " + WDiccionari.getSelectTextField()
                    + " FROM AUTORS_ACT AA, USUARIS U, ROLS R, DICCIONARI D"
                    + " WHERE AA.ACTIVITAT=? AND D.IDIOMA=?"
                    + " AND U.ID=AA.AUTOR AND D.CODI=R.CODI_DIC AND R.ID=AA.ROL";
        } else {
            sql = sql
                    + " FROM AUTORS_ACT AA, USUARIS U"
                    + " WHERE AA.ACTIVITAT=?"
                    + " AND U.ID=AA.AUTOR";
        }

        sql = sql + " ORDER BY AA.ROL, U.NOM";

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        if (idioma != null) {
            stmt.setString(2, idioma);
        }
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WAutorsAct waa = new WAutorsAct();
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

        return (WAutorsAct[]) v.toArray(new WAutorsAct[v.size()]);
    }

    public String getRol() {
        return rol;
    }

    public String getRolId() {
        return rolId;
    }

    public WUsuaris getUsuari() {
        return autor;
    }
}
