<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bg" class="gali.beans.GaliGuidedBean" scope="request" /><%
if(!bg.init(request, session, response)){%><jsp:forward page="error.html"/><%}
session.setAttribute("cons", "no");
if(!bg.isValidated()) {//No està validat 
  bg.redirectToValidation(bg.JSP_GUIDED);
} else{ //Està validat 
      if (!bg.hasDoneGaliActivity()
              && bg.getVar()!=gali.beans.GaliCtt.VAR_OC  //Excloem l'aranes del test de diagnosi
              ){ //No ha fet cap activitat Gali 
        %><jsp:forward page="galiIndexNewUser.jsp"/><%
      } else{
        %><jsp:forward page="<%=bg.getBaseUrl(false)%>"/><%
      }
}%>
