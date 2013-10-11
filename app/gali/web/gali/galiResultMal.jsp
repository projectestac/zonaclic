<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="br" class="gali.beans.GaliResultBean" scope="request" /><%
if(!br.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute;</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21"><table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=br.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"><div align="right"><a href="galiMain.jsp"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Men&uacute; principal"></a></div></td>
        </tr>
      </table></td>
  </tr>
  <tr> 
    <td valign="top"><table width="566" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td height="30">&nbsp;</td>
        </tr>
        <tr> 
          <td height="120"><a href="gali.jsp"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a></td>
        </tr>
        <tr> 
          <td height="15"><p>&nbsp;</p></td>
        </tr>
        <tr> 
          <td valign="top"> <table width="566" height="200" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td height="7"><img src="../home/top_left.gif" width="7" height="7"></td>
                <td height="7" colspan="2" valign="top"><img src="../home/pixel.gif" width="560" height="1"></td>
                <td height="7"><img src="../home/top_right.gif" width="7" height="7"></td>
              </tr>
              <tr> 
                <td width="7" align="left"><img src="../home/pixel.gif" width="1" height="198"></td>
                <td width="119"> 
                  <p align="center"><font color="#05508a" size="2" face="Arial, Helvetica, sans-serif"><img src="imatges/nenplorab.gif" width="77" height="154"></font></p></td>
                <td width="441">
                <% switch(br.getVar()){
                    case gali.beans.GaliCtt.VAR_ALG: %>
                        <p align="center" class="titoltema">Ep!<br>
                        <%if(!br.incomplete){%>
                            Has obtengut una puntuaci&oacute; del <%=br.getQualification()%>%,<br>
                            i el m&iacute;nim per superar la prova era del <%=br.getMinimumPassedQualification()%>%.
                        <%}
                        else {
                            if(br.isOk){%>
                                Anaves bé! Portaves una puntuaci&oacute; del <%=br.getQualification()%>%, però...<br>
                            <%}%>
                            No s&eacute;s arribat a la fi de la sessi&oacute;! Se deuen de fer totes les activitats d'una sessi&oacute; per donar-la per bona.					
                        <%}%>
                        </p>
                        <p align="center" class="be">Torna a provar!</p>
                        <p align="center" class="be"><a href="<%=br.getContinueURL()%>">[continuar]</a></p>                                
                    <% break;
                    case gali.beans.GaliCtt.VAR_OC:%>
                        <p align="center" class="titoltema">Ep!<br>
                        <%if(!br.incomplete){%>
                            Portaues ua puntuacion deth <%=br.getQualification()%>%,<br>
                            e eth minim a superar era pr&ograve;va &egrave;re deth <%=br.getMinimumPassedQualification()%>%.
                        <%}
                        else {
                            if(br.isOk){%>
                              Anaues ben! Portaues ua puntuacion deth <%=br.getQualification()%>%, m&egrave;s...<br>
                            <%}%>
                            Non as arribat ath finau dera session! Cau h&egrave;r totes es activitats d'ua session ent&agrave; dar-la per bona.
                        <%}%>
                        </p>
                        <p align="center" class="be">Torna'c a sajar!</p>
                        <p align="center" class="be"><a href="<%=br.getContinueURL()%>">[contunhar]</a></p>                                
                    <% break;
                    default:%>
                        <p align="center" class="titoltema">Ep!<br>
                        <%if(!br.incomplete){%>
                            Has obtingut una puntuaci&oacute; del <%=br.getQualification()%>%,<br>
                            i el m&iacute;nim per superar la prova era del <%=br.getMinimumPassedQualification()%>%.
                        <%}
                        else {
                            if(br.isOk){%>
                                Anaves bé! Portaves una puntuaci&oacute; del <%=br.getQualification()%>%, però...<br>
                            <%}%>
                            No has arribat al final de la sessió! Cal fer totes les activitats d'una sessió per donar-la per bona.					
                        <%}%>
                        </p>
                        <p align="center" class="be">Torna a intentar-ho!</p>
                        <p align="center" class="be"><a href="<%=br.getContinueURL()%>">[continuar]</a></p>                
                    <% break;
                }
                %>                        
                </td>
                <td width="7" align="right"><img src="../home/pixel.gif" width="1" height="198"></td>
              </tr>
              <tr> 
                <td height="7"><img src="../home/bottom_left.gif" width="7" height="7"></td>
                <td height="7" colspan="2" valign="bottom"><img src="../home/pixel.gif" width="560" height="1"></td>
                <td height="7"><img src="../home/bottom_right.gif" width="7" height="7"></td>
              </tr>
            </table></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="25" bgcolor="#0481bb"><img src="../descripcio/logo.gif" width="55" height="25"></td>
  </tr>
</table>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("UA-2006596-2");
pageTracker._trackPageview();
</script></body>
</html>
