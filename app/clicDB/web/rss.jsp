<%@page session="false" contentType="text/xml; charset=iso-8859-1"
%><jsp:useBean id="b" class="edu.xtec.web.clic.RSSBean" scope="request" /><%
if(!b.init(request, response, null)){%><jsp:forward page="error_<%=b.lang%>.jsp"/><%}
String appBase=b.getAppBase();
String srvBase=b.getServerBase();
%><?xml version="1.0" encoding="ISO-8859-1"?>
<rdf:RDF
 xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
 xmlns="http://purl.org/rss/1.0/"
 xmlns:dc="http://purl.org/dc/elements/1.1/"
 xmlns:syn="http://purl.org/rss/1.0/modules/syndication/"
 xmlns:admin="http://webns.net/mvcb/"
 xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/"
>
<channel rdf:about="<%=srvBase%>">
<title><%=b.getMsg("web_name")%></title>
<link><%=b.getMsg("web_link")%></link>
<description><%=b.getMsg("web_desc")%></description>
<dc:language><%=b.lang%></dc:language>
<dc:date><%=b.ISODateFormat.format(b.getLastDate())%></dc:date>
<dc:creator><%=b.getMsg("web_creator")%></dc:creator>
<items>
 <rdf:Seq><%if(b.rss!=null){
    for(int i=0; i<b.rss.length; i++){%>
  <rdf:li rdf:resource="<%=appBase%>/news_<%=b.lang%>.jsp#<%=b.rss[i].id%>" /><%  }
  }%>
 </rdf:Seq>
</items>
<image rdf:resource="<%=b.getMsg("web_image")%>" />
<textinput rdf:resource="<%=srvBase%><%=b.lang%>/cercar/index.htm" />
</channel>

<image rdf:about="<%=b.getMsg("web_image")%>">
<title><%=b.getMsg("web_name")%></title>
<url><%=b.getMsg("web_image")%></url>
<link><%=b.getMsg("web_root")%></link>
</image>
<% if(b.rss!=null){
    for(int i=0; i<b.rss.length; i++){
      edu.xtec.web.clic.WRSSDesc desc=b.rss[i].descripcions[0];
%>
<item rdf:about="<%=appBase%>/news_<%=b.lang%>.jsp#<%=b.rss[i].id%>">
<title><%=desc.title%></title>
<link><%=desc.link%></link>
<description><%=b.rss[i].getFilteredImageTag()%><%=desc.getFilteredDescription()%></description>
<dc:date><%=b.ISODateFormat.format(b.rss[i].data_pub)%></dc:date>
</item>
<%  }
}%>
</rdf:RDF>
