<%@page contentType="application/json" pageEncoding="UTF-8"
%><jsp:useBean id="b" class="edu.xtec.web.clic.UsrLibBean" scope="request" /><%
if(!b.init(request, response, "ca")){%>{"status":"error"}<%}
// TODO: Save persistent data in cookie
%>{
 "email": "<%= b.email %>",
 "user": "<%= b.fullUserName %>",
 "avatar": "<%= b.avatar %>",
 "expires": "<%= b.expDate %>",
}