<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bpg" class="gali.beans.GaliPageBean" scope="request" /><%
if(!bpg.init(request, session, response)){%><jsp:forward page="error.html"/><%}
String enrere = (bpg.getVar() == gali.beans.GaliCtt.VAR_OC) ? "endarr&egrave;r" : "enrere";
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute; - Sessions</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21"> 
      <table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=bpg.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"> 
            <div align="right"><a href="galiMain.jsp"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Blocs tem&agrave;tics"></a></div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td valign="top"> 
	<table width="600" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td height="10">&nbsp;</td>
        </tr>
        <tr> 
          <td height="120"><table width="132" height="120" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td height="7"><img src="../home/top_left.gif" width="7" height="7"></td>
                <td height="7" valign="top"><img src="../home/pixel.gif" width="118" height="1"></td>
                <td height="7"><img src="../home/top_right.gif" width="7" height="7"></td>
              </tr>
              <tr> 
                <td width="7" align="left"><img src="../home/pixel.gif" width="1" height="106"></td>
                <td valign="bottom"> <p><img src="imatges/<%=bpg.getIconUrl()%>" width="116" height="103"></p></td>
                <td width="7" align="right"><img src="../home/pixel.gif" width="1" height="106"></td>
              </tr>
              <tr> 
                <td height="7"><img src="../home/bottom_left.gif" width="7" height="7"></td>
                <td height="7" valign="bottom"><img src="../home/pixel.gif" width="118" height="1"></td>
                <td height="7"><img src="../home/bottom_right.gif" width="7" height="7"></td>
              </tr>
            </table></td>
        </tr>
        <tr> 
          <td height="15"><p>&nbsp;</p></td>
        </tr>
        <tr> 
<td align="left" valign="top">
<p class="titoltema"><%=bpg.getPageDescription().toUpperCase()%></p>
<table width="600" border="0" cellpadding="0" cellspacing="2">
<tr align="left">
<td colspan="2" valign="middle"><div align="left"><img src="../descripcio/pixel.gif" width="600" height="2"></div></td>
</tr>
<% for(int iPackage=0; iPackage<bpg.getPackagesLength(); iPackage++){ //Es posa un enllac a tots els paquets d'aquesta plana %>
<tr align="left" valign="middle"> 
<td class="menunegre" width="90%"><%=bpg.getDescription(iPackage).toUpperCase()%></td>
<td align="center"><a href="<%=bpg.getUrl("galiPackage.jsp", -1, iPackage, -1, false)%>"><img src="<%=bpg.getIconUrl(iPackage)%>" width="21" height="19" border="0" alt="<%=bpg.getDescription(iPackage)%>"></a></td>
</tr>
  <% if(iPackage<(bpg.getPackagesLength()-1)){%>
<tr align="left"> 
<td height="2" colspan="2" valign="middle"><img src="../descripcio/pixel.gif" width="600" height="1"></td>
</tr>
  <%}%>
<%}%>        
<tr align="left"> 
<td height="2" colspan="2" valign="middle"><img src="../descripcio/pixel.gif" width="600" height="2"></td>
</tr>
</table>
            <p class="cos"><a href="galiMain.jsp"><b>[<%= enrere %>]</b></a></p>
          </td>
        </tr>
      </table> 
      <div align="center">
        <p>&nbsp;</p>
        </div>
      <p align="center">&nbsp;</p>
      </td>
  </tr>
  <tr>
    <td height="25" bgcolor="#0481bb"><img src="../descripcio/logo.gif" width="55" height="25" border="0"></td>
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
