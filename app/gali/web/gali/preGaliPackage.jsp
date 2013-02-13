<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bpk" class="gali.beans.GaliPackageBean" scope="request" /><%
if(!bpk.init(request, session, response)){%><jsp:forward page="error.html"/><%}
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
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=bpk.getLiteralUserId()%></font></div></td>
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
                  <p class="cospetit"><a href="../index.html"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a><br>
                  </p>
                  <p>El Gal&iacute; ofereix dues maneres diferents de descarregar 
                    els continguts multim&egrave;dia de les activitats. A la <a href="../descripcio/descripcio.html#download">descripci&oacute;</a> 
                    del programa s'expliquen amb detall les caracter&iacute;stiques 
                    de cada modalitat.</p>
                  <p class="cospetit">&nbsp;</p>
                </td>
              </tr>
              <tr> 
                <td valign="top" align="left" width="45%"> 
                  <p class="cos"><span class="titoltema">Descarregar les imatges 
                    i els sons quan es necessitin</span><br>
                    Opci&oacute; recomanada si us connecteu a Internet mitjan&ccedil;ant 
                    un m&ograve;dem.</p>
                  <p class="cospetit">&nbsp;</p>
                  </td>
                <td width="10%">&nbsp;</td>
                <td valign="top" align="right" width="45%"> 
                  <p class="cos"><span class="titoltema">Descarregar tots<br>
                    els fitxers a l'inici</span><br>
                    Opci&oacute; recomanada per a connexions<br>
                    de tipus ADSL i cable.</p>
                  <p class="cospetit">&nbsp;</p>
                  </td>
              </tr>
              <tr> 
                <td align="left" valign="top"> 
                  <p><a href="<%=bpk.getUrl("galiPackage.jsp")%>&light=2"><img src="imatges/guiat.gif" width="190" height="98" border="0" alt="Descarregar els fitxers quan es necessitin"></a></p>
                </td>
                <td>&nbsp;</td>
                <td align="right" valign="top"> 
                  <p><a href="<%=bpk.getUrl("galiPackage.jsp")%>&light=1"><img src="imatges/lliure.gif" width="190" height="98" border="0" alt="Descarregar-ho tot a l'inici"></a></p>
                </td>
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
