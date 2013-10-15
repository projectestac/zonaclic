/*
 * WActivitats.java
 *
 * Created on 4 de julio de 2003, 15:33
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 *
 * @author fbusquet
 */
public class WActivitats extends TableWrapper {

    public long id;
    public boolean valid;
    public java.sql.Date data_creacio;
    public java.sql.Date data_revisio;
    public String imatge;
    public int imatge_w;
    public int imatge_h;
    public boolean flag_cd_1;
    public boolean flag_cd_2;
    public boolean flag_cd_3;
    public boolean flag_nou;
    public boolean te_sons;
    public String old_tipus;
    public String old_num;
    public boolean flag1;
    public boolean flag2;
    public boolean flag3;
    public boolean flag4;
    public boolean flag5;
    public String comentaris;
    public String licenseText;
    public String ccLicenseUrl;
    public String ccLicenseTag;
    public WActivitatsDesc[] desc;
    public String titol;
    public String titolEnc;
    public String descripcio;
    public WAutorsAct[] autors;
    public WCentresAct[] centres;
    public WActivitatsArees[] arees;
    public WActivitatsNivells[] nivells;
    public WDescriptorsAct[] descriptors;
    public WVersions[] versions;
    public static final int ID = 0,
            VALID = 1,
            DATA_CREACIO = 2,
            DATA_REVISIO = 3,
            IMATGE = 4,
            IMATGE_W = 5,
            IMATGE_H = 6,
            FLAG_CD_1 = 7,
            FLAG_CD_2 = 8,
            FLAG_CD_3 = 9,
            FLAG_NOU = 10,
            TE_SONS = 11,
            OLD_TIPUS = 12,
            OLD_NUM = 13,
            FLAG1 = 14,
            FLAG2 = 15,
            FLAG3 = 16,
            FLAG4 = 17,
            FLAG5 = 18,
            COMENTARIS = 19,
            CODI_LLICENCIA_CC = 20,
            LLICENCIA_NO_CC = 21,
            NUM_FIELDS = 22;
    public static final String[] FIELD_NAMES = {
        "ID",
        "VALID",
        "DATA_CREACIO",
        "DATA_REVISIO",
        "IMATGE",
        "IMATGE_W",
        "IMATGE_H",
        "FLAG_CD_1",
        "FLAG_CD_2",
        "FLAG_CD_3",
        "FLAG_NOU",
        "TE_SONS",
        "OLD_TIPUS",
        "OLD_NUM",
        "FLAG1",
        "FLAG2",
        "FLAG3",
        "FLAG4",
        "FLAG5",
        "COMENTARIS",
        "CODI_LLICENCIA_CC",
        "LLICENCIA_NO_CC"
    };
    public static final String TABLE_NAME = "ACTIVITATS";
    public static final String PREFIX = "A";

    /**
     * Creates a new instance of WActivitats
     */
    public WActivitats() {
    }
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

    public void load(ResultSet rs, ConnectionBean con, String idioma) throws Exception {
        String[] fn = getPrefixedFieldNames();

        id = rs.getLong(fn[ID]);
        valid = getBoolean(rs, fn[VALID]);
        data_creacio = rs.getDate(fn[DATA_CREACIO]);
        data_revisio = rs.getDate(fn[DATA_REVISIO]);
        imatge = rs.getString(fn[IMATGE]);
        imatge_w = rs.getInt(fn[IMATGE_W]);
        imatge_h = rs.getInt(fn[IMATGE_H]);
        flag_cd_1 = getBoolean(rs, fn[FLAG_CD_1]);
        flag_cd_2 = getBoolean(rs, fn[FLAG_CD_2]);
        flag_cd_3 = getBoolean(rs, fn[FLAG_CD_3]);
        flag_nou = getBoolean(rs, fn[FLAG_NOU]);
        te_sons = getBoolean(rs, fn[TE_SONS]);
        old_tipus = rs.getString(fn[OLD_TIPUS]);
        old_num = rs.getString(fn[OLD_NUM]);
        flag1 = getBoolean(rs, fn[FLAG1]);
        flag2 = getBoolean(rs, fn[FLAG2]);
        flag3 = getBoolean(rs, fn[FLAG3]);
        flag4 = getBoolean(rs, fn[FLAG4]);
        flag5 = getBoolean(rs, fn[FLAG5]);
        comentaris = rs.getString(fn[COMENTARIS]);
        licenseText = rs.getString(fn[LLICENCIA_NO_CC]);
        if (licenseText == null || licenseText.length() == 0) {
            ccLicenseUrl = rs.getString(fn[CODI_LLICENCIA_CC]);
        }

        if (con != null) {
            desc = WActivitatsDesc.loadManyFromAct(id, con, idioma);
            if (idioma != null) {
                for (int i = 0; i < desc.length; i++) {
                    if (desc[i].idioma.equals(idioma)) {
                        titol = desc[i].titol;
                        titolEnc = desc[i].titolEnc;
                        descripcio = desc[i].descripcio;
                        break;
                    }
                }
                autors = WAutorsAct.loadManyFromAct(id, con, idioma);
                centres = WCentresAct.loadManyFromAct(id, con, idioma);
                arees = WActivitatsArees.loadManyFromAct(id, con, idioma);
                nivells = WActivitatsNivells.loadManyFromAct(id, con, idioma);
                descriptors = WDescriptorsAct.loadManyFromAct(id, con, idioma);
                versions = WVersions.loadManyFromAct(id, con, idioma);
            }
        }

        ccLicenseTag = getLicenseTag();
    }

    public String getLicenseTag() {
        String result = null;
        if (ccLicenseUrl != null) {
            String myUrl = Context.cntx.getProperty("appBase") + "/act.jsp?id=" + id;
            Calendar cal = Calendar.getInstance();
            cal.setTime(data_creacio);
            StringBuffer sb = new StringBuffer(3000);
            sb.append("<!--\n");
            sb.append("<rdf:RDF xmlns=\"http://web.resource.org/cc/\"\n");
            sb.append("  xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\n");
            sb.append("  xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n");
            sb.append("<Work rdf:about=\"").append(myUrl).append("\">\n");
            if (titolEnc != null) {
                sb.append("   <dc:title>").append(titolEnc).append("</dc:title>\n");
            }
            if (data_creacio != null) {
                sb.append("   <dc:date>").append(cal.get(Calendar.YEAR)).append("</dc:date>\n");
            }
            if (descripcio != null) {
                sb.append("   <dc:description>").append(Utilities.xmlEncode(descripcio)).append("</dc:description>\n");
            }
            if (autors != null) {
                StringBuffer sb2 = new StringBuffer();
                for (int i = 0; i < autors.length; i++) {
                    if (autors[i] != null && autors[i].autor != null) {
                        sb2.append("<Agent><dc:title>");
                        sb2.append(Utilities.xmlEncode(autors[i].autor.getNom()));
                        sb2.append("</dc:title></Agent>\n");
                    }
                }
                String s = sb2.substring(0);

                sb.append("<dc:creator>\n").append(s).append("</dc:creator>\n");
                sb.append("<dc:rights>\n").append(s).append("</dc:rights>\n");
            }
            sb.append("<dc:type rdf:resource=\"http://purl.org/dc/dcmitype/Interactive\" />\n");
            sb.append("<dc:source rdf:resource=\"").append(myUrl).append("\"/>\n");
            sb.append("<license rdf:resource=\"http://creativecommons.org/licenses/").append(ccLicenseUrl).append("/2.5/\" />\n");
            sb.append("</Work>\n");
            sb.append("<License rdf:about=\"http://creativecommons.org/licenses/by-nc-sa/2.5/\">\n");
            sb.append(" <permits rdf:resource=\"http://web.resource.org/cc/Reproduction\" />\n");
            sb.append(" <permits rdf:resource=\"http://web.resource.org/cc/Distribution\" />\n");
            sb.append(" <requires rdf:resource=\"http://web.resource.org/cc/Notice\" />\n");
            if (ccLicenseUrl.indexOf("by") >= 0) {
                sb.append(" <requires rdf:resource=\"http://web.resource.org/cc/Attribution\" />\n");
            }
            if (ccLicenseUrl.indexOf("nc") >= 0) {
                sb.append("<prohibits rdf:resource=\"http://web.resource.org/cc/CommercialUse\" />\n");
            }
            if (ccLicenseUrl.indexOf("nd") < 0) {
                sb.append("<permits rdf:resource=\"http://web.resource.org/cc/DerivativeWorks\" />\n");
            }
            if (ccLicenseUrl.indexOf("sa") >= 0) {
                sb.append("<requires rdf:resource=\"http://web.resource.org/cc/ShareAlike\" />\n");
            }
            sb.append("</License>\n");
            sb.append("</rdf:RDF>\n");
            sb.append("-->\n");
            result = sb.substring(0);
        }
        return result;
    }

    public void load(long id, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM ACTIVITATS A"
                + " WHERE A.ID=?";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid activity ID: " + id);
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
}
