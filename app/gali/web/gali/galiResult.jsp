<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="br" class="gali.beans.GaliResultBean" scope="request" /><%
if(!br.init(request, session, response)){%><jsp:forward page="error.html"/><%}
if(br.isCons()){%>
    <jsp:forward page="galiPage.jsp"/>
<%} else if (br.isOk && !br.incomplete){%>
    <jsp:forward page="galiResultBe.jsp"/>
<%} else {%>
    <jsp:forward page="galiResultMal.jsp"/>
<%}%>
