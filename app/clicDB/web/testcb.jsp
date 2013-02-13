<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><html>
<head><title>ConnectionBeanBroker status</title></head>
<body>
<H2>ConnectionBean status</H2>
<p>Current time is: <%=new java.util.Date()%></p>
<%=edu.xtec.util.db.ConnectionBeanProvider.getGlobalInfo()%>
</body>
</html>
