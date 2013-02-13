<%@page session="false" contentType="text/html; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.ActBean" scope="request" /><%
if(!b.init(request, response, "ca")){%><jsp:forward page="error_ca.jsp"/><%}
int img=0;
String srvBase=b.getServerBase();
%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><!-- InstanceBegin template="/Templates/act_ca.dwt.jsp" codeOutsideHTMLIsLocked="false" -->
<head>
<!-- InstanceBeginEditable name="doctitle" --> 
<title><%=b.getMsg("pre_title")%> <%=b.getTitle()%></title>
<!-- InstanceEndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="../css/basic.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="../scripts/mm.js" type="text/JavaScript"></script>
 
<meta name="Keywords" content="<%=b.getMsg("common_keywords")%>, <%=b.getDescriptors()%>">
<link href="../css/act.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=srvBase%>/dist/jclic/launchApplet.js" type="text/javascript"></script>
<!-- InstanceBeginEditable name="head" -->
<style>
.container{ position:relative; width:300px; float:right; }
.play{position:absolute; top:40%; left:40%; width:63px; height:63px;}
.play img{border:none; opacity:0.4; filter: alpha(opacity=40); -moz-opacity:0.4; -khtml-opacity: 0.4;}
.play img:hover{border: none; opacity:0.8; filter: alpha(opacity=80); -moz-opacity:0.8;  -khtml-opacity: 0.8;}
</style>
<!-- InstanceEndEditable -->
<script language="JavaScript" type="text/JavaScript">
<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
 
<meta http-equiv="Content-Language" content="ca">
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<meta name="keywords" content="clic jclic education educational software educació educación programari educatiu software educativo">
</head>
<body onLoad="MM_preloadImages('../img/info_on.gif')">
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
   <td class="main" width="718" valign="top"><script language="JavaScript" src="../scripts/av_ca.js" type="text/JavaScript"></script>
         <div class="topMenu"><a href="../ca/index.htm">inici</a> | <a href="../ca/act/index.htm">activitats</a> 
          | <a href="listact_ca.jsp"><strong>cerca</strong></a> </div>
   <div class="titol"><!-- InstanceBeginEditable name="titol" --><%=b.getTitle()%><!-- InstanceEndEditable --></div>
   <div class="autors">
<% 
    int ic=0;
	String rol="aut";
	for(int i=0; i<b.act.autors.length; i++, ic++){
	 edu.xtec.web.clic.WAutorsAct aa=b.act.autors[i];
%>
     <!-- InstanceBeginEditable name="autor" -->
<%   if(!rol.equals(aa.rolId)){%><br><%=aa.rol%>: <%
      ic=0;
	  rol=aa.rolId;
	 }
	 boolean first=(ic==0);
	 boolean last=(i==b.act.autors.length-1) || !rol.equals(b.act.autors[i+1].rolId);
	 if(last && !first){%> <%=b.getMsg("and")%> <%}
	 else if(!first){%>, <%}
     %><%=aa.autor.getNom()%><%
     if(aa.autor.mail!=null){%> <a href="javascript:mailto('<%=b.em(aa.autor.mail)%>')"><img src="../img/mail.gif" border="0" alt="<%=b.getMsg("mailto")%> <%=aa.autor.getNom()%>" title="<%=b.getMsg("mailto")%> <%=aa.autor.getNom()%>"></a><%}
%>
<!-- InstanceEndEditable -->
<%  }%>			
   </div>
   <!-- InstanceBeginEditable name="img" -->
<% 
   edu.xtec.web.clic.WVersions vv=b.getFirstJClic();
   if(vv!=null){%>
<div class="container">
<div class="play">
<a href="javascript:launchApplet('jclicApplet.jsp?project=<%=srvBase%>/projects/<%=vv.folder%>/<%=vv.path%><%=b.getAppletExtraParams(vv)%>')">
    <img src="../img/play.png" alt="<%=b.getMsg("runApplet")%>" title="<%=b.getMsg("runApplet")%>"/></a></div>
<%}%>       
   <img src="<%=srvBase%>/gifs/<%=b.act.imatge%>" border="0" hspace="10" vspace="5" align="right" width="<%=b.act.imatge_w%>" height="<%=b.act.imatge_h%>" alt="">
<% if(vv!=null){%>
</div>
<%}%>
   <!-- InstanceEndEditable -->
<% for(int i=0; i<b.act.centres.length; i++){
    edu.xtec.web.clic.WCentres c=b.act.centres[i].centre;
%>
    <div class="centre">
	 <!-- InstanceBeginEditable name="centre" --><b><%=c.getLinkedName()%></b><br>
<%=c.getLocalitat()%>
<!-- InstanceEndEditable --><br>
    </div>
<% }%>
        <div class="desc"> <!-- InstanceBeginEditable name="desc" --><%=b.act.descripcio%><!-- InstanceEndEditable --> </div>
   <table border="0" cellspacing="2" cellpadding="3" >
<%  for(int i=0; i<b.infoCaixes.length; i++){%>
     <tr>
	  <td valign="top" class="taulaInfoRow" width="106"><!-- InstanceBeginEditable name="itLabel" --><%=b.infoCaixes[i][0]%><!-- InstanceEndEditable --></td>
	  <td class="taulaInfoCol" width="259"><!-- InstanceBeginEditable name="itValue" --><%=b.infoCaixes[i][1]%><!-- InstanceEndEditable --></td>
	 </tr>
<%  }
    if(b.act.ccLicenseUrl!=null){%>
     <tr>
	  <td valign="top" class="taulaInfoRow"><%=b.getMsg("license")%></td>
	  <td class="taulaInfoCol"><a rel="license" href="http://creativecommons.org/licenses/<%=b.act.ccLicenseUrl%>/2.1/es/"><img src="../img/somerights20b.gif" alt="" title="<%=b.getMsg("license_cc_alt")%>" width="70" height="25" border="0" align="left" /><%=b.getMsg("license_cc")%></a>
<%=b.act.ccLicenseTag%>	  
	  </td>
	 </tr>
<%  }
    else if(b.act.licenseText!=null){%>
     <tr>
	  <td valign="top" class="taulaInfoRow"><%=b.getMsg("license")%></td>
	  <td class="taulaInfoCol"><%=b.act.licenseText%></td>
	 </tr>
<%  }%>
   </table>
        <!-- InstanceBeginEditable name="addThis" --><hr style="margin-top: 40px;"><!-- AddThis Button BEGIN -->
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
  
<% for(int i=0; i<b.act.versions.length; i++){
    edu.xtec.web.clic.WVersions v=b.act.versions[i];
	if(!v.esPublic && !b.checking)
	  continue;
    if("j".equals(v.tipus)){ %>
  <tr>
   <td><img src="../img/f1_r2_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/f1_r2_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img src="../img/f1_r2_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr> 
   <td width="20" height="60" background="../img/f1_r3_c2.gif">&nbsp;</td>
   <td class="f1" width="718" valign="top">
    <table border="0" cellspacing="0" cellpadding="3" width="718" class="f1">
          <tr>
	        <td class="fBox" colspan="2"><!-- InstanceBeginEditable name="titolJC" --><%=b.getVerTitle(v)%><!-- InstanceEndEditable --></td>
	        <td width="20%" align="right" class="fBox"><!-- InstanceBeginEditable name="dataJC" --><%=b.dateFormat.format(v.dataVersio)%><!-- InstanceEndEditable --></td>
	 </tr>
	 <tr>
	        <td width="42%" rowspan="2" valign="top" class="f1"><a href="../ca/jclic/howto.htm" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('info<%=i%>1','','../img/info_on.gif',1)"><img src="../img/info.gif" name="info<%=i%>1" width="20" height="20" hspace="3" border="0" align="absmiddle" id="info<%=i%>1" alt="">com 
              funciona?</a><br>
			   <!-- InstanceBeginEditable name="applet" --> 
              <a href="javascript:launchApplet('jclicApplet.jsp?project=<%=srvBase%>/projects/<%=v.folder%>/<%=v.path%><%=b.getAppletExtraParams(v)%>')" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('img<%=img%>','','../img/run_applet_on.gif',1)"><img src="../img/run_applet.gif" width="20" height="20" border="0" name="img<%=img++%>" hspace="3" align="absmiddle" alt=""><%=b.getMsg("runApplet")%></a><!-- InstanceEndEditable -->
              <%     for (int k=0; k<v.fitxers.length; k++){
        edu.xtec.web.clic.WFitxers f=v.fitxers[k];
%>
        <br>
              <!-- InstanceBeginEditable name="fitxerJC" --> <a href="<%=f.jnlp ? srvBase+"/jnlp/jclic/install.jnlp?argument=" : ""%><%=srvBase%>/projects/<%=v.folder%>/<%=f.nom%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('img<%=img%>','','../img/<%=f.ico%>_on.gif',1)"><img src="../img/<%=f.ico%>.gif" border="0" width="20" height="20" name="img<%=img++%>" hspace="3" align="absmiddle" alt=""><%=f.jnlp ? b.getMsg("Install") : f.nomCurt%></a><%
if(!f.jnlp && f.mida>0){%>&nbsp;(<%=b.numberFormat.format(f.mida)%>&nbsp;Kb)<%}%>
              <!-- InstanceEndEditable --> 
              <%     }%>
			  
			  
			  
		    </td>
	  <td class="f1" valign="top" align="right" colspan="2">
<%     for(int k=0; k<v.autors.length; k++){
        edu.xtec.web.clic.WAutorsVer av=v.autors[k];
%>
        <!-- InstanceBeginEditable name="autorJC" -->
<%if(av.rol!=null && av.rol.length()>0){%><%=av.rol%>: <%}%><%=av.autor.getNom()%><%
if(av.autor.mail!=null){%> <a href="javascript:mailto('<%=b.em(av.autor.mail)%>')"><img src="../img/mail.gif" border="0" alt="<%=b.getMsg("mailto")%> <%=av.autor.getNom()%>" title="<%=b.getMsg("mailto")%> <%=av.autor.getNom()%>"><%}%></a><br>
<!-- InstanceEndEditable -->
<%     }%>
              <!-- InstanceBeginEditable name="actsJC" --><%=b.numberFormat.format(v.mida)%>&nbsp;Kb - <%=v.nActivitats%>&nbsp;<%=b.getMsg("activities")%><!-- InstanceEndEditable --><br>
              <!-- InstanceBeginEditable name="midaJC" --><!-- InstanceEndEditable -->
            </td>		
	 </tr>
	 <tr>
            <td class="url" valign="bottom" align="right" colspan="2"><!-- InstanceBeginEditable name="urlJC" -->
<%=b.getMsg("embed")%> <input readonly onclick="this.focus(); this.select();" type="text" value=
"&lt;div style=&quot;width:560;text-align:center;font-size:x-small;&quot;&gt;&lt;iframe src=&quot;<%=srvBase%>/db/jclicApplet.jsp?project=<%=srvBase%>/projects/<%=v.folder%>/<%=v.path%>&amp;skin=@mini.xml&amp;lang=ca&quot; frameborder=0 width=560 height=440 scrolling=&quot;no&quot;&gt;&lt;/iframe&gt;&lt;br&gt;&lt;a href=&quot;<%=srvBase%>/db/act_ca.jsp?id=<%= b.id %>&quot;&gt;<%=b.getMsg("pre_title")%> <%=b.getTitle()%>&lt;/a&gt;&lt;/div&gt;" /><br>
<%=b.getMsg("projectURL")%> <input readonly onclick="this.focus(); this.select();" type="text" value="<%=srvBase%>/projects/<%=v.folder%>/<%=v.path%>" /><!-- InstanceEndEditable --></td>
          </tr>
    </table>
   </td>
   <td width="20" background="../img/f1_r3_c4.gif">&nbsp;</td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img src="../img/f1_r4_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/f1_r4_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img src="../img/f1_r4_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
<% } else { %>
  <tr>
   <td><img src="../img/f2_r2_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/f2_r2_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img src="../img/f2_r2_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
   <td width="20" height="60" background="../img/f2_r3_c2.gif">&nbsp;</td>
   <td class="f2" width="718" valign="top">
	<table border="0" cellspacing="0" cellpadding="3" width="718" class="f2">
	 <tr>
	  <td class="fBox" colspan="2"><!-- InstanceBeginEditable name="titolC3" --><%=b.getVerTitle(v)%><!-- InstanceEndEditable --></td>
	        <td width="20%" align="right" class="fBox"><!-- InstanceBeginEditable name="dataC3" --><%=b.dateFormat.format(v.dataVersio)%><!-- InstanceEndEditable --></td>
	 </tr>
	 <tr>
	        <td width="55%" rowspan="2" valign="top" class="f2"><a href="../ca/clic3/howto.htm" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('info<%=i%>','','../img/info_on.gif',1)"><img src="../img/info.gif" width="20" height="20" border="0" name="info<%=i%>" hspace="3" align="absmiddle" alt="">com 
              funciona?</a><br>
<%     int tipus=-1;
       int cnt=0;
	   %><table border="0" cellspacing="0"><tr><%
       for (int k=0; k<v.fitxers.length; k++){
         edu.xtec.web.clic.WFitxers f=v.fitxers[k];
		 if(cnt>=2 || (tipus>=0 && tipus!=v.fitxers[k].tipus)){
		   cnt=0;
		   %></tr><tr><%
		 }
		 cnt++;
		 tipus=v.fitxers[k].tipus;		 
%><td><!-- InstanceBeginEditable name="fitxerC3" --><a href="<%=srvBase%>/projects/<%=v.folder%>/<%=f.nom%>" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('img<%=img%>','','../img/<%=f.ico%>_on.gif',1)"><img src="../img/<%=f.ico%>.gif" border="0" width="20" height="20" name="img<%=img++%>" hspace="3" align="absmiddle" alt=""><%=f.nomCurt%></a><%
if(!f.jnlp && f.mida>0){%>&nbsp;(<%=b.numberFormat.format(f.mida)%>&nbsp;Kb)<%}%>
              <!-- InstanceEndEditable --></td>
<%
	  }%></tr></table>
	  </td>
	  <td class="f2" valign="top" align="right" colspan="2">
<%     for(int k=0; k<v.autors.length; k++){
        edu.xtec.web.clic.WAutorsVer av=v.autors[k];
%><!-- InstanceBeginEditable name="autorC3" -->
<%if(av.rol!=null && av.rol.length()>0){%><%=av.rol%>: <%}%><%=av.autor.getNom()%><%
if(av.autor.mail!=null && av.autor.mail.length()>0){%> <a href="javascript:mailto('<%=b.em(av.autor.mail)%>')"><img src="../img/mail.gif" border="0" alt="<%=b.getMsg("mailto")%> <%=av.autor.getNom()%>" title="<%=b.getMsg("mailto")%> <%=av.autor.getNom()%>"><%}%></a><br>
<!-- InstanceEndEditable -->
<%     }%>
<!-- InstanceBeginEditable name="actsC3" --><%=b.numberFormat.format(v.mida)%>&nbsp;Kb - <%=v.nActivitats%>&nbsp;<%=b.getMsg("activities")%><!-- InstanceEndEditable --><br>
<!-- InstanceBeginEditable name="midaC3" --><!-- InstanceEndEditable -->
       </td>
     </tr>
	 <tr>
	   <td class="url" valign="bottom" align="right" colspan="2"><!-- InstanceBeginEditable name="urlC3" --><!-- InstanceEndEditable --></td>
	 </tr>
	</table>
   </td>
   <td width="20" background="../img/f2_r3_c4.gif">&nbsp;</td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img src="../img/f2_r4_c2.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/f2_r4_c3.gif" width="718" height="20" border="0" alt=""></td>
   <td><img src="../img/f2_r4_c4.gif" width="20" height="20" border="0" alt=""></td>
   <td><img src="../img/spacer.gif" width="1" height="20" border="0" alt=""></td>
  </tr>
<%
 }
}
%>
<!-- XXXXX -->
  
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

