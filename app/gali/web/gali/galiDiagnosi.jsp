<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bd" class="gali.beans.GaliDiagnosiBean" scope="request" /><%
if(!bd.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute; - Prova de nivell inicial</title>
<script language="JavaScript" src="http://clic.xtec.cat/dist/jclic/jclicplugin.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" height="100%">
  <tr>
    <td height="21"><table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=bd.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"> 
            <div align="right"><a href="../index.html"><img src="../home/gali.gif" width="68" height="21" border="0"></a></div></td>
        </tr>
      </table></td>
  </tr>
  <tr>
    <td height="100%" width="100%" bgcolor="#05508A" align="center">
<script language="JavaScript" type="text/javascript">
setLanguage('ca');
setExitUrl('<%=bd.getExitURL()%>');
setSkin('<%=bd.getPackageURL()%>gali.xml');
setReporter('<%=bd.getSetting("reporterName")%>','<%=bd.getReporterParams()%>');
writePlugin('<%=bd.getPackageURL()%>td.jclic.zip', '100%', '100%');
</script></td>
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
