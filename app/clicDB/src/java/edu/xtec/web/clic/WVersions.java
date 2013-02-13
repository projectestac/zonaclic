/*
 * WVersions.java
 *
 * Created on 18 / novembre / 2003, 16:36
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
public class WVersions extends TableWrapper {

    public long id;
    public long activitat;
    public java.sql.Date dataVersio;
    public java.sql.Date dataRevisio;
    public boolean esPublic;
    public String tipus;
    public String idiomaVersioBak;
    public String etiqueta;
    public String descripcio;
    public String path;
    public int nActivitats;
    public int nPaquets;
    public int mida;
    public String idt;
    public int nivell;
    public String folder;
    public WAutorsVer[] autors;
    public WFitxers[] fitxers;
    public WVersionsIdiomes[] idiomes;
    public static final int ID = 0,
            ACTIVITAT = 1,
            DATA_VERSIO = 2,
            DATA_REVISIO = 3,
            ES_PUBLIC = 4,
            TIPUS = 5,
            IDIOMA = 6,
            ETIQUETA = 7,
            DESCRIPCIO = 8,
            PATH = 9,
            N_ACTIVITATS = 10,
            N_PAQUETS = 11,
            MIDA = 12,
            IDT = 13,
            NIVELL = 14,
            FOLDER = 15,
            NUM_FIELDS = 16;
    public static final String[] FIELD_NAMES = {
        "ID",
        "ACTIVITAT",
        "DATA_VERSIO",
        "DATA_REVISIO",
        "ES_PUBLIC",
        "TIPUS",
        "IDIOMA",
        "ETIQUETA",
        "DESCRIPCIO",
        "PATH",
        "N_ACTIVITATS",
        "N_PAQUETS",
        "MIDA",
        "IDT",
        "NIVELL",
        "FOLDER"
    };
    public static final String TABLE_NAME = "VERSIONS";
    public static final String PREFIX = "V";

    /**
     * Creates a new instance of WVersions
     */
    public WVersions() {
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
        activitat = rs.getLong(fn[ACTIVITAT]);
        dataVersio = rs.getDate(fn[DATA_VERSIO]);
        dataRevisio = rs.getDate(fn[DATA_REVISIO]);
        esPublic = getBoolean(rs, fn[ES_PUBLIC]);
        tipus = rs.getString(fn[TIPUS]);
        idiomaVersioBak = rs.getString(fn[IDIOMA]);
        etiqueta = rs.getString(fn[ETIQUETA]);
        descripcio = rs.getString(fn[DESCRIPCIO]);
        path = rs.getString(fn[PATH]);
        nActivitats = rs.getInt(fn[N_ACTIVITATS]);
        nPaquets = rs.getInt(fn[N_PAQUETS]);
        mida = rs.getInt(fn[MIDA]);
        idt = rs.getString(fn[IDT]);
        nivell = rs.getInt(fn[NIVELL]);
        folder = rs.getString(fn[FOLDER]);

        fitxers = WFitxers.loadManyFromVer(id, con, idioma);
        autors = WAutorsVer.loadManyFromVer(id, con, idioma);
        idiomes = WVersionsIdiomes.loadManyFromVer(id, con, idioma);
    }

    public static WVersions[] loadManyFromAct(long actId, ConnectionBean con, String idioma) throws Exception {
        ArrayList v = new ArrayList(3);
        String sql =
                "SELECT " + getSelectFields()
                + " FROM VERSIONS V"
                + " WHERE V.ACTIVITAT=?"
                + " ORDER BY V.NIVELL, V.TIPUS DESC";
        PreparedStatement stmt = con.getPreparedStatement(sql);
        stmt.setLong(1, actId);
        ResultSet rs = stmt.executeQuery();
        Exception ex = null;
        try {
            while (rs.next()) {
                WVersions wv = new WVersions();
                wv.load(rs, con, idioma);
                v.add(wv);
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

        return (WVersions[]) v.toArray(new WVersions[v.size()]);
    }

    public boolean hasLang(String codiIdioma) {
        boolean result = false;
        if (idiomes != null && codiIdioma != null) {
            for (int i = 0; i < idiomes.length; i++) {
                if (codiIdioma.equals(idiomes[i].codiIdioma)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
