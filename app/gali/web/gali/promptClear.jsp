<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
if(!b.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute;</title>
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21"><table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=b.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"><div align="right"><a href="galiMain.jsp"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Men&uacute; principal"></a></div></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top"> 
      <table width="566" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td height="30">&nbsp;</td>
        </tr>
        <tr> 
          <td height="120"><a href="gali.jsp"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a></td>
        </tr>
        <tr> 
          <td height="5"> 
            <p>&nbsp;</p>
          </td>
        </tr>
        <tr> 
          <td valign="top" class="cos"> 
            <p class="titoltema">Atenci&oacute;!</p>
            <p>Aquesta opci&oacute; eliminar&agrave; de la base de <%if(b.getVar()!=b.VAR_ALG){%>dades<%}else{%>dats<%}%> de resultats 
              del Gal&iacute; tota la informaci&oacute; corresponent a <b><%=b.getUserId()%></b>. 
              <%if(b.getVar()!=b.VAR_ALG){%>Ser&agrave;<%}else{%>Siguer&agrave;<%}%> com si comencessis des de zero.</p>
            <p></p>
            <p class="titoltema">Vols esborrar <%if(b.getVar()!=b.VAR_ALG){%>les dades<%}else{%>els dats<%}%>?</p>
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="150"><a href="deleteData.jsp"><img src="imatges/vols_si.gif" width="84" height="50" border="0" alt="Si, vull esborrar <%if(b.getVar()!=b.VAR_ALG){%>les dades<%}else{%>els dats<%}%>."></a></td>
                <td width="150"><a href="galiMain.jsp"><img src="imatges/vols_no.gif" width="84" height="50" border="0" alt="No, no!"></a></td>
              </tr>
            </table>
            <p>&nbsp;</p>
            <p><b><strong><font size="2" face="Arial, Helvetica, sans-serif"> 
              <img src="../descripcio/imatges/pixel.gif" width="1" height="1"></font></strong><a href="galiMain.jsp">[enrere]</a><strong><font size="2" face="Arial, Helvetica, sans-serif"><br>
              <img src="../descripcio/imatges/pixel.gif" width="1" height="1"> 
              </font></strong></b></p>
          </td>
        </tr>
      </table> 
      </td>
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
