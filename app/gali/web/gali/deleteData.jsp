<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bdd" class="gali.beans.GaliDeleteDataBean" scope="request" /><%
if(!bdd.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute; - Dades esborrades</title>
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21"><table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=bdd.getLiteralUserId()%></font></div></td>
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
          <td class="cos"> 
            <p>&nbsp;</p>
            <p>La informaci&oacute; corresponent a <b><%=bdd.getUserId()%></b> 
              <%if(bdd.getVar()!=bdd.VAR_ALG){%>ha estat eliminada de la base de dades<%}else{%>és estada eliminada de la base de dats<%}%>.</p>
            <p>&nbsp;</p>
            <p><b>[<a href="gali.jsp">continuar</a>]</b></p>
            <!--p>&nbsp;</p-->
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
