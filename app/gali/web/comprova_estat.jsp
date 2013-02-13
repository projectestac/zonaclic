<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
String msg="OK";
// MIRAR-ho!!!!

if(!b.init(request, response, "ca")){
  msg=(String)request.getAttribute("ERROR");
  if(msg==null || msg.length()==0){
    msg="ERROR DESCONEGUT";
  }
}%><%=msg%>
