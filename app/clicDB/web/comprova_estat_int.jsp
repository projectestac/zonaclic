<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.ListBean" scope="request" /><%
String msg="OK";
if(!b.init(request, response, "ca")){
  msg=(String)request.getAttribute("ERROR");
  if(msg==null || msg.length()==0){
    msg="ERROR DESCONEGUT";
  }
}
else {
  String path = "/serveis/dades/int/NAS01/1057-1072/";
  java.io.File testFile=new java.io.File(path+"index.htm");
  if(!testFile.exists())
      msg="ERROR: No es pot accedir al sistema de fitxers. Reviseu el punt de muntatge '"+path+"'";
}
%><%=msg%>
