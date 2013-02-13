/*
 * WCentres.java
 *
 * Created on 18 / novembre / 2003, 15:45
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;

/**
 *
 * @author fbusquet
 */
public class WCentres extends TableWrapper {

    public String id;
    public String idioma;
    public String nom;
    public String municipi;
    public String domicili;
    public String cpostal;
    public String telf1;
    public String telf2;
    public String fax;
    public String email;
    public String web;
    public String dt;
    public String comarca;
    public String estatus;
    public String pais;
    public String comunitatId;
    public String comentaris;
    public String comunitat;
    public static final int ID = 0,
            IDIOMA = 1,
            NOM = 2,
            MUNICIPI = 3,
            DOMICILI = 4,
            CPOSTAL = 5,
            TELF1 = 6,
            TELF2 = 7,
            FAX = 8,
            EMAIL = 9,
            WEB = 10,
            DT = 11,
            COMARCA = 12,
            ESTATUS = 13,
            PAIS = 14,
            COMUNITAT = 15,
            COMENTARIS = 16,
            NUM_FIELDS = 17;
    public static final String[] FIELD_NAMES = {
        "ID",
        "IDIOMA",
        "NOM",
        "MUNICIPI",
        "DOMICILI",
        "CPOSTAL",
        "TELF1",
        "TELF2",
        "FAX",
        "EMAIL",
        "WEB",
        "DT",
        "COMARCA",
        "ESTATUS",
        "PAIS",
        "COMUNITAT",
        "COMENTARIS"
    };
    public static final String TABLE_NAME = "CENTRES";
    public static final String PREFIX = "C";
    public static final String TNP = "CENTRES C, COMUNITATS CM";

    /**
     * Creates a new instance of WCentres
     */
    public WCentres() {
    }
    private static String SELECT_FIELDS;

    public static String getSelectFields() {
        if (SELECT_FIELDS == null) {
            SELECT_FIELDS = buildSelectFields(FIELD_NAMES, getPrefixedFieldNames(), PREFIX);
            SELECT_FIELDS = SELECT_FIELDS + ", CM.NOM AS CM_NOM";
        }
        return SELECT_FIELDS;
    }
    private static String[] PREFIXED_FIELDS;

    public static String[] getPrefixedFieldNames() {
        if (PREFIXED_FIELDS == null) {
            String[] PP = buildPrefixedFieldNames(FIELD_NAMES, PREFIX);
            PREFIXED_FIELDS = new String[PP.length + 1];
            System.arraycopy(PP, 0, PREFIXED_FIELDS, 0, PP.length);
            PREFIXED_FIELDS[PP.length] = "CM_NOM";
        }
        return PREFIXED_FIELDS;
    }

    public void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String idioma_dummy) throws Exception {
        String[] fn = getPrefixedFieldNames();
        id = rs.getString(fn[ID]);
        idioma = rs.getString(fn[IDIOMA]);
        nom = rs.getString(fn[NOM]);
        municipi = rs.getString(fn[MUNICIPI]);
        domicili = rs.getString(fn[DOMICILI]);
        cpostal = rs.getString(fn[CPOSTAL]);
        telf1 = rs.getString(fn[TELF1]);
        telf2 = rs.getString(fn[TELF2]);
        fax = rs.getString(fn[FAX]);
        email = rs.getString(fn[EMAIL]);
        web = rs.getString(fn[WEB]);
        dt = rs.getString(fn[DT]);
        comarca = rs.getString(fn[COMARCA]);
        estatus = rs.getString(fn[ESTATUS]);
        pais = rs.getString(fn[PAIS]);
        comunitatId = rs.getString(fn[COMUNITAT]);
        comentaris = rs.getString(fn[COMENTARIS]);

        comunitat = rs.getString(fn[NUM_FIELDS]);
    }

    public void load(String id, ConnectionBean con, String idioma) throws Exception {

        String sql =
                "SELECT " + getSelectFields()
                + " FROM " + TNP
                + " WHERE C.ID=?"
                + " AND CM.ID(+)=C.COMUNITAT";

        java.sql.PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setString(1, id);
        java.sql.ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid center ID: " + id);
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

    public String getLinkedName() {
        StringBuffer sb = new StringBuffer();
        boolean w = (web != null && web.length() > 0);
        if (w) {
            sb.append("<a href=\"");
            if (!web.startsWith("http://")) {
                sb.append("http://");
            }
            sb.append(web).append("\" target=\"_blank\">");
        }
        sb.append(Utilities.xmlEncode(nom));
        if (w) {
            sb.append("</a>");
        }
        return sb.substring(0);
    }
    private static final String[] NO_COMARCA = {"Barcelona", "Tarragona", "Lleida", "Girona"};

    public String getLocalitat() {
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        String[] llocs = {municipi, comarca, comunitat, pais};
        for (int i = 0; i < llocs.length; i++) {
            String s = llocs[i];
            if (s != null && s.length() > 0) {
                if (flag) {
                    sb.append(" (");
                }
                sb.append(Utilities.xmlEncode(s));
                if (flag) {
                    sb.append(")");
                    break;
                }
                flag = true;
                int j;
                for (j = 0; j < NO_COMARCA.length; j++) {
                    if (s.equalsIgnoreCase(NO_COMARCA[j])) {
                        break;
                    }
                }
                if (j < NO_COMARCA.length) {
                    break;
                }
            }
        }
        return sb.substring(0);
    }
}
