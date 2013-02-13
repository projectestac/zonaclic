<%@page session="false" contentType="text/html; charset=iso-8859-1"
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Variables de servei</title>
    </head>
    <body>
        <h2>Variables de servei zonaClic</h2>
        <p>Data: <%=java.text.DateFormat.getInstance().format(new java.util.Date())%></p>

        <table border="1">
<%
edu.xtec.web.clic.Context ctx=edu.xtec.web.clic.Context.cntx;
java.util.Iterator it=ctx.keySet().iterator();
while(it.hasNext()){
  String key=(String)it.next();
  String value=(String)ctx.get(key);
%>
<tr><td><%=key%></td><td><%=value%></td></tr>
<% } %>
        </table>
    </body>
</html>
