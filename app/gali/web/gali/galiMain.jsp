<%@page contentType="text/html; charset=ISO-8859-1"%>
<jsp:useBean id="bm" class="gali.beans.GaliMainBean" scope="request" /><%
if(!bm.init(request, session, response)){%><jsp:forward page="error.html"/><%}
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>GAL&Iacute; - Pas 1</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../estils.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
  <tr>
    <td height="21">
      <table width="100%" height="21" border="0" cellpadding="0" cellspacing="0" bgcolor="#05508A">
        <tr> 
          <td width="102"><a href="http://www.edu365.cat/"><img src="../home/edu-365.gif" width="102" height="21" border="0" alt="edu365.cat"></a></td>
          <td class="cos"><div align="center"><font color="#FFFFFF"><%=bm.getLiteralUserId()%></font></div></td>
          <td width="68"> 
            <div align="right"><a href="../index.html"><img src="../home/gali.gif" width="68" height="21" border="0" alt="Inici"></a></div>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td valign="top"><table width="566" border="0" align="center" cellpadding="0" cellspacing="0">
        <tr> 
          <td colspan="3" height="30">&nbsp;</td>
        </tr>
        <tr> 
          <td height="120"><a href="gali.jsp"><img src="../descripcio/logo_petit.gif" width="132" height="120" border="0"></a></td>
          <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
          <td valign="bottom" class="titoltema">
<% if(bm.getVar()!=bm.VAR_ALG){ %> 
            <p>Fes un clic sobre el grup d'activitats pel qual vulguis comen&ccedil;ar:</p>
<% } else { %>
            <p>Fes un clic a damunt del grup d'activitats pel qual vulguis comen&ccedil;ar:</p>
<% } %>
          </td>
        </tr>
        <tr> 
          <td colspan="3" valign="top"> 
            <p>&nbsp;</p>
            <table width="560" border="0" align="left" cellpadding="0" cellspacing="2">
              <tr align="left"> 
<% if(bm.getVar()!=bm.VAR_ALG){ %> 
                <td width="60%" class="titoltemab">BLOCS TEM&Agrave;TICS DE CONTINGUT</td>
<% } else { %>
                <td width="60%" class="titoltemab">BLOCS TEM&Agrave;TICS DE CONTENGUT</td>
<% } %>
                <td width="20%" class="titoltemab" align="center">INICIACI&Oacute;</td>
                <td width="20%" class="titoltemab" align="center">APROFUNDIMENT</td>
              </tr>
              <tr align="left"> 
                <td height="10" colspan="3"><img src="../descripcio/pixel.gif" width="560" height="2"></td>
              </tr>
<% for(int iPage=0; iPage<bm.PAGES.length; iPage++){ //Es posa un enlla&ccedil; a totes les planes que formen el Gal&iacute; %>
              <tr align="left"> 
                <td width="60%" valign="middle" class="menunegre"><%= bm.getDescription(iPage).toUpperCase() %></td>
                <td width="20%" class="menunegre"> 
                  <div align="center"><a href="<%=bm.getUrl(bm.JSP_PAGE, iPage, -1, 0, false)%>"><img src="imatges/<%= bm.getIconUrl(iPage, bm.INI_LEVEL)%>" width="21" height="19" border="0" alt="Bloc <%=iPage+1%> - Iniciaci&oacute;"></a></div>
                </td>
                <td width="20%" valign="middle" class="menunegre"> 
                  <div align="center"><a href="<%=bm.getUrl(bm.JSP_PAGE, iPage, -1, 1, false)%>"><img src="imatges/<%= bm.getIconUrl(iPage, bm.PROF_LEVEL)%>" width="21" height="19" border="0" alt="Bloc <%=iPage+1%> - Aprofundiment"></a></div>
                </td>
              </tr>
<%   if(iPage<bm.PAGES.length-1){%>
              <tr align="left">
                <td height="5" colspan="3" valign="middle"><img src="../descripcio/pixel.gif" width="560" height="1"></td>
              </tr>
<%  }else{%>
              <tr align="left">
                <td height="5" colspan="3" valign="middle"><img src="../descripcio/pixel.gif" width="560" height="2"></td>
              </tr>
<%  }%>
<%}%>
            </table></td>
        </tr>
        <tr> 
          <td colspan="3" valign="top"> 
            <table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
			    <td align="left" class="cos" valign="top"> 
                  <p><b><a href="gali.jsp">[enrere]</a><br>
                    </b></p>
                </td>
			    <td align="right" class="cos" valign="top"> 
                  <p> 
                    <% if(!bm.isCons() && bm.getUserId()!=null){%>
                    <b><a href="promptClear.jsp">[esborrar dades]</a></b> 
                    <% } %>
                  </p>
            </td>
			</tr>
			</table>
          </td>
        </tr>
      </table></td>
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
