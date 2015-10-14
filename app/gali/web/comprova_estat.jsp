<%@page contentType="text/html; charset=iso-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%

String msg="OK";
if (!b.init(request, session, response)) {
  msg = (String)request.getAttribute("ERROR");
  if (msg == null || msg.length() == 0 ) {
    msg = "ERROR DESCONEGUT";
  }
}%><%=msg%>
