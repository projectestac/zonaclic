<%-- 
    Document   : dummyValidate
    Created on : 11/10/2010, 13:54:45
    Author     : fbusquet
--%>
<%@page session="false" contentType="text/html" pageEncoding="UTF-8"%>
<%!
    private Cookie createCookie(String sPropertyName, String sValue){
        Cookie c = new Cookie(sPropertyName, sValue);
        c.setMaxAge(-1);
        c.setPath("/");
        c.setDomain("xtec.cat");
    	return c;
    }
%>
<%
String user = request.getParameter("user");
String url = request.getParameter("url");
String logo = request.getParameter("logo");
int ALLOWED_PORT=8084;

if (user!=null && !"".equals(user) && url!=null && !"".equals(url)){
  response.addCookie(createCookie("usuari-xtec", user));
  response.addCookie(createCookie("email-xtec", user+"@test"));
  String sPortal = (new java.util.Date()).getTime()+"AP";
  response.addCookie(createCookie("xtec", sPortal));
  try{
    response.sendRedirect(url);
  }catch (Exception ioe) {
    ioe.printStackTrace();
  }
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Simulació de validació per cookie xtec.cat</title>
    </head>
    <body onload="document.login.user.focus()">
        <h2>Simulació de validació per cookie xtec.cat</h2>
        <hr>
<% if(request.getRemotePort()!=ALLOWED_PORT) {%>
<form action="dummyValidate.jsp" method="post" name="login" id="login" autocomplete="off">
  <%if (url == null) url="gali/error.html";%>
      <input type="hidden" name="url" value="<%=url%>"/>

    <% if (logo!=null){%>
      <input type="hidden" name="logo" value="<%= logo %>"/>
      <p><img src="<%= logo %>"/></p>
    <%} %>

    <p>ID d'usuari/ària: <input maxlength="30" id="user" name="user" tabindex="1" type="text"></p>
    <p><input value="Test" type="submit"></p>
</form>
<% } else { %>
<p>ERROR: Vàlid només per crides pel port <= ALLOWED_PORT %></p>
<% } %>
    <hr>
    </body>
</html>
