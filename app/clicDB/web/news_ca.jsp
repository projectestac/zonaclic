<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.RSSBean" scope="request" /><%
if(!b.init(request, response, "ca")){%><jsp:forward page="error_ca.jsp"/><%}
String appBase=b.getAppBase();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"><html><!-- InstanceBegin template="/Templates/basic_ca.dwt" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" --> 
<title>zonaClic - Canal RSS de not&iacute;cies</title>
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../css/basic.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="../scripts/mm.js" type="text/JavaScript"></script>
<!-- InstanceBeginEditable name="head" -->
<link rel="alternate" title="zonaClic" href="<%=appBase%>/rss_ca.xml" type="application/rss+xml">
<link href="../css/news.css" rel="stylesheet" type="text/css">
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Language" content="ca">
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
          <param name="movie" value="../img/nav_ca.swf">
          <param name="quality" value="high">
          <param name="BGCOLOR" value="#CCFF99">
          <param name="BASE" value="/img">
          <embed src="../img/nav_ca.swf" width="758" height="80" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" bgcolor="#CCFF99" base="/img"></embed></object></td>
  </tr>
  <tr>
   <td><img name="f0_r2_c2" src="../img/f0_r2_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img name="f0_r2_c3" src="../img/f0_r2_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img name="f0_r2_c4" src="../img/f0_r2_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
      <td width="20" height="200" background="../img/f0_r3_c2.gif">&nbsp;</td>
   <td class="main" width="718" valign="top"><script language="JavaScript" src="../scripts/av_ca.js" type="text/JavaScript"></script><!-- InstanceBeginEditable name="zona0" --> 
        <p class="topMenu"><a href="../ca/index.htm">inici</a> | <a href="../ca/com/index.htm">comunitat</a> 
          | rss</p>
        <p class="t1">Canal RSS de not&iacute;cies</p>
        <p><a type="application/rss+xml" href="<%=appBase%>/rss_ca.xml"><img src="../img/xml.gif" alt="Canal RSS de not&iacute;cies de la zonaClic" border="0"></a></p>
        <p>El <a href="<%=appBase%>/rss_ca.xml">canal RSS</a> de la
          <strong>zonaClic</strong> ofereix informaci&oacute; actualitzada sobre 
          les novetats que es van produ&iuml;nt al web. Podeu consultar-les directament 
          des d'aquesta p&agrave;gina o mitjan&ccedil;ant qualsevol aplicaci&oacute; capa&ccedil; 
          de llegir  RSS. Hi ha dos tipus de lectors RSS: els que s'instal&middot;len a l'ordinador, com el <a href="http://www.mozilla.org/thunderbird" target="_blank" class="extern">Thunderbird</a> o el <a href="http://www.mozilla.org/products/firefox/" target="_blank" class="extern">Firefox</a>, i els serveis en l&iacute;nia com Google, Bloglines, NewsGator o MyYahoo!. Per afegir el canal <strong>zonaClic</strong> a un d'aquests serveis feu clic al seu bot&oacute;:</p>
        <p align="center"><a href="http://fusion.google.com/add?feedurl=<%=appBase%>/rss_ca.xml"><img src="../img/add-to-google-plus.gif" alt="Afegir la zonaClic al Google Reader" width="104" height="17" hspace="10" border="0" align="absmiddle"></a><a href="http://www.bloglines.com/sub/<%=appBase%>/rss_ca.xml"><img src="../img/add-to-bloglines.gif" alt="Afegir la zonaClic al Bloglines" width="76" height="17" hspace="10" border="0" align="absmiddle"></a><a href="http://www.newsgator.com/ngs/subscriber/subext.aspx?url=<%=appBase%>/rss_ca.xml"><img src="../img/newnewsgator_button.gif" alt="Afegir la zonaClic al NewsGator" width="91" height="17" hspace="10" border="0" align="absmiddle"></a><a href="http://add.my.yahoo.com/content?url=http%3A//clic.xtec.cat/db/rss_ca.xml"><img src="../img/addtomyyahoo.gif" alt="Afegir la zonaClic al My Yahoo!" width="91" height="17" hspace="10" border="0" align="absmiddle"></a></p>
        <% if(b.rss==null || b.rss.length==0){
%>
        <p>No hi ha cap notícia!</p>
        <%} else {
    for(int i=0; i<b.rss.length; i++){
      edu.xtec.web.clic.WRSS wrss=b.rss[i];
      if(b.rss[i].descripcions==null || b.rss[i].descripcions.length==0){
%>
        <p>Notícia buida</p>
        <%    } else{
        edu.xtec.web.clic.WRSSDesc wrssDesc=b.rss[i].descripcions[0];
%>
        <div class="newsBlock"><a name="<%=wrss.id%>" id="<%=wrss.id%>"></a> 
          <p class="newsTitle"><span class="newsData"><%=b.dateFormat.format(wrss.data_pub)%></span><%=wrssDesc.title%></p>
          <p class="newsLink"><a href="<%=wrssDesc.link%>"><%=wrssDesc.link%></a></p>
          <p><%=wrssDesc.description%></p>
        </div>
        <%
      }      
%>
        <%  }
}%>
        <!-- InstanceEndEditable --><!-- InstanceBeginEditable name="addThis" --><hr style="margin-top: 40px;"><!-- AddThis Button BEGIN -->
<div class="addthis_toolbox addthis_default_style">
<a href="http://www.addthis.com/bookmark.php?v=250&username=clic" class="addthis_button_compact">Comparteix-ho</a>
<span class="addthis_separator">|</span>
<a title="Envia a Facebook" class="addthis_button_facebook"></a>
<a title="Envia a Twitter" class="addthis_button_twitter"></a>
<a title="Envia a Delicious" class="addthis_button_delicious"></a>
<a title="Afegeix als favorits" class="addthis_button_favorites"></a>
<a title="Envia per correu mitjançant Gmail" class="addthis_button_gmail"></a>
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
	<td align="right"><!-- InstanceBeginEditable name="postPeuDret" --><!-- InstanceEndEditable --></td>
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
