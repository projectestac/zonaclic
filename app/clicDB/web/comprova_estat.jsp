<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.ListBean" scope="request" /><%
String msg="OK";
if(!b.init(request, response, "ca")){
  msg=(String)request.getAttribute("ERROR");
  if(msg==null || msg.length()==0){
    msg="ERROR DESCONEGUT";
  }
} else { 
  String sPath = edu.xtec.web.clic.Context.cntx.getProperty("staticFilesPath"); 
  String sEnv = edu.xtec.web.clic.Context.cntx.getProperty("environment");
  if (request.getParameter("env") != null) {
    sEnv = (String)request.getParameter("env");
    sEnv = sEnv.toUpperCase();
    if (sEnv.equals("INT")) {
      sPath = "/serveis/dades/int/NAS01/1057-1072/";
    } else if (sEnv.equals("ACC") || sEnv.equals("PRE")) {
      sPath = "/serveis/dades/pre/NAS01/1057-1072/";
    } else {
      sPath = "/j2ee/e13_clic/web/";
    }
  } 

  java.io.File testFile=new java.io.File(sPath+"index.htm");
  if(!testFile.exists())
      msg="ERROR: No es pot accedir al sistema de fitxers. Reviseu el punt de muntatge '"+sPath+"'";
}
%><%=msg%>
