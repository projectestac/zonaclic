<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
if(!b.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Gal&iacute; en l&iacute;nia</title>
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
          <td width="68" height="21"> 
            <div align="right"><a href="../index.html"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Inici"></a></div>
          </td>
        </tr>
      </table>
	 </td>
  </tr>
  <tr>
    <td valign="top"> <table width="566" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td valign="top"> 
            <table width="566" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td colspan="3" height="30">&nbsp;</td>
              </tr>
              <tr> 
                <td colspan="3" valign="top" class="cos"> 
                  <p class="cospetit"><a href="../index.html"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a></p>
                  <p>Gal&iacute; aufer&iacute;s dues modalitats de trabalh: amiat o liure. Ena <a href="../descripcio/descripcio.html#mode">descripcion</a>
                      deth programa s'expliquen damb m&egrave;s detall 
                      es caracteristiques de cada modalitat.</p>
                  <p class="cospetit">&nbsp;</p>
                  </td>
              </tr>
              <tr> 
                <td valign="top" align="left" width="45%" class="cos"> 
                  <p><span class="titoltema">Aprenentatge amiat</span><br>
                    Sonque ent&agrave; usuaris registradi der XTEC o XTECBlocs. 
                    Eth sist&egrave;ma rebrembe eth resultat des exercicis. Er objectiu 
                    ei superar toti es moduls.</p>
                  <p class="cospetit">&nbsp;</p>
                  </td>
                <td width="10%">&nbsp;</td>
                <td valign="top" align="right" width="45%" class="cos"> 
                  <p><span class="titoltema">Aprenentatge liure</span><br>
                    Ent&agrave; practicar e navegar pes exercicis.<br>
                    Eth sist&egrave;ma non enregistre es resultats obtengudi.</p>
                  <p class="cospetit">&nbsp;</p>
                  </td>
              </tr>
              <tr> 
                <td align="left" valign="top"> 
                  <p><a href="<%=b.getServerBaseUrl()%>/galiGuided.jsp"><img src="imatges/guiat-oc.gif" width="190" height="98" border="0" alt="Aprenentatge amiat"></a></p>
                </td>
                <td>&nbsp;</td>
                <td align="right" valign="top"> <a href="<%=b.getServerBaseUrl()%>/galiMain.jsp?cons=si"><img src="imatges/lliure-oc.gif" width="190" height="98" border="0" alt="Aprenentatge liure"></a></td>
              </tr>
            </table>            
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
