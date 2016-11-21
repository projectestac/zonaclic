<%@page contentType="text/html; UTF-8"
%><jsp:useBean id="b" class="edu.xtec.web.clic.UsrLibBean" scope="request" /><%
if(!b.init(request, response, "ca")){%><jsp:forward page="error_ca.jsp"/><%}
// Aquí va el codi d'inicialització
%><!DOCTYPE html>
<html lang="ca">
  <head>
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="<%=b.getGoogleToken()%>">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src="https://apis.google.com/js/platform.js" async defer></script>
    <style type="text/css" src="main.css"></style>
  </head>
  <body>
    <div class="g-signin2" data-onsuccess="onSignIn" data-theme="dark"></div>
    <div id="result"></div>
    <div id="logout" class="hidden"><a href="#" onclick="signOut();">Sign out</a></div>
    <script src="main.js"></script>
  </body>
</html>