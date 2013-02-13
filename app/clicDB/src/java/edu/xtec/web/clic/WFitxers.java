/*
 * WFitxers.java
 *
 * Created on 18 / novembre / 2003, 16:23
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
public class WFitxers extends TableWrapper {

    public long id;
    public long versio;
    public String nom;
    public int tipus;
    public int mida;
    public String descripcio;
    // Camps calculats
    public String ico;
    public String nomCurt;
    public boolean jnlp;
    public static final int ID = 0,
            VERSIO = 1,
            NOM = 2,
            TIPUS = 3,
            MIDA = 4,
            DESCRIPCIO = 5,
            NUM_FIELDS = 6;
    public static final String[] FIELD_NAMES = {
        "ID",
        "VERSIO",
        "NOM",
        "TIPUS",
        "MIDA",
        "DESCRIPCIO"
    };
    public static final String TABLE_NAME = "FITXERS";
    public static final String PREFIX = "F";

    /**
     * Creates a new instance of WFitxers
     */
    public WFitxers() {
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
        versio = rs.getLong(fn[VERSIO]);
        nom = rs.getString(fn[NOM]);
        tipus = rs.getInt(fn[TIPUS]);
        mida = rs.getInt(fn[MIDA]);
        descripcio = rs.getString(fn[DESCRIPCIO]);

        jnlp = (tipus == 8);

        ico = "null";
        switch (tipus) {
            case 0:
                ico = "mkdisk";
                break;

            case 2: // doc_pdf
                ico = "pdf";
                break;

            case 3: // zip
                ico = "zip";
                break;

            case 1: // doc_word
            case 5: // doc_hlp
            case 6: // doc_wri
            case 7: // html_link
                ico = "actdoc";
                break;

            case 4: // jclic.zip
            case 8: // jclic.inst
                ico = "instal";
                break;
        }

        int k = nom.lastIndexOf('/');
        nomCurt = (k > 0 ? nom.substring(k + 1) : nom);

    }

    public void load(String id, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM FITXERS F"
                + " WHERE F.ID=?";
        java.sql.PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setString(1, id);
        java.sql.ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid file ID: " + id);
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

    public static WFitxers[] loadManyFromVer(long verId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields()
                + " FROM FITXERS F"
                + " WHERE F.VERSIO=? "
                + " ORDER BY F.ID";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, verId);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WFitxers wf = new WFitxers();
                wf.load(rs, con, idioma);
                v.add(wf);
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

        return (WFitxers[]) v.toArray(new WFitxers[v.size()]);
    }
}
