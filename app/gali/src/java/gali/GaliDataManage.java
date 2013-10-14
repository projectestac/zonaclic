/*
 * GaliDataManage.java
 *
 * Created on 28 / maig / 2003, 13:44
 */

package gali;

import edu.xtec.util.db.ConnectionBean;
import java.sql.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author  allastar
 */
public abstract class GaliDataManage {
    
    //private static edu.xtec.util.db.ConnectionBeanProvider db;
    private static java.util.Map hmActs;
    private static java.util.Map hmDiagnosi;
    
    private static final String PACS="/gali/pacs.properties";
    private static final String TD="/gali/td.properties";
    //private static final String DBCONF="gali.properties";
    
    /*
    public static final int COM_ESCRIU_PAGE=0;
    public static final int QUI_SOC_PAGE=1;
    public static final int QUINA_HORA_PAGE=2;
    public static final int MENJO_PAGE=3;
    public static final int FAIG_PAGE=4;
    public static final int MORFOSINTAXI_PAGE=5;
     */
    
    public static final int INI_LEVEL=0;
    public static final int PROF_LEVEL=1;    
    public static final double MINIMUM_PASSED_QUALIFICATION=75;
    
    //transient HttpSession session;
    
    public static final String[][][] iniPackInfo={
        {{"alfabet","g0101a"},{"on_lletra","g0102a"},{"nombres","g0103a"},{"unitats","g0104a"}},
        {{"nom","g0201a"},{"familia","g0202a"},{"cos","g0203a"},{"fisic","g0204a"},{"naixement","g0205a"}},
        {{"dies","g0301a"},{"hora","g0302a"},{"personals","g0303a"},{"vestit","g0304a"},{"animals","g0305a"}},
        {{"aliments","g0401a"},{"altres_aliments","g0402a"},{"mobles_menjador","g0403a"},{"mobles_cuina","g0404a"},{"escola","g0405a"}},
        {{"accions","g0501a"},{"ofici","g0502a"},{"comprar","g0503a"},{"eines","g0504a"},{"on_vas","g0505a"}},
        {{"article","g0601a"},{"masculi","g0602a"},{"determinants","g0603a"},{"pronoms","g0604a"},{"adverbis","g0605a"},{"verb_present","g0606a"},{"verb_passat","g0607a"}}
    };
    
    public static final String[][][] profPackInfo={
        {{"vocals","g0101b"},{"consonants","g0102b"},{"centenes","g0103b"}},
        {{"qui_es_qui","g0201b"},{"relacions_familiars","g0202b"},{"aspecte","g0203b"}},
        {{"ahir","g0301b"},{"hora_temps","g0302b"},{"opinions","g0303b"},{"vesteixo","g0304b"}},
        {{"menjar","g0401b"},{"habitatge","g0402b"},{"adreces","g0403b"},{"estudi","g0404b"}},
        {{"que_fa","g0501b"},{"oficis","g0502b"},{"on_comprar","g0503b"},{"avaria","g0504b"},{"viatge","g0505b"}},
        {{"substantius","g0601b"},{"combinacions","g0602b"},{"quantificadors","g0603b"},{"verb_subjuntiu","g0604b"},{"obligacio","g0605b"},{"correlacio","g0606b"}}        
    };
    //String[][][] iniPackInfo;
    //String[][][] profPackInfo;
    
    private static java.util.Properties pPackageDesc, pPackageDescNOC, pPackageDescALG, pPackageDescOC;
        
    static{
        //java.util.Properties pDatabase = new java.util.Properties();
        try{
            /*
            pDatabase.load(GaliDataManage.class.getResourceAsStream("/gali/"+DBCONF));
            File f=new File(System.getProperty("user.home"), DBCONF);
            if(f.exists()){
                FileInputStream is=new FileInputStream(f);
                pDatabase.load(is);
                is.close();
            }
            db=edu.xtec.util.db.ConnectionBeanProvider.getConnectionBeanProvider(true, pDatabase);
             */
        
            pPackageDesc=new java.util.Properties(); 
            pPackageDesc.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions.properties"));
            
            pPackageDescNOC=new java.util.Properties(); 
            pPackageDescNOC.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions.properties"));
            pPackageDescNOC.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions_noc.properties"));
            
            pPackageDescALG=new java.util.Properties(); 
            pPackageDescALG.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions.properties"));
            pPackageDescALG.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions_alg.properties"));
            
            pPackageDescOC=new java.util.Properties(); 
            pPackageDescOC.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions.properties"));
            pPackageDescOC.load(GaliDataManage.class.getResourceAsStream("/gali/resources/package_descriptions_oc.properties"));
            
            hmDiagnosi=createMapPackActivities(TD,false); //La clau és l'activitat
            hmActs=createMapPackActivities(PACS,true); //La clau és el paquet
            
            //tablePrefix=pDatabase.getProperty("tablePrefix", "");
            
        } catch(Exception ex){
            System.err.println("FATAL ERROR:\n"+ex);
            ex.printStackTrace(System.err);
        }
    }
    
    
    /** Creates a new instance of GaliDataManage */
    //public GaliDataManage() throws Exception{
        //this.session=session;
        
        //iniPackInfo=new String[6][][];//{{"","",""}};
        //profPackInfo=new String[6][][];//{{"","",""}};
    
    /*    
        if(db==null){
            java.util.Properties pDatabase = new java.util.Properties();
            pDatabase.load(getClass().getResourceAsStream("/com/javaexchange/dbConnectionBroker/connection.properties"));
            db=new com.javaexchange.dbConnectionBroker.DbConnectionBeanBroker(pDatabase);
        }
        
        if(pPackageDesc==null){
            pPackageDesc=new java.util.Properties(); 
            pPackageDesc.load(getClass().getResourceAsStream("/gali/resources/package_descriptions.properties"));
        }
     */
      
        /*
        iniPackInfo[COM_ESCRIU_PAGE]=new String[][]{{"alfabet","g0101a"},{"on_lletra","g0102a"},{"nombres","g0103a"},{"unitats","g0104a"}};
        iniPackInfo[QUI_SOC_PAGE]=new String[][]{{"nom","g0201a"},{"familia","g0202a"},{"cos","g0203a"},{"fisic","g0204a"},{"naixement","g0205a"}};
        iniPackInfo[QUINA_HORA_PAGE]=new String[][]{{"dies","g0301a"},{"hora","g0302a"},{"personals","g0303a"},{"vestit","g0304a"},{"animals","g0305a"}};
        iniPackInfo[MENJO_PAGE]=new String[][]{{"aliments","g0401a"},{"altres_aliments","g0402a"},{"mobles_menjador","g0403a"},{"mobles_cuina","g0404a"},{"escola","g0405a"}};
        iniPackInfo[FAIG_PAGE]=new String[][]{{"accions","g0501a"},{"ofici","g0502a"},{"comprar","g0503a"},{"eines","g0504a"},{"on_vas","g0505a"}};
        iniPackInfo[MORFOSINTAXI_PAGE]=new String[][]{{"article","g0601a"},{"masculi","g0602a"},{"determinants","g0603a"},{"pronoms","g0604a"},{"adverbis","g0605a"},{"verb_present","g0606a"},{"verb_passat","g0607a"}};
        */
        /*
        profPackInfo[COM_ESCRIU_PAGE]=new String[][]{{"vocals","g0101b"},{"consonants","g0102b"},{"centenes","g0103b"}};
        profPackInfo[QUI_SOC_PAGE]=new String[][]{{"qui_es_qui","g0201b"},{"relacions_familiars","g0202b"},{"aspecte","g0203b"}};
        profPackInfo[QUINA_HORA_PAGE]=new String[][]{{"ahir","g0301b"},{"hora_temps","g0302b"},{"opinions","g0303b"},{"vesteixo","g0304b"}};
        profPackInfo[MENJO_PAGE]=new String[][]{{"menjar","g0401b"},{"habitatge","g0402b"},{"adreces","g0403b"},{"estudi","g0404b"}};
        profPackInfo[FAIG_PAGE]=new String[][]{{"que_fa","g0501b"},{"oficis","g0502b"},{"on_comprar","g0503b"},{"avaria","g0504b"},{"viatge","g0505b"}};
        profPackInfo[MORFOSINTAXI_PAGE]=new String[][]{{"substantius","g0601b"},{"combinacions","g0602b"},{"quantificadors","g0603b"},{"verb_subjuntiu","g0604b"},{"obligacio","g0605b"},{"correlacio","g0606b"}};        
         */
    //}
    
    /*
    public void setSession(HttpSession session){
        this.session=session;
    }
    
    public HttpSession getSession(){
        return session;
    }
     */

    //private static edu.xtec.util.db.ConnectionBeanProvider db;
    private static edu.xtec.util.db.ConnectionBeanProvider getDB() throws Exception {
       return Context.cntx.getDB();
       /*
       if(db==null){
          System.out.println("GALI: Creating ConnecionBeanProvider");
          db=edu.xtec.util.db.ConnectionBeanProvider.getConnectionBeanProvider(true, Context.cntx);
       }
       return db;
        */
    }

    public static String getPackageDescription(String id, int var){
        return id==null ? null : 
            (var==gali.beans.GaliCtt.VAR_NOC ? pPackageDescNOC.getProperty(id) : 
                var==gali.beans.GaliCtt.VAR_ALG ? pPackageDescALG.getProperty(id) : 
                   var==gali.beans.GaliCtt.VAR_OC ? pPackageDescOC.getProperty(id) :
                       pPackageDesc.getProperty(id));
    }
    
    public static String getPageIconURL(String id){
        return id==null ? null : pPackageDesc.getProperty(id+"_icon");
    }
    
    public static String[][] getPackageInfo(int iPage, int iLevel){
        String[][] result=null;
        if(iLevel==INI_LEVEL){
            if(iPage>=0 && iPage<iniPackInfo.length)
                result=iniPackInfo[iPage];
        }
        else {
            if(iPage>=0 && iPage<profPackInfo.length)
                result=profPackInfo[iPage];
        }
        return result;
    }
    
    public static boolean hasDoneGaliActivity(String sUserId) throws Exception{
        /* Retorna cert si l'usuari 'sUserId' ha fet algun paquet del Galí (indicat
         amb el session_code='gali'. */
        boolean hasDone=false;
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT COUNT(*) FROM "+Context.cntx.getProperty("tablePrefix")+"sessions s ";
        query+="WHERE s.user_id=? ";
        query+="AND s.session_code='gali'";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                hasDone=rs.getInt(1)>0;
            }
            rs.close();
            cb.closeStatement(pstmt);            
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return hasDone;
    }
    
    public static boolean hasPassedAllPackages(String sUserId, HttpSession session){
        /* Retorna cert si l'usuari 'sUserId' ha superat tots els paquets Galí. */
        boolean bPassedAll=true;
        for(int iPage=0; iPage<6 && bPassedAll; iPage++)
            bPassedAll=hasPassedPage(iPage, PROF_LEVEL, sUserId, session);
        for(int iPage=0; iPage<6 && bPassedAll; iPage++)
            bPassedAll=hasPassedPage(iPage, INI_LEVEL, sUserId, session);
        return bPassedAll;
    }
    
    //public Vector getConvalidated(Str
    
    public static boolean[][][] getPassedGaliPackages(String sUserId, HttpSession session){
        /* Retorna una matriu tridimensional de booleans que indiquen si l'usuari
         'sUserId' ha superat mitjançant el test de diagnosi els paquets Galí indexats. */
        if (session!=null && session.getAttribute("pgp_"+sUserId)!=null){
            //System.out.println("TD de la sessió");//%
            return (boolean[][][])session.getAttribute("pgp_"+sUserId);
        }
        else{
            boolean[][][] passed=new boolean[6][7][2];
            java.util.Vector vActs;
            try{
                vActs=getGaliActivities(sUserId);
            } catch(Exception ex){
                System.err.println(ex);
                vActs=new java.util.Vector();
            }
            java.util.Enumeration e=vActs.elements();
            while (e.hasMoreElements()){ // Es recorren les activitats Galí que ha fet l'usuari 'sUserId'
                String sActivityName=e.nextElement().toString().toLowerCase();////
                if (sActivityName!=null){
                    int i=sActivityName.indexOf('/');
                    if (i>=0) sActivityName=sActivityName.substring(i+1);
                    //System.out.println("Ha fet l'activitat:"+sActivityName);//%
                }
                
                String pac=getPassedPackage(sActivityName);
                //System.out.println("pac per "+sActivityName+" = "+pac);
                if (pac!=null){ // Havent fet l'activitat 'sActivityName' es suposa haber superat el paquet 'pac'
                    pac=pac.toLowerCase();
                    //System.out.println("Convalido pac:"+pac+"<--");
                    try{
                        int iPage=Integer.parseInt(pac.substring(2,4))-1;
                        int iPack=Integer.parseInt(pac.substring(4,6))-1;
                        int iLevel=(pac.substring(6,7).equals("a"))?0:1;
                        //System.out.println("pg:"+iPage+" pk:"+iPack+" lev:"+iLevel);
                        passed[iPage][iPack][iLevel]=true;
                    }
                    catch (Exception ex){
                        ex.printStackTrace(System.out);
                    }
                }
            }
            setPassedGaliPackages(sUserId, passed, session);
            return passed;
        }
    }
    
    public static void setPassedGaliPackages(String sUserId, boolean[][][] bPassed, HttpSession session){
        //System.out.println("TD de l'usuari "+sUserId+" afegit a la sessió");
        if (session!=null)
            session.setAttribute("pgp_"+sUserId, bPassed);
    }
    
    private static java.util.Vector getGaliActivities (String sUserId) throws Exception {
        /* Retorna un Vector amb els activity_name de les activitats Galí (indicades
         amb el activity_code='gali') que ha realitzat l'usuari 'sUserId' */

        String prefix=Context.cntx.getProperty("tablePrefix");

        java.util.Vector vActivities=new java.util.Vector();
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT DISTINCT(a.activity_name) FROM "+prefix+"sessions s, "+prefix+"activities a ";
        query+="WHERE s.user_id=? ";
        query+="AND s.project_name='td' "; //&
        query+="AND s.session_code='gali' ";
        query+="AND s.session_id=a.session_id";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            ResultSet rs=pstmt.executeQuery();
            while (rs.next())
                vActivities.add(rs.getString(1));
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return vActivities;
    }
    
    public static boolean hasPassedPage(int iPage, int iLevel, String sUserId, HttpSession session){
        /* Retorna cert si l'usuari 'sUserId' ha superat tots els paquets Galí
         de la plana 'iPage' en el nivell 'iLevel'. */
        boolean bPassed=true;
        String[][] packInfo=getPackageInfo(iPage,iLevel);
        if (packInfo!=null){
            for (int i=0;i<packInfo.length && bPassed;i++){
                String sPackageName=packInfo[i][1];
                bPassed=hasPassedPackage(sUserId, iPage, i, iLevel, session);
            }
        }
        return bPassed;
    }
    
    public static boolean hasPassedPackage(String sUserId, int iPage, int iPackage, int iLevel, HttpSession session){
        /* Retorna cert si l'usuari 'sUserId' ha superat tots el paquet Galí 'iPackage'
         de la plana 'iPage' en el nivell 'iLevel'.*/
        boolean [][][] bTD=getPassedGaliPackages(sUserId, session);
        if (bTD[iPage][iPackage][iLevel]==false){ //Per el test de diagnosi no l'ha passat
            if (session!=null && session.getAttribute("pack_"+sUserId+"_"+iPage+"_"+iPackage+"_"+iLevel)!=null){
                //System.out.println("Package "+iPage+","+iPackage+","+iLevel+" obtingut de la sessió.");
                return ((Boolean)session.getAttribute("pack_"+sUserId+"_"+iPage+"_"+iPackage+"_"+iLevel)).booleanValue();
            }
            else{
                String [][] packInfo=getPackageInfo(iPage,iLevel);
                String sPackName=packInfo[iPackage][1];
                boolean b=hasPassedPackage(sUserId,sPackName);
                //System.out.println("db hasPassedPackage("+sUserId+","+iPage+","+iPackage+","+iLevel+")?"+b);
                setPassedPackage(sUserId,iPage,iPackage,iLevel, b, session);
                return b;
            }
        }
        else return true;
    }
    
    public static void setPassedPackage(String sUserId, int iPage, int iPackage, int iLevel, Boolean bPassed, HttpSession session){
        //System.out.println("Package "+iPage+","+iPackage+","+iLevel+" afegit a la sessió.");
        if (session!=null)
            session.setAttribute("pack_"+sUserId+"_"+iPage+"_"+iPackage+"_"+iLevel,bPassed);
    }
    
    private static boolean hasPassedPackage(String sUserId, String sPackageName){
        boolean bPassed=false;
        try {
            if (packageIsOkInConsolidatedTable(sUserId, sPackageName)) {
                return true;
            }

            //double qualifConsolidated=getQualificationInConsalidateTable(sUserId, sPackageName);
            //if (qualifConsolidated>=MINIMUM_PASSED_QUALIFICATION) return true;

            java.util.Vector vSessions = getFinishedPackageSessions(sUserId, sPackageName);
            java.util.Enumeration e = vSessions.elements();
            while (e.hasMoreElements() && !bPassed) {
                String sSessionId = e.nextElement().toString();
                double qualification = getPackageQualification(sPackageName, sSessionId);
                bPassed = (qualification >= MINIMUM_PASSED_QUALIFICATION);
            //System.out.println("session:"+sSessionId+"--> Qualification:"+qualification);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return bPassed;
    }
    
/*
    private static double getQualificationInConsalidateTable(String sUserId, String sPackageName){
        double qualification=-1;
        ConnectionBean cb=db.getConnectionBean();
        String query="SELECT best_qualification ";
        query+="FROM "+Context.TABLE_PREFIX+"consolidated_info ";
        query+="WHERE user_id=? ";
        query+="AND project_name=?";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sPackageName);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                qualification=rs.getDouble(1);
            }
            rs.close();
            //pstmt.close();
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            db.freeConnectionBean(cb);
        }
        return qualification;
    }    
 */
    
    private static boolean packageIsOkInConsolidatedTable(String sUserId, String sPackageName) throws Exception{
        double qualification=-1;
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT best_qualification ";
        query+="FROM "+Context.cntx.getProperty("tablePrefix")+"consolidated_info ";
        query+="WHERE user_id=? ";
        query+="AND project_name=? ";
        query+="AND finished=1";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sPackageName);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                qualification=rs.getDouble(1);
            }
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return qualification>=MINIMUM_PASSED_QUALIFICATION;
    }
        
    protected static java.util.Vector getFinishedPackageSessions(String sUserId, String sPackageName) throws Exception {
        /*Retorna un Vector amb els identificadors de sessió on l'usuari 'sUserId' ha
         finalitzat el paquet 'sPackageName' */

        String prefix=Context.cntx.getProperty("tablePrefix");

        java.util.Vector vSessions=new java.util.Vector();
        String sLastActivityName=getLastActivityName(sPackageName.toLowerCase()+".pac");
        if (sLastActivityName!=null){
            sLastActivityName=sLastActivityName.toLowerCase();
            int i=sLastActivityName.indexOf('.');
            if (i>0){
                String s=sLastActivityName.substring(0,i);
                sLastActivityName=s+"/"+sLastActivityName;
            }
        }
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT s.session_id FROM "+prefix+"sessions s, "+prefix+"activities a ";
        query+="WHERE s.user_id=? ";
        query+="AND s.project_name=? ";
        query+="AND s.session_id=a.session_id ";
        query+="AND s.consolidated=0 "; //Només sessions no consolidades ja
        query+="AND a.activity_name=?";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sPackageName);
            pstmt.setString(3,sLastActivityName);
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()){
                String s=rs.getString(1);
                vSessions.add(s);
            }
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        //System.out.println("b="+vSessions.size());
        return vSessions;
    }
    
    public static double getPackageQualificationSessionKey(String sUserId, String sKey) throws Exception{
        //System.out.println("getPackageQualification("+sUserId+","+sKey+")");

        String prefix=Context.cntx.getProperty("tablePrefix");

        double qualification=0;
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT AVG(a.qualification) FROM "+prefix+"activities a, "+prefix+"sessions s ";
        query+="WHERE s.user_id=? ";
        query+="AND s.session_key=? ";
        query+="AND s.session_id=a.session_id";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sKey);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                qualification=rs.getInt(1);
                //System.out.println("Qualification="+qualification);
            }
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return qualification;
    }
    
    protected static double getPackageQualification(String sPackageName, String sSessionId) throws Exception{
        double qualification=0;
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT AVG(a.qualification) FROM "+Context.cntx.getProperty("tablePrefix")+"activities a ";
        query+="WHERE a.session_id=? ";
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sSessionId);
            ResultSet rs=pstmt.executeQuery();
            if (rs.next()){
                qualification=rs.getInt(1);
            }
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return qualification;
    }
    
    public static boolean consolidateGaliInfo(String sUserId, String sPackageName) throws Exception{
        boolean result=false;
        java.util.Vector v=getFinishedPackageSessions(sUserId, sPackageName);
        ConnectionBean cb=getDB().getConnectionBean();
        String query="SELECT session_id, session_key ";
        query+="FROM "+Context.cntx.getProperty("tablePrefix")+"sessions ";
        query+="WHERE user_id=? ";
        query+="AND project_name=? ";
        query+="AND session_code=? "; // Només de Galí
        query+="AND consolidated=0 "; // No consolidades
        try{
            PreparedStatement pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sPackageName);
            pstmt.setString(3,"gali");
            ResultSet rs=pstmt.executeQuery();
            while (rs.next()){ // Recorro totes les sessions no consolidades on l'usuari 'sUserId' ha fet el paquet Galí 'sPackageName'.
                String sSessionId=rs.getString(1);
                String sSessionKey=rs.getString(2);
                if(v.contains(sSessionId))
                    result=true;
                consolidateInfo(cb, sUserId, sSessionId, sPackageName, sSessionKey);
            }
            rs.close();
            cb.closeStatement(pstmt);
        }
        catch (SQLException e){
            e.printStackTrace(System.out);
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return result;
    }
    
    public static void consolidateInfo(ConnectionBean cb, String sUserId, String sSessionId, String sPackageName, String sSessionKey)throws SQLException{

        String prefix=Context.cntx.getProperty("tablePrefix");

        String query="SELECT count(a.total_time),avg(a.qualification) ";
        query+="FROM "+prefix+"sessions s, "+prefix+"activities a ";
        query+="WHERE s.session_id=? ";
        //query+="AND s.project_name=? ";
        query+="AND s.session_key=? ";
        query+="AND s.consolidated=? ";
        query+="AND a.session_id=s.session_id";
        PreparedStatement pstmt=cb.getPreparedStatement(query);
        pstmt.setString(1,sSessionId);
        //pstmt.setString(2,sPackageName);
        pstmt.setString(2,sSessionKey);
        pstmt.setInt(3,0); //Que no estigui consolidada
        
        ResultSet rs=pstmt.executeQuery();
        if (rs.next()){
            long time=rs.getLong(1);
            float qualification=rs.getFloat(2);
            try{
                java.util.Vector v=getFinishedPackageSessions(sUserId,sPackageName);
                boolean bFinished=v.contains(sSessionId);
                updateConsolidatedInfo(cb,sUserId,sSessionId,sPackageName,time,qualification,bFinished);
            } catch(Exception ex){
                System.err.println(ex);
            }
        }
        
        rs.close();
        cb.closeStatement(pstmt);
    }
    
    protected static void updateConsolidatedInfo(ConnectionBean cb, String sUserId, String sSessionId, String sPackageName, long time, float qualification,boolean bFinished) throws SQLException{

        String prefix=Context.cntx.getProperty("tablePrefix");

        PreparedStatement pstmt;
        String query;
        if (existConsolidatedInfo(cb, sUserId, sPackageName)){
            query="UPDATE "+prefix+"consolidated_info ";
            query+="SET tries=tries+1, ";
            query+="total_time=total_time+?, ";
            query+="qualification=((qualification*tries)+?)/(tries+1), ";
            query+="best_qualification=greatest(best_qualification,?), ";
            query+="finished=greatest(finished,"+(bFinished?1:0)+") ";
            query+="WHERE user_id=? ";
            query+="AND project_name=? ";
            query+="AND key=?";
            pstmt=cb.getPreparedStatement(query);
            pstmt.setInt(1,(int)time);
            pstmt.setFloat(2,qualification);
            pstmt.setFloat(3,qualification);
            pstmt.setString(4,sUserId);
            pstmt.setString(5,sPackageName);
            pstmt.setString(6,"gali");
            pstmt.executeUpdate();
            cb.closeStatement(pstmt);
        }
        else{ // Si no existeix, es crea
            query="INSERT INTO "+prefix+"consolidated_info(user_id,project_name,key,tries,total_time,qualification,best_qualification,finished) ";
            query+="VALUES (?,?,?,?,?,?,?,?)";
            pstmt=cb.getPreparedStatement(query);
            pstmt.setString(1,sUserId);
            pstmt.setString(2,sPackageName);
            pstmt.setString(3,"gali");
            pstmt.setInt(4,1); // Un cop
            pstmt.setInt(5,(int)time);
            pstmt.setInt(6,(int)qualification);
            pstmt.setInt(7,(int)qualification); // best
            pstmt.setInt(8,(bFinished?1:0));
            pstmt.executeUpdate();
            cb.closeStatement(pstmt);
        }
        query="UPDATE "+prefix+"sessions ";
        query+="SET consolidated=1 ";
        query+="WHERE session_id=?";
        pstmt=cb.getPreparedStatement(query);
        pstmt.setString(1,sSessionId);
        pstmt.executeUpdate(); // Marco la sessió com a consolidada
        cb.closeStatement(pstmt);
        
        query="DELETE FROM "+prefix+"activities ";
        query+="WHERE session_id=?";
        pstmt=cb.getPreparedStatement(query);
        pstmt.setString(1,sSessionId);
        pstmt.executeUpdate(); // Elimino la informació de les activitats fetes a la sessió
        cb.closeStatement(pstmt);

        query="DELETE FROM "+prefix+"actions ";
        query+="WHERE session_id=?";
        pstmt=cb.getPreparedStatement(query);
        pstmt.setString(1,sSessionId);
        pstmt.executeUpdate();  // Elimino la informació de les accions fetes a la sessió
        cb.closeStatement(pstmt);
    }
    
    private static boolean existConsolidatedInfo(ConnectionBean cb, String sUserId, String sPackageName) throws SQLException{
        boolean bExist=false;
        String query="SELECT count(*) FROM "+Context.cntx.getProperty("tablePrefix")+"consolidated_info WHERE user_id=? AND project_name=?";
        PreparedStatement pstmt=cb.getPreparedStatement(query);
        pstmt.setString(1,sUserId);
        pstmt.setString(2,sPackageName);
        ResultSet rs=pstmt.executeQuery();
        if (rs.next()){
            bExist=rs.getInt(1)>0;
        }
        rs.close();
        cb.closeStatement(pstmt);
        return bExist;
    }
    
    private static String getPassedPackage(String sActivityName){
        return (String)hmDiagnosi.get(sActivityName);
    }
    
    private static String getLastActivityName(String sPackageName){
        return (String)hmActs.get(sPackageName);
    }
    
    private static java.util.Map createMapPackActivities(String sFileName, boolean keyIsPac) throws Exception{
        /* Crea una taula de hash amb el mapeig entre paquets i activitats al
         fitxer 'sFileName'. KeyIsPac indica si la clau de la taula serà el paquet.*/
        
        java.util.Map result;
        
        java.util.Properties prop=new java.util.Properties();
        prop.load(GaliDataManage.class.getResourceAsStream(sFileName));
        if(keyIsPac)
            result=prop;
        else{
            result=new java.util.HashMap(prop.size());
            java.util.Iterator it=prop.keySet().iterator();
            while(it.hasNext()){
                String key=(String)it.next();
                String value=(String)prop.get(key);
                result.put(value, key);
            }
        }
        return result;
    }
    
    public static boolean deleteUserData(String sUserId, HttpSession session) throws Exception{

        String prefix=Context.cntx.getProperty("tablePrefix");

        boolean result=true;        
        ConnectionBean cb=getDB().getConnectionBean();
        try{
            PreparedStatement pstmt;
            StringBuilder query=new StringBuilder();
            query.setLength(0);
            query.append("DELETE FROM ").append(prefix).append("actions WHERE session_id IN");
            query.append(" (SELECT session_id FROM ").append(prefix).append("sessions");
            query.append(" WHERE user_id=?");
            query.append(" AND session_code=?");
            query.append(")");
            pstmt=cb.getPreparedStatement(query.toString());
            pstmt.setString(1,sUserId);
            pstmt.setString(2,"gali");            
            pstmt.executeUpdate(); // Elimino la informació de les accions d'aquest usuari
            cb.closeStatement(pstmt);
            
            query.setLength(0);
            query.append("DELETE FROM ").append(prefix).append("activities WHERE session_id IN");
            query.append(" (SELECT session_id FROM ").append(prefix).append("sessions");
            query.append(" WHERE user_id=?");
            query.append(" AND session_code=?");
            query.append(")");
            pstmt=cb.getPreparedStatement(query.toString());
            pstmt.setString(1,sUserId);
            pstmt.setString(2,"gali");            
            pstmt.executeUpdate(); // Elimino la informació de les sessions d'aquest usuari
            cb.closeStatement(pstmt);
            
            query.setLength(0);
            query.append("DELETE FROM ").append(prefix).append("sessions");
            query.append(" WHERE user_id=?");
            query.append(" AND session_code=?");
            pstmt=cb.getPreparedStatement(query.toString());
            pstmt.setString(1,sUserId);
            pstmt.setString(2,"gali");            
            pstmt.executeUpdate(); // Elimino la informació de les sessions de l'usuari
            cb.closeStatement(pstmt);
            
            query.setLength(0);
            query.append("DELETE FROM ").append(prefix).append("consolidated_info");
            query.append(" WHERE user_id=?");
            pstmt=cb.getPreparedStatement(query.toString());
            pstmt.setString(1,sUserId);
            pstmt.executeUpdate(); // Elimino la informació consolidada de l'usuari            
            cb.closeStatement(pstmt);
            
            java.util.Enumeration en=session.getAttributeNames();
            java.util.Vector v=new java.util.Vector();
            while(en.hasMoreElements()){
                String s=(String)en.nextElement();
                if(s!=null && (s.startsWith("pgp_") || (s.startsWith("pack_"))))
                    v.add(s);
            }
            java.util.Iterator it=v.iterator();
            while(it.hasNext())
                session.removeAttribute((String)it.next());
            
        }
        catch(SQLException ex){
            result=false;
        }
        finally{
            getDB().freeConnectionBean(cb);
        }
        return result;
    }
    
    
    /*public static void main(String[] args){
        try{
            GaliDataManage g=new GaliDataManage();
            //g.getPassedGaliPackages("prova5");
            g.hasPassedPackage("fbusquet","g0602b");
            g.db.destroy(0);
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }*/
}
