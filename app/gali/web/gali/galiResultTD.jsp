<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
if(!b.init(request, session, response)){%><jsp:forward page="error.html"/><%}
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
    <td height="21"> 
      <table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=b.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"><div align="right"><a href="../index.html"><img src="../home/gali.gif" width="68" height="21" border="0"></a></div></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td valign="top"> <table width="566" border="0" align="center" cellpadding="0" cellspacing="0">
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
          <td valign="top"> 
            <table width="566" height="200" border="0" cellpadding="0" cellspacing="0">
              <tr> 
                <td height="7"><img src="../home/top_left.gif" width="7" height="7"></td>
                <td height="7" valign="top"><img src="../home/pixel.gif" width="560" height="1"></td>
                <td height="7"><img src="../home/top_right.gif" width="7" height="7"></td>
              </tr>
              <tr> 
                <td width="7" align="left"><img src="../home/pixel.gif" width="1" height="198"></td>
                <td width="470"> 
                  <p align="center" class="be">Prova de nivell inicial acabada </p>
                  <p align="center" class="be"><a href="galiMain.jsp" class="link">[continuar]</a></p>
                </td>
                <td width="7" align="right"><img src="../home/pixel.gif" width="1" height="198"></td>
              </tr>
              <tr> 
                <td height="7"><img src="../home/bottom_left.gif" width="7" height="7"></td>
                <td height="7" valign="bottom"><img src="../home/pixel.gif" width="560" height="1"></td>
                <td height="7"><img src="../home/bottom_right.gif" width="7" height="7"></td>
              </tr>
            </table>
            <strong><font size="2" face="Arial, Helvetica, sans-serif"><br>
            </font></strong><strong><font size="2" face="Arial, Helvetica, sans-serif"> 
            </font></strong></td>
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
