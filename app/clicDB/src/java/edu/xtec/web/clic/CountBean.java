/*
 * CountBean.java
 *
 * Created on 19 / desembre / 2003, 18:02
 */
package edu.xtec.web.clic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public class CountBean extends PageBean {

    public int nProjects;
    public int nVersions;
    public int nActivities;
    public int nVersionsClic3;
    public int nActivitiesClic3;
    public int nVersionsJClic;
    public int nActivitiesJClic;
    public String by;
    public static final String BY = "by";
    public String[] items;
    public String[][] act;

    /**
     * Creates a new instance of CountBean
     */
    public CountBean() {
        super();
    }

    protected String getMainBundle() {
        return "edu.xtec.resources.messages.countMessages";
    }

    //@Override
    protected void getRequestParams(HttpServletRequest request) throws Exception {
        super.getRequestParams(request);
        by = getParam(request, BY, ListBean.WILD);
    }

    protected void process(edu.xtec.util.db.ConnectionBean con) throws Exception {
        compta(con);
        if (by != null && !ListBean.WILD.equals(by)) {
            performQuery(con);
        }
    }

    private void compta(edu.xtec.util.db.ConnectionBean con) throws Exception {
        PreparedStatement stmt;
        ResultSet rs;

        stmt = con.getPreparedStatement(
                "SELECT COUNT(ACTIVITATS.ID)"
                + " FROM ACTIVITATS"
                + " WHERE ACTIVITATS.VALID=?");
        stmt.setString(1, "s");
        rs = stmt.executeQuery();
        nProjects = rs.getInt(1);
        rs.close();
        con.closeStatement(stmt);

        String sql =
                "SELECT COUNT(V.ID), SUM(V.N_ACTIVITATS)"
                + " FROM VERSIONS V, ACTIVITATS A"
                + " WHERE V.ACTIVITAT=A.ID"
                + " AND A.VALID=?"
                + " AND V.TIPUS=?";
        stmt = con.getPreparedStatement(sql);
        stmt.setString(1, "s");
        stmt.setString(2, "c");
        rs = stmt.executeQuery();
        nVersionsClic3 = rs.getInt(1);
        nActivitiesClic3 = rs.getInt(2);
        rs.close();
        con.closeStatement(stmt);

        stmt = con.getPreparedStatement(sql);
        stmt.setString(1, "s");
        stmt.setString(2, "j");
        rs = stmt.executeQuery();
        nVersionsJClic = rs.getInt(1);
        nActivitiesJClic = rs.getInt(2);
        rs.close();
        con.closeStatement(stmt);

        nVersions = nVersionsClic3 + nVersionsJClic;
        nActivities = nActivitiesClic3 + nActivitiesJClic;
    }

    protected void performQuery(edu.xtec.util.db.ConnectionBean con) throws Exception {
        String[][] result = null;
        PreparedStatement stmt;
        ResultSet rs;
        boolean bArea = ListBean.AREA.equals(by);
        boolean bIdioma = ListBean.IDIOMA.equals(by);
        boolean bNivell = ListBean.NIVELL.equals(by);

        // ACTIVITATS_DESC
        StringBuilder sb = new StringBuilder();

        // Modificat: fem servir el camo TXIDIOMES en comptes del procediment emmagatzemat
        // sb.append("SELECT DISTINCT A.ID, A.DATA_CREACIO, A.FLAG_NOU, AD.TITOL, ACT_IDIOMES(A.ID) AS IDIOMES, D.TEXT AS TEXT");
        sb.append("SELECT DISTINCT A.ID, A.DATA_CREACIO, A.FLAG_NOU, AD.TITOL, AD.TXIDIOMES AS IDIOMES, D.TEXT AS TEXT");
        sb.append(" FROM ACTIVITATS A, ACTIVITATS_DESC AD, DICCIONARI D");
        if (bArea) {
            sb.append(", ACTIVITATS_AREES AA, AREES AR");
        } else if (bIdioma) {
            sb.append(", VERSIONS V, VERSIONS_IDIOMES VI, IDIOMES IDIO");
        } else if (bNivell) {
            sb.append(", ACTIVITATS_NIVELLS AN, NIVELLS NI");
        }

        sb.append(" WHERE AD.ID=A.ID AND A.VALID=? AND AD.IDIOMA=?");

        if (bArea) {
            sb.append(" AND AA.ID=A.ID AND AR.ID=A.AREA AND D.CODI=AR.CODI_DIC");
        } else if (bIdioma) {
            sb.append(" V.ACTIVITAT=A.ID AND VI.ID=V.ID AND IDIO.ID=VI.ID AND D.CODI=IDIO.CODI_DIC");
        } else if (bNivell) {
            sb.append(" AND AN.ID=A.ID AND NI.ID=AN.NIVELL AND D.CODI=NI.CODI_DIC");
        }

        sb.append(" AND D.IDIOMA=?");

        sb.append(" ORDER BY ").append(bArea ? "AR.CODI_DIC" : bIdioma ? "IDIO.CODI_DIC" : "NI.CODI_DIC");

        stmt = con.getPreparedStatement(sb.substring(0));
        stmt.setString(1, "s");
        stmt.setString(2, lang);
        stmt.setString(3, lang);

        rs = stmt.executeQuery();

        ArrayList v = new ArrayList();
        while (rs.next()) {
            v.add(new String[]{
                        rs.getString(1), //A.ID
                        dateFormat.format(rs.getDate(2)), //A.DATA_CREACIO
                        rs.getString(3), //A.FLAG_NOU
                        rs.getString(4), //AD.TITOL
                        rs.getString(5), //AD.TXIDIOMES AS IDIOMES
                        rs.getString(6)}); //D.TEXT AS TEXT"                
        }
        rs.close();
        con.closeStatement(stmt);

        act = (String[][]) v.toArray(new String[v.size()][]);

        v.clear();
        String current = null;
        for (int i = 0; i < act.length; i++) {
            if (!act[i][5].equals(current)) {
                current = act[i][5];
                v.add(current);
            }
        }
        items = (String[]) v.toArray(new String[v.size()]);
    }
}
