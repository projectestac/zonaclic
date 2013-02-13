<%@page contentType="text/html; charset=iso-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
if(!b.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Gal&iacute; - Selecci&oacute; de variant dialectal</title>
<link href="../estils.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"></head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21"> 
      <table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102" height="21"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=b.getLiteralUserId()%></font></div></td>
          <td width="68" height="21"> 
            <div align="right"><a href="../index.html"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Inici"></a></div>
          </td>
        </tr>
      </table>
	 </td>
  </tr>
  <tr>
    <td valign="top"> <table width="657" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td valign="top"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td colspan="2" height="30">&nbsp;</td>
              </tr>
              <tr> 
                <td colspan="2" valign="top" class="cos"> 
                  <p class="cospetit"><a href="../index.html"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a></p>
                  <p>El catal&agrave;, com moltes altres lleng&uuml;es, t&eacute; diverses
                    variants dialectals que presenten algunes difer&egrave;ncies<br>
                    fon&egrave;tiques
                    i ortogr&agrave;fiques. Els exercicis del
                  Gal&iacute; es poden fer en aquestes variants:</p>
                  <p>&nbsp;</p></td>
              </tr>
              <tr> 
			    <td width="12%" align="left" valign="middle"><p><a href="<%=b.getServerBaseUrl()%>/gali.jsp?var=or"><img src="imatges/selecciona.gif" alt="Variant central" width="68" height="45" vspace="5" border="0"></a></p>
		        </td>
                <td valign="middle" align="left" class="cos"> 
                  <p><span class="titoltema">Central</span><br>
&Eacute;s la variant parlada a les comarques de Girona, Barcelona i Tarragona.</p>
                </td>
			 </tr>
			 <tr>
			    <td align="left" valign="middle"><p><a href="<%=b.getServerBaseUrl()%>/gali.jsp?var=noc"><img src="imatges/selecciona.gif" alt="Variant nord-occidental" width="68" height="45" vspace="5" border="0"></a></p>
		        </td>
                <td valign="middle" width="88%" class="cos"> 
                  <p><span class="titoltema">Nord-occidental</span><br>
&Eacute;s la variant parlada a les comarques de Lleida, a les Terres de l'Ebre<span class="cos"> i
a la Franja Oriental d'Arag&oacute;.</span></p>
                </td>
              </tr>
			 <tr>
			    <td align="left" valign="middle"><p><a href="<%=b.getServerBaseUrl()%>/galiAlg.jsp?var=alg"><img src="imatges/selecciona.gif" alt="Variant algueresa" width="68" height="45" vspace="5" border="0"></a></p>
		        </td>
                <td valign="middle" width="88%" class="cos"> 
                  <p><span class="titoltema">Alguer&egrave;s</span><br>
&Eacute;s la variant parlada a la ciutat de l'Alguer, a l'illa de Sardenya<span class="cos">.</span></p>
                </td>
              </tr>
            </table>            
			<p>&nbsp;</p>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td bgcolor="#0481bb" height="25"><img src="../descripcio/logo.gif" width="55" height="25" border="0"></td>
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
