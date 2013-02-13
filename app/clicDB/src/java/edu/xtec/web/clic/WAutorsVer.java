/*
 * WAutorsVer.java
 *
 * Created on 18 / novembre / 2003, 16:59
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
public class WAutorsVer extends TableWrapper implements Autor {

    public long autorId;
    public long versioId;
    public String rolId;
    public String rol;
    public java.sql.Date data;
    public WUsuaris autor;
    public static final int AUTOR = 0,
            VERSIO = 1,
            DATA = 2,
            ROL = 3,
            NUM_FIELDS = 4;
    public static final String[] FIELD_NAMES = {
        "AUTOR",
        "VERSIO",
        "DATA",
        "ROL"
    };
    public static final String TABLE_NAME = "AUTORS_VER";
    public static final String PREFIX = "AV";

    /**
     * Creates a new instance of WAutorsVer
     */
    public WAutorsVer() {
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
        versioId = rs.getLong(fn[VERSIO]);
        data = rs.getDate(fn[DATA]);

        rolId = rs.getString(fn[ROL]);
        rol = WDiccionari.getText(rs);

        autor = new WUsuaris();
        autor.load(rs, con, idioma);

    }

    public void load(long verId, long usrId, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields() + ", " + WDiccionari.getSelectTextField()
                + " FROM AUTORS_VER AV, USUARIS U, ROLS R, DICCIONARI D "
                + " WHERE AV.VERSIO=? AND AV.AUTOR=? AND D.IDIOMA=?"
                + " AND U.ID=AV.AUTOR AND D.CODI=R.CODI_DIC AND R.ID=AV.ROL";

        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, verId);
        stmt.setLong(2, usrId);
        stmt.setString(3, idioma);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid UserID (" + usrId + ") for activity " + verId);
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

    public static WAutorsVer[] loadManyFromVer(long verId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields() + ", " + WUsuaris.getSelectFields() + ", " + WDiccionari.getSelectTextField()
                + " FROM AUTORS_VER AV, USUARIS U, ROLS R, DICCIONARI D "
                + " WHERE AV.VERSIO=? AND D.IDIOMA=?"
                + " AND U.ID=AV.AUTOR AND D.CODI=R.CODI_DIC AND R.ID=AV.ROL"
                + " ORDER BY AV.ROL, U.NOM";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, verId);
        stmt.setString(2, idioma);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WAutorsVer wav = new WAutorsVer();
                wav.load(rs, con, idioma);
                v.add(wav);
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

        return (WAutorsVer[]) v.toArray(new WAutorsVer[v.size()]);
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
