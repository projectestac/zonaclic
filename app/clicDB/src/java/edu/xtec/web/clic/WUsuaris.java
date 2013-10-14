/*
 * WUsuaris.java
 *
 * Created on 7 de julio de 2003, 16:05
 */
package edu.xtec.web.clic;

import edu.xtec.util.db.ConnectionBean;

/**
 *
 * @author fbusquet
 */
public class WUsuaris extends TableWrapper {

    // Camps de dades
    public long id;
    public long associat_a;
    public String tipus;
    public String login;
    public String pwd;
    public String nom;
    public long privacitat;
    public String sexe;
    public java.sql.Date data_alta;
    public String organitzacio;
    public String nom_complet;
    public String dni;
    public boolean es_passaport;
    public String mail;
    public String url;
    public String idioma;
    public String comentaris;
    public String domicili;
    public String localitat;
    public String pais;
    public String cp;
    public String zona;
    public String telefon;
    public boolean flag1;
    public boolean flag2;
    public boolean flag3;
    public boolean flag4;
    public boolean flag5;
    public String nom_maj;
    public static final int ID = 0,
            ASSOCIAT_A = 1,
            TIPUS = 2,
            LOGIN = 3,
            PWD = 4,
            NOM = 5,
            PRIVACITAT = 6,
            SEXE = 7,
            DATA_ALTA = 8,
            ORGANITZACIO = 9,
            NOM_COMPLET = 10,
            DNI = 11,
            ES_PASSAPORT = 12,
            MAIL = 13,
            URL = 14,
            IDIOMA = 15,
            COMENTARIS = 16,
            DOMICILI = 17,
            LOCALITAT = 18,
            PAIS = 19,
            CP = 20,
            ZONA = 21,
            TELEFON = 22,
            FLAG1 = 23,
            FLAG2 = 24,
            FLAG3 = 25,
            FLAG4 = 26,
            FLAG5 = 27,
            NUM_FIELDS = 28;
    public static final String[] FIELD_NAMES = {
        "ID",
        "ASSOCIAT_A",
        "TIPUS",
        "LOGIN",
        "PWD",
        "NOM",
        "PRIVACITAT",
        "SEXE",
        "DATA_ALTA",
        "ORGANITZACIO",
        "NOM_COMPLET",
        "DNI",
        "ES_PASSAPORT",
        "MAIL",
        "URL",
        "IDIOMA",
        "COMENTARIS",
        "DOMICILI",
        "LOCALITAT",
        "PAIS",
        "CP",
        "ZONA",
        "TELEFON",
        "FLAG1",
        "FLAG2",
        "FLAG3",
        "FLAG4",
        "FLAG5"
    };
    public static final String TABLE_NAME = "USUARIS";
    public static final String PREFIX = "U";
    public static final String TNP = TABLE_NAME + " " + PREFIX;

    /**
     * Creates a new instance of WUsuari
     */
    public WUsuaris() {
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
        associat_a = rs.getLong(fn[ASSOCIAT_A]);
        tipus = rs.getString(fn[TIPUS]);
        login = rs.getString(fn[LOGIN]);
        pwd = rs.getString(fn[PWD]);
        nom = rs.getString(fn[NOM]);
        privacitat = rs.getLong(fn[PRIVACITAT]);
        sexe = rs.getString(fn[SEXE]);
        data_alta = rs.getDate(fn[DATA_ALTA]);
        organitzacio = rs.getString(fn[ORGANITZACIO]);
        nom_complet = rs.getString(fn[NOM_COMPLET]);
        dni = rs.getString(fn[DNI]);
        es_passaport = getBoolean(rs, fn[ES_PASSAPORT]);
        mail = rs.getString(fn[MAIL]);
        url = rs.getString(fn[URL]);
        idioma = rs.getString(fn[IDIOMA]);
        comentaris = rs.getString(fn[COMENTARIS]);
        domicili = rs.getString(fn[DOMICILI]);
        localitat = rs.getString(fn[LOCALITAT]);
        pais = rs.getString(fn[PAIS]);
        cp = rs.getString(fn[CP]);
        zona = rs.getString(fn[ZONA]);
        telefon = rs.getString(fn[TELEFON]);
        flag1 = getBoolean(rs, fn[FLAG1]);
        flag2 = getBoolean(rs, fn[FLAG2]);
        flag3 = getBoolean(rs, fn[FLAG3]);
        flag4 = getBoolean(rs, fn[FLAG4]);
        flag5 = getBoolean(rs, fn[FLAG5]);
    }

    public void load(long id, ConnectionBean con, String idioma) throws Exception {
        String sql =
                "SELECT " + getSelectFields()
                + " FROM USUARIS U"
                + " WHERE U.ID=?";
        java.sql.PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, id);
        java.sql.ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            if (!rs.next()) {
                ex = new Exception("Invalid user ID: " + id);
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

    public String getNom() {
        return Utilities.xmlEncode(nom);
    }
}
