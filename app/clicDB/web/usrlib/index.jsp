<%@page contentType="text/html" pageEncoding="UTF-8"
%><jsp:useBean id="b" class="edu.xtec.web.clic.UsrLibBean" scope="request" /><%
if(!b.init(request, response, "ca")){%><jsp:forward page="error_ca.jsp"/><%}
// Aquí va el codi d'inicialització
%><!DOCTYPE html>
<html lang="ca">
  <head>
    <link href="main.css" rel="stylesheet" type="text/css">
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="<%=b.getGoogleToken()%>">
    <!-- meta name="google-signin-hosted_domain" content="xtec.cat" -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://apis.google.com/js/platform.js?onload=start" async defer></script>
    <script src="main.js"></script>
  </head>
  <body>
    <div id="signInBtn">Entra</div>
    <div id="result"></div>
    <button id="logout" class="hidden">Surt</button>
  </body>
</html>