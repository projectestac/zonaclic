<%@page contentType="application/json" pageEncoding="UTF-8" session="true"
%><jsp:useBean id="b" class="edu.xtec.web.clic.UsrDeleteBean" scope="request" /><%
b.init(request, response, "ca");
%><%= b.getJsonResponse() %>