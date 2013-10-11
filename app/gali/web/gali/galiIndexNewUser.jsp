<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="b" class="gali.beans.GaliDummyBean" scope="request" /><%
if(!b.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute; - Test inicial</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF" height="100%">
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
              <% String icoSufix="";
                 String enrere = "enrere";
                 switch(b.getVar()){
                    case gali.beans.GaliCtt.VAR_ALG:%>
            <p class="titoltema">Salut <b><%=b.getUserId()%></b>!</p>
            <p>Aquesta &eacute;s la primera volta que entres al Gal&iacute; en 
                la modalitat d'aprendiment guiat.</p>
            <p>Tens l'opci&oacute; de fer una prova de nivell inicial. Los 
                resultats d'aquesta prova te permetiran de no fer les sessions 
                correspondents als temes que ja coneixes, per fer les activitats 
                que resultaran m&eacute;s &uacute;tils per millorar el tou 
                nivell de llengua catalana.</p>                    
            <p class="titoltema">Vols fer la prova de nivell inicial?</p>            
                    <% break;
                    case gali.beans.GaliCtt.VAR_OC:
                        icoSufix="-oc";
                        enrere="endarrèr";%>
            <p class="titoltema">&Ograve;la <b><%=b.getUserId()%></b>!</p>
            <p>Aquest ei eth prum&egrave;r viatge qu'accedisses ath Gal&iacute; ena modalitat 
               d'aprenentatge amiat.</p>
            <p>As era opcion de h&egrave;r ua pr&ograve;va de niv&egrave;u 
               iniciau. Es resultats d'aguesta pr&ograve;va permeter&agrave;n 
               estauviar-te es sessions corresponentes as t&egrave;mes que 
               ja coneixes ent&agrave; centrar-te enes activitats que resultar&agrave;n 
               m&egrave;s utiles ent&agrave; melhorar eth t&ograve;n niv&egrave;u 
               de lengua aranesa.</p>                    
            <p class="titoltema">V&ograve;s h&egrave;r era pr&ograve;va de niv&egrave;u iniciau?</p>                        
                    <% break;
                    default:%>
            <p class="titoltema">Hola <b><%=b.getUserId()%></b>!</p>
            <p>Aquesta &eacute;s la primera vegada que accedeixes al Gal&iacute; 
              en la modalitat d'aprenentatge guiat.</p>
            <p>Tens l'opci&oacute; de fer una prova de nivell inicial. Els resultats 
              d'aquesta prova permetran estalviar-te les sessions corresponents 
              als temes que ja coneixes, per centrar-te en les activitats que 
              resultaran m&eacute;s &uacute;tils per millorar el teu nivell de 
              llengua catalana.</p>                    
            <p class="titoltema">Vols fer la prova de nivell inicial?</p>            
                    <% break;
                 }%>
              
                          
            <table width="300" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td width="150"><a href="galiDiagnosi.jsp"><img src="imatges/vols_si<%= icoSufix %>.gif" width="84" height="50" border="0" alt="Si, fem la prova!"></a></td>
                <td width="150"><a href="<%=b.getBaseUrl(false)%>"><img src="imatges/vols_no<%= icoSufix %>.gif" width="84" height="50" border="0" alt="No, prefereixo m&eacute;s comen&ccedil;ar de zero"></a></td>
              </tr>
            </table>
            <p class="cospetit">&nbsp;</p>
            <p><b><strong><font size="2" face="Arial, Helvetica, sans-serif"> 
              <img src="../descripcio/imatges/pixel.gif" width="1" height="1"></font></strong><a href="gali.jsp">[<%= enrere %>]</a><strong><font size="2" face="Arial, Helvetica, sans-serif"><br>
              <img src="../descripcio/imatges/pixel.gif" width="1" height="1"> 
              </font></strong></b></p>
          </td>
        </tr>
      </table> 
      <div align="center"></div>
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
