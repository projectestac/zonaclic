/*
 * ListBean.java
 *
 * Created on 30 de junio de 2003, 12:08
 */
package edu.xtec.web.clic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public class ListBean extends PageBean {

    public String area;
    public String idioma;
    public String nivell;
    public boolean ctm;
    public int ordre;
    public boolean desc;
    public int fromReg, maxRegs, numRegs, numPages, currentPage;
    public String[][] idiomes;
    public String[][] arees;
    public String[][] nivells;
    public String textTitol[], textTitolStr;
    public String textDesc[], textDescStr;
    public String textAut[], textAutStr;
    public String[][] data;
    public String queryFoundDesc, queryPageDesc;
    public int[] navigator;
    public int navPrev = -1, navNext = -1;
    // Llistes tancades
    private static HashMap llista_arees;
    private static HashMap llista_nivells;
    private static HashMap llista_idiomes;
    // Parametres
    public static final String MY_NAME = "listact.jsp";
    public static final String AREA = "area", IDIOMA = "idioma", NIVELL = "nivell", CTM = "ctm", ORDRE = "ordre", DESC = "desc";
    public static final String WILD = "*", ONE = "1", FROM = "from", NUM = "num";
    public static final String TEXT_TITOL = "text_titol", TEXT_DESC = "text_desc", TEXT_AUT = "text_aut";
    public static final int DEFAULT_MAX_REGS = 25;
    public static final String[] COLUMNES = {"DATA_CREACIO", "TITOL", "IDIOMES", "AREES", "NIVELLS"};
    public static final int COL_DATA = 0, COL_TITOL = 1, COL_IDIOMES = 2, COL_AREES = 3, COL_NIVELLS = 4, NUM_COLS = 5;

    /**
     * Creates a new instance of ListBean
     */
    public ListBean() {
        super();
    }

    protected String getMainBundle() {
        return "edu.xtec.resources.messages.listMessages";
    }

    //@Override
    protected void getRequestParams(HttpServletRequest request) throws Exception {
        super.getRequestParams(request);
        area = getParam(request, AREA, WILD);
        idioma = getParam(request, IDIOMA, WILD);
        nivell = getParam(request, NIVELL, WILD);
        //ctm=getBoolParam(request, CTM, true);
        ctm = ONE.equals(request.getParameter(CTM));

        String s;
        ordre = 0;
        desc = true;
        if ((s = request.getParameter(ORDRE)) != null && s.length() > 0) {
            try {
                ordre = Math.max(0, Math.min(Integer.parseInt(s), COLUMNES.length - 1));
            } catch (NumberFormatException ex) {
                ordre = 0;
            }
            desc = ONE.equals(request.getParameter(DESC));
        }
        fromReg = 0;
        if ((s = request.getParameter(FROM)) != null && s.length() > 0) {
            try {
                fromReg = Math.max(0, Integer.parseInt(s));
            } catch (NumberFormatException ex) {
                //
            }
        }
        maxRegs = DEFAULT_MAX_REGS;
        if ((s = request.getParameter(NUM)) != null && s.length() > 0) {
            try {
                maxRegs = Math.max(1, Integer.parseInt(s));
            } catch (NumberFormatException ex) {
                //
            }
        }

        textTitolStr = request.getParameter(TEXT_TITOL);
        if (textTitolStr != null) {
            textTitol = Utilities.parseSearchText(textTitolStr);
        }

        textDescStr = request.getParameter(TEXT_DESC);
        if (textDescStr != null) {
            textDesc = Utilities.parseSearchText(textDescStr);
        }

        textAutStr = request.getParameter(TEXT_AUT);
        if (textAutStr != null) {
            textAut = Utilities.parseSearchText(textAutStr);
        }

    }

    protected void process(edu.xtec.util.db.ConnectionBean con) throws Exception {
        idiomes = getLlistaIdiomes(con);
        arees = getLlistaArees(con);
        nivells = getLlistaNivells(con);
        data = performQuery(con);
    }

    protected String[][] performQuery(edu.xtec.util.db.ConnectionBean con) throws Exception {
        String[][] result;
        PreparedStatement stmt;
        ResultSet rs;
        boolean bArea = !WILD.equals(area);
        boolean bIdioma = !WILD.equals(idioma);
        boolean bNivell = !WILD.equals(nivell);

        // ACTIVITATS_DESC
        StringBuilder sb = new StringBuilder();

        // Modificat: Traiem "DISTINCT" per alleugerir la consulta
        //sb.append("SELECT DISTINCT A.ID, A.VALID, A.DATA_CREACIO, A.FLAG_NOU, AD.TITOL, AD.TITOL_MAJ, ACT_IDIOMES(A.ID) AS IDIOMES, ACT_NIVELLS(A.ID,?) AS NIVELLS, ACT_AREES(A.ID,?) AS AREES");
        // Segona modificacio: fem servir els camps consolidats TXAREES, TXIDIOMES i TXNIVELLS en comptes dels
        // procediments emmagatzemats que els calculaven
        //sb.append("SELECT A.ID, A.VALID, A.DATA_CREACIO, A.FLAG_NOU, AD.TITOL, AD.TITOL_MAJ, ACT_IDIOMES(A.ID) AS IDIOMES, ACT_NIVELLS(A.ID,?) AS NIVELLS, ACT_AREES(A.ID,?) AS AREES");
        sb.append("SELECT ");
        sb.append("A.ID, A.VALID, A.DATA_CREACIO, A.FLAG_NOU, AD.TITOL, AD.TITOL_MAJ, AD.TXIDIOMES AS IDIOMES, AD.TXNIVELLS AS NIVELLS, AD.TXAREES AS AREES");
        sb.append(" FROM ACTIVITATS A, ACTIVITATS_DESC AD");
        
        // Modificat: canviem el metode de cerca directa als camps 'area', 'idioma' i 'nivell' per un 'like'
        /*if (bArea) {
            sb.append(", ACTIVITATS_AREES AA");
        }
        if (bIdioma) {
            sb.append(", VERSIONS V, VERSIONS_IDIOMES VI");
        }
        if (bNivell) {
            sb.append(", ACTIVITATS_NIVELLS AN");
        }
        * 
        */

        sb.append(" WHERE AD.ID=A.ID AND A.VALID=? AND AD.IDIOMA=?");
        if (textTitol != null) {
            for (int i = 0; i < textTitol.length; i++) {
                sb.append(" AND AD.TITOL_MAJ LIKE ?");
            }
        }
        if (textDesc != null) {
            for (int i = 0; i < textDesc.length; i++) {
                sb.append(" AND AD.DESCRIPCIO_MAJ LIKE ?");
            }
        }
        if (textAut != null) {
            for (int i = 0; i < textAut.length; i++) {
                // Fem servir el camp AUTORS_MAJ en comptes de la funcio
                // sb.append(" AND ACT_AUTORS_MAJ(A.ID) LIKE ?");
                sb.append(" AND A.AUTORS_MAJ LIKE ?");
            }
        }

        
        if (bArea) {
            //sb.append(" AND A.ID=AA.ID AND AA.AREA=?");
            sb.append(" AND AD.TXAREES LIKE ?");
        }
        if (bIdioma) {
            //sb.append(" AND V.ACTIVITAT=A.ID AND VI.ID=V.ID");
            if (ctm) {
                //sb.append(" AND (A.FLAG1=? OR VI.IDIOMA=?)");
                sb.append(" AND(A.FLAG1=? OR AD.TXIDIOMES LIKE ?)");
            } else {
                //sb.append(" AND VI.IDIOMA=?");
                sb.append(" AND AD.TXIDIOMES LIKE ?");
            }
        }
        if (bNivell) {
            //sb.append(" AND A.ID=AN.ID AND AN.NIVELL=?");
            sb.append(" AND AD.TXNIVELLS LIKE ?");
        }

        //sb.append(" ORDER BY ").append(COLUMNES[ordre]);
        sb.append(" ORDER BY ").append(ordre == 1 ? "AD.TITOL_MAJ" : COLUMNES[ordre]);

        if (desc) {
            sb.append(" DESC");
        }
        if (ordre != 1) {
            //sb.append(",").append(COLUMNES[1]);
            sb.append(", AD.TITOL_MAJ");
        }

        stmt = con.getPreparedStatement(sb.substring(0), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        int i = 1;

        // Modificat: Els dos primers parametres ja no son necessaris, donat que es fan servir els camps consolidats
        //stmt.setString(i++, lang);
        //stmt.setString(i++, lang);

        stmt.setString(i++, "s");
        stmt.setString(i++, lang);

        if (textTitol != null) {
            for (int k = 0; k < textTitol.length; k++) {
                stmt.setString(i++, "%" + textTitol[k] + "%");
            }
        }
        if (textDesc != null) {
            for (int k = 0; k < textDesc.length; k++) {
                stmt.setString(i++, "%" + textDesc[k] + "%");
            }
        }
        if (textAut != null) {
            for (int k = 0; k < textAut.length; k++) {
                stmt.setString(i++, "%" + textAut[k] + "%");
            }
        }
        if (bArea) {
            //stmt.setString(i++, area);
            stmt.setString(i++, "%" + Utilities.getKeyFor(arees, area) + "%");
        }
        if (bIdioma) {
            if (ctm) {
                stmt.setString(i++, "s");
            }
            //stmt.setString(i++, idioma);
            stmt.setString(i++, "%" + idioma + "%");
        }
        if (bNivell) {
            //stmt.setString(i++, nivell);
            stmt.setString(i++, "%" + Utilities.getKeyFor(nivells, nivell) + "%");
        }

        rs = stmt.executeQuery();

        rs.last();
        numRegs = rs.getRow();

        if (numRegs < 1) {
            queryFoundDesc = bundle.getString("query_null");
            queryPageDesc = "";
            result = new String[0][];
        } else {
            sb.setLength(0);
            sb.append(bundle.getString("query_found_1")).append(" ").append(numRegs).append(" ").append(bundle.getString(numRegs == 1 ? "query_found_2_sing" : "query_found_2"));
            queryFoundDesc = sb.substring(0);

            if (fromReg >= numRegs) {
                fromReg = Math.max(0, numRegs - maxRegs);
            }

            numPages = Math.max(1, ((numRegs - 1) / maxRegs) + 1);
            currentPage = fromReg / maxRegs;

            if (numPages > 1) {
                sb.setLength(0);
                sb.append(bundle.getString("page")).append(" ").append(currentPage + 1).append("/").append(numPages);
                queryPageDesc = sb.substring(0);

                int startCount = Math.max(0, currentPage - 5);
                int endCount = Math.min(numPages, startCount + 10);
                if (endCount == numPages) {
                    startCount = Math.max(0, numPages - 10);
                }

                if (currentPage > 0) {
                    navPrev = Math.max(0, fromReg - maxRegs);
                }
                int n = endCount - startCount;
                navigator = new int[n];
                for (int z = 0; z < n; z++) {
                    navigator[z] = startCount + z;
                }
                if (currentPage < numPages - 1) {
                    navNext = fromReg + maxRegs;
                }
            } else {
                queryPageDesc = "";
            }

            if (fromReg > 1) {
                rs.absolute(fromReg);
            } else {
                rs.beforeFirst();
            }

            ArrayList v = new ArrayList(maxRegs);
            int j = 0;
            while (rs.next() && j < maxRegs) {
                String[] s = new String[NUM_COLS + 1];
                s[COL_DATA] = dateFormat.format(rs.getDate("DATA_CREACIO"));
                sb.setLength(0);
                sb.append("act_").append(lang).append(".jsp?id=").append(rs.getString("ID"));
                s[NUM_COLS] = sb.substring(0);
                s[COL_TITOL] = Utilities.xmlEncode(rs.getString("TITOL"));
                s[COL_IDIOMES] = Utilities.toNbsp(rs.getString("IDIOMES"));
                s[COL_AREES] = Utilities.toNbsp(rs.getString("AREES"));
                s[COL_NIVELLS] = Utilities.toNbsp(rs.getString("NIVELLS"));
                j++;
                v.add(s);
            }
            result = (String[][]) v.toArray(new String[v.size()][]);
        }
        rs.close();
        con.closeStatement(stmt);
        return result;

    }

    private String[][] getLlistaIdiomes(edu.xtec.util.db.ConnectionBean con) throws Exception {
        PreparedStatement stmt;
        ResultSet rs;
        if (llista_idiomes == null) {
            llista_idiomes = new HashMap();
        }
        String[][] result = (String[][]) llista_idiomes.get(lang);
        if (result == null) {
            stmt = con.getPreparedStatement(
                    "SELECT DISTINCT VI.IDIOMA, D.TEXT"
                    + " FROM VERSIONS_IDIOMES VI, IDIOMES I, DICCIONARI D"
                    + " WHERE I.ID=VI.IDIOMA AND D.CODI=I.CODI_DIC AND D.IDIOMA=?"
                    + " ORDER BY D.TEXT");
            stmt.setString(1, lang);
            rs = stmt.executeQuery();
            ArrayList v = new ArrayList(50);
            //TreeMap tm=new TreeMap();
            while (rs.next()) //tm.put(rs.getString("IDIOMA"), rs.getString("TEXT"));
            {
                v.add(new String[]{rs.getString("IDIOMA"), rs.getString("TEXT"), rs.getString("IDIOMA")});
            }
            rs.close();
            con.closeStatement(stmt);

            result = (String[][]) v.toArray(new String[v.size()][3]);
            /*
             * result=new String[tm.size()][2]; Iterator
             * it=tm.keySet().iterator(); int i=0; while(it.hasNext()){
             * result[i][0]=(String)it.next();
             * result[i][1]=(String)tm.get(result[i][0]); i++; }
             */
            llista_idiomes.put(lang, result);
        }
        return result;
    }

    private String[][] getLlistaArees(edu.xtec.util.db.ConnectionBean con) throws Exception {
        PreparedStatement stmt;
        ResultSet rs;
        if (llista_arees == null) {
            llista_arees = new HashMap();
        }
        String[][] result = (String[][]) llista_arees.get(lang);
        if (result == null) {
            stmt = con.getPreparedStatement(
                    "SELECT A.ID, A.CODI_BREU_DIC, D.TEXT"
                    + " FROM AREES A, DICCIONARI D"
                    + " WHERE D.IDIOMA=? AND D.CODI=A.CODI_DIC"
                    + " ORDER BY A.ORDRE");
            stmt.setString(1, lang);
            rs = stmt.executeQuery();

            ArrayList v = new ArrayList(20);
            while (rs.next()) {
                v.add(new String[]{rs.getString("ID"), rs.getString("TEXT"), rs.getString("CODI_BREU_DIC")});
            }
            rs.close();
            con.closeStatement(stmt);
            
            stmt = con.getPreparedStatement("SELECT TEXT FROM DICCIONARI WHERE IDIOMA=? AND CODI=?");
            Iterator it=v.iterator();
            while(it.hasNext()){
                String[] ar=(String [])it.next();
                stmt.setString(1, lang);
                stmt.setString(2, ar[2]);
                rs=stmt.executeQuery();
                ar[2] = rs.next() ? rs.getString("TEXT") : ar[0];
                rs.close();
            }
            con.closeStatement(stmt);
                        
            result = (String[][]) v.toArray(new String[v.size()][3]);

            llista_arees.put(lang, result);
        }
        return result;
    }

    private String[][] getLlistaNivells(edu.xtec.util.db.ConnectionBean con) throws Exception {
        PreparedStatement stmt;
        ResultSet rs;
        if (llista_nivells == null) {
            llista_nivells = new HashMap();
        }
        String[][] result = (String[][]) llista_nivells.get(lang);
        if (result == null) {
            stmt = con.getPreparedStatement(
                    "SELECT N.ID, N.CODI_BREU_DIC, D.TEXT"
                    + " FROM NIVELLS N, DICCIONARI D"
                    + " WHERE D.IDIOMA=? AND D.CODI=N.CODI_DIC"
                    + " ORDER BY N.ORDRE");
            stmt.setString(1, lang);
            rs = stmt.executeQuery();
            ArrayList v = new ArrayList(10);
            while (rs.next()) {
                v.add(new String[]{rs.getString("ID"), rs.getString("TEXT"), rs.getString("CODI_BREU_DIC")});
            }
            rs.close();
            con.closeStatement(stmt);

            stmt = con.getPreparedStatement("SELECT TEXT FROM DICCIONARI WHERE IDIOMA=? AND CODI=?");
            Iterator it=v.iterator();
            while(it.hasNext()){
                String[] nv=(String [])it.next();
                stmt.setString(1, lang);
                stmt.setString(2, nv[2]);
                rs=stmt.executeQuery();
                nv[2] = rs.next() ? rs.getString("TEXT") : nv[0];
                rs.close();
            }
            con.closeStatement(stmt);
                        
            result = (String[][]) v.toArray(new String[v.size()][3]);

            llista_nivells.put(lang, result);
        }
        return result;
    }
}
