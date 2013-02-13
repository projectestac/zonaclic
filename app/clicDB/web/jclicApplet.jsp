<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.AppletBean" scope="request" /><%
if(!b.init(request, response, null)){%><jsp:forward page="error_en.jsp"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>JClic<%=b.title%></title>
<script language="JavaScript" SRC="<%=b.getCodeBase()%>/jclicplugin.js" type="text/javascript"></script>
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="window.focus();">
<script language="JavaScript">
<!--
setJarBase('<%=b.getCodeBase()%>');
setLanguage('<%=b.requestedLang%>');
<%if(b.skin!=null){%>
setSkin('<%=b.skin%>');<%}
if(b.reporter!=null){%>
setReporter('<%=b.reporterClass%>','<%=b.reporter%>');<%}
if(b.exitUrl!=null){%>
setExitUrl('<%=b.exitUrl%>');<%}
if(b.infoUrlFrame!=null){%>
setInfoUrlFrame('<%=b.infoUrlFrame%>');<%}
if(b.sequence!=null){%>
setSequence('<%=b.sequence%>');<%}
if(b.systemSounds!=null){%>
setSystemSounds('<%=b.systemSounds%>');<%}
if(b.compressImages!=null){%>
setCompressImages('<%=b.compressImages%>');<%}%>
writePlugin('<%=b.project%>', '100%', '100%');
-->
</script><script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("UA-2006596-1");
pageTracker._trackPageview();
</script></body>
</html>
