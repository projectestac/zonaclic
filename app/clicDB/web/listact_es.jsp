<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.ListBean" scope="request" /><%
if(!b.init(request, response, "es")){%><jsp:forward page="error_es.jsp"/><%}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><!-- InstanceBegin template="/Templates/basic_es.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" --> 
<title>zonaClic - Buscar actividades</title>
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../css/basic.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="../scripts/mm.js" type="text/JavaScript"></script>
<!-- InstanceBeginEditable name="head" -->
<script language="JavaScript" type="text/javascript">
<!--
function setOrder(col, desc){
  document.mainForm.ordre.value=col;
  document.mainForm.desc.value=desc;
  document.mainForm.submit();
}
function goTo(n){
  document.mainForm.from.value=n;
  document.mainForm.submit();
}
// -->
</script>
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Language" content="es">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<meta name="keywords" content="clic jclic education educational software educació educación programari educatiu software educativo">
</head>
<body>
<div align="center"> 
 <table border="0" width="758" cellpadding="0" cellspacing="0">
  <tr>
   <td><img src="../img/spacer.gif" width="20" height="1" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="718" height="1" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="20" height="1" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="1" border="0" alt=""></td>
  </tr>
  <tr>
   <td colspan="4"><object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,29,0" width="758" height="80">
          <param name="movie" value="../img/nav_es.swf">
          <param name="quality" value="high">
          <param name="BGCOLOR" value="#CCFF99">
          <param name="BASE" value="/img">
          <embed src="../img/nav_es.swf" width="758" height="80" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" bgcolor="#CCFF99" base="/img"></embed></object></td>
  </tr>
  <tr>
   <td><img name="f0_r2_c2" src="../img/f0_r2_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img name="f0_r2_c3" src="../img/f0_r2_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img name="f0_r2_c4" src="../img/f0_r2_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
      <td width="20" height="200" background="../img/f0_r3_c2.gif">&nbsp;</td>
   <td class="main" width="718" valign="top"><script language="JavaScript" src="../scripts/av_es.js" type="text/JavaScript"></script><!-- InstanceBeginEditable name="zona0" --> 
<% if(System.currentTimeMillis()<1187452800000L){ %>
<p class="avis"><span class="nou"><strong>&iexcl;Atenci&oacute;n!</strong></span><br>
  El <strong>s&aacute;bado 18 de agosto</strong>, entre las 9 y las 17h, la zonaClic podr&aacute; sufrir interrupciones del servicio debido a operaciones de mantenimiento de la Red telem&aacute;tica Educativa de Catalu&ntilde;a (XTEC). Disculpad las molestias.</p>
<% }%>      
        <div class="topMenu"><a href="../es/index.htm">inicio</a> | <a href="../es/act/index.htm"><strong>actividades</strong></a> 
          | b&uacute;squeda</div>
		  
        <p class="t1">Buscar actividades</p>		  	  
          <form action="listact_es.jsp" method="get" name="mainForm">			
            <input type="hidden" name="lang" value="<%=b.lang%>">
            <input type="hidden" name="ordre" value="<%=b.ordre%>">
            <input type="hidden" name="desc" value="<%=b.desc ? 1 : 0%>">
            <input type="hidden" name="from" value="1">
          <table border="0">
                <tr> 
                  
              <td align="right">Area:</td>		  
			<td><select name="area">
<option value="*" <%=b.area.equals("*") ? "SELECTED": ""%>><%=b.getMsg("all_areas")%></option>
<% for(int i=0; i<b.arees.length; i++){
%><option value="<%=b.arees[i][0]%>" <%=b.area.equals(b.arees[i][0]) ? "selected" : ""%>><%=b.arees[i][1]%></option>
<%}
%></select></td>
              <td width="50%" rowspan="9" class="blocLateralMenu">
<p>Indique los criterios de b&uacute;squeda y haga clic en el bot&oacute;n <a href="javascript:document.forms[0].submit()"><strong>buscar</strong></a> 
                  para obtener la lista de resultados.</p>
                <p>Los resultados se ordenan inicialmente por fecha de publicaci&oacute;n. 
                  Haga clic en los t&iacute;tulos de las columnas de la tabla 
                  para indicar otros criterios de ordenaci&oacute;n .</p>
                </td>
          </tr>
          <tr> 
                  <td align="right">Idioma:</td>
			<td><select name="idioma">
			<option value="*" <%=b.idioma.equals("*") ? "selected" : "" %>><%=b.getMsg("all_languages")%></option>
<% for(int i=0; i<b.idiomes.length; i++){
%><option value="<%=b.idiomes[i][0]%>" <%=b.idioma.equals(b.idiomes[i][0]) ? "selected" : ""%>><%=b.idiomes[i][1]%></option>
<%}
%></select>
</td>
          </tr>
          <tr> 
            <td></td>
<td><input type="checkbox" name="ctm" value="1" <%=b.ctm ? "checked" : "" %>>
                Incluir actividades con contenido textual m&iacute;nimo</td>
          </tr>
          <tr> 
                  
              <td align="right">Nivel:</td>
			<td><select name="nivell">
<option value="*" <%=b.nivell.equals("*") ? "SELECTED": ""%>><%=b.getMsg("all_nivells")%></option>
<% for(int i=0; i<b.nivells.length; i++){
%><option value="<%=b.nivells[i][0]%>" <%=b.nivell.equals(b.nivells[i][0]) ? "selected" : ""%>><%=b.nivells[i][1]%></option>
<%}
%></select>
</td>		  
          </tr>
          <tr> 
                  
              <td align="right">T&iacute;tulo:</td>
			<td><input type="text" size="20" name="text_titol" value="<%=b.str(b.textTitolStr)%>"></td>
          </tr>
          <tr> 
            <td align="right">Autor/a:</td>
			<td><input type="text" size="20" name="text_aut" value="<%=b.str(b.textAutStr)%>"></td>			
          </tr>
          <tr> 
                  
              <td align="right">Descripci&oacute;n:</td>			
			<td><input type="text" size="20" name="text_desc" value="<%=b.str(b.textDescStr)%>"></td>
          </tr>
          <tr> 
                  
              <td align="right">Mostrar:</td>
			<td><input type="text" size="3" name="num" value="<%=b.maxRegs%>">
                resultados por p&aacute;gina</td>			
          </tr>
          <tr> 
            <td></td>
            <td><input type="submit" VALUE="buscar..."></td>
          </tr>
        </table>
      </form>
          <p><%=b.queryFoundDesc%><br>
<%=b.queryPageDesc%></p>
        <table border="0" width="100%" cellspacing="0">
          <tr> 
          <td colspan="5" align="center"> 
<%if(b.navigator!=null){
    if(b.navPrev>=0){%>
                  <a href="javascript:goTo(<%=b.navPrev%>)"><img src="../img/prev.gif" width="7" height="11" hspace="5" border="0"></a> 
                  <%}
    for(int i=0; i<b.navigator.length; i++){
      if(i!=b.currentPage){%><a href="javascript:goTo(<%=b.navigator[i]*b.maxRegs%>)"><%=b.navigator[i]+1%></a> <%}
      else {%><b><%=b.navigator[i]+1%></b> <%}
    }
    if(b.navNext>=0){%>
                  <a href="javascript:goTo(<%=b.navNext%>)"><img src="../img/next.gif" width="7" height="11" hspace="5" border="0"></a>
<%}
}%>
		  </td>
        </tr>
          <tr> 
            <% for(int i=0; i<b.NUM_COLS; i++){
boolean a=(b.ordre==i);
int vd = a&&b.desc ? 0 : 1;
String mosca="";
if(a)
  mosca="<img src=\"../img/order_"+(b.desc ? "up" : "down")+".gif\" border=\"0\">";
%>
            <td class="taulaCap"><b>&nbsp;<a href="javascript:setOrder(<%=i%>,<%=vd%>)"><%=mosca%><%=b.getMsg("col_"+i)%></a></b></td>
<%}
%>
</tr>
<% for(int i=0; i<b.data.length; i++){
String[] s=b.data[i];
%><tr class="<%=(i&1)==0 ? "taulaSenar" : "taulaParell"%>"><td>&nbsp;<%=s[b.COL_DATA]%></td><td>&nbsp;<a href="<%=s[b.NUM_COLS]%>"><b><%=s[b.COL_TITOL]%></b></a></td><td>&nbsp;<%=s[b.COL_IDIOMES]%></td><td>&nbsp;<%=s[b.COL_AREES]%></td><td>&nbsp;<%=s[b.COL_NIVELLS]%></td></tr>
<%}%>
        <tr> 
          <td colspan="5" align="center"> 
<%if(b.navigator!=null){
    if(b.navPrev>=0){%>
                  <a href="javascript:goTo(<%=b.navPrev%>)"><img src="../img/prev.gif" width="7" height="11" hspace="5" border="0"></a> 
                  <%}
    for(int i=0; i<b.navigator.length; i++){
      if(i!=b.currentPage){%><a href="javascript:goTo(<%=b.navigator[i]*b.maxRegs%>)"><%=b.navigator[i]+1%></a> <%}
      else {%><b><%=b.navigator[i]+1%></b> <%}
    }
    if(b.navNext>=0){%>
                  <a href="javascript:goTo(<%=b.navNext%>)"><img src="../img/next.gif" width="7" height="11" hspace="5" border="0"></a>
<%}
}%>
		  </td>
        </tr>
      </table>
        <!-- InstanceEndEditable --><!-- InstanceBeginEditable name="addThis" --><hr style="margin-top: 40px;"><!-- AddThis Button BEGIN -->
<div class="addthis_toolbox addthis_default_style">
<a href="http://www.addthis.com/bookmark.php?v=250&username=clic" class="addthis_button_compact">Compartir</a>
<span class="addthis_separator">|</span>
<a title="Enviar a Facebook" class="addthis_button_facebook"></a>
<a title="Enviar a Twitter" class="addthis_button_twitter"></a>
<a title="Enviar a Delicious" class="addthis_button_delicious"></a>
<a title="Añadir a favoritos" class="addthis_button_favorites"></a>
<a title="Enviar por correo mediante Gmail" class="addthis_button_gmail"></a>
</div>
<script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=clic"></script>
<!-- AddThis Button END --><!-- InstanceEndEditable --></td>
   <td width="20" background="../img/f0_r3_c4.gif">&nbsp;</td>
   <td width="1" background="../img/spacer.gif"><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img name="f0_r4_c2" src="../img/f0_r4_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img name="f0_r4_c3" src="../img/f0_r4_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img name="f0_r4_c4" src="../img/f0_r4_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <!-- InstanceBeginEditable name="zonaExtra" --><!-- InstanceEndEditable -->
  <tr>
   <td colspan="4" bgcolor="#99CCCC" class="peu">
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
	<td align="left"><!-- InstanceBeginEditable name="postPeuEsquerre" --><!-- InstanceEndEditable --></td>
	<td align="center">
	<a href="http://www.gencat.cat/educacio/"><img src="../img/depeducacio.gif" alt="Generalitat de Catalunya - Departament d'Educaci&oacute;" width="130" height="23" border="0"></a>
	<img src="../img/spacer.gif" width="100" height="1" border="0" alt="">
    <a href="http://www.xtec.cat/"><img src="../img/xtec.gif" alt="Xarxa Telem&agrave;tica Educativa de Catalunya" width="178" height="23" border="0"></a>
          <!-- InstanceBeginEditable name="postPeu" --><!-- InstanceEndEditable -->
    </td>
	<td align="right"><!-- InstanceBeginEditable name="postDret" --><!-- InstanceEndEditable --></td>
     </tr>
     </table>
   </td>
  </tr>
 </table>
</div>
<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("UA-2006596-1");
pageTracker._trackPageview();
</script></body>
<!-- InstanceEnd --></html>
