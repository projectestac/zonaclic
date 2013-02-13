<%
    String lang = request.getHeader("ACCEPT-LANGUAGE");
    String whatPage = "act_en.jsp";
    if(lang!=null){
      lang=lang.trim().toLowerCase();
      java.util.StringTokenizer langParser = new java.util.StringTokenizer(lang, " ,");
      while(langParser.hasMoreTokens()){
        String s=langParser.nextToken();
        if(s.startsWith("en")){
          break;
        }
        else if(s.startsWith("ca")){
          whatPage="act_ca.jsp";
          break;
        }
        else if(s.startsWith("es")){
          whatPage="act_es.jsp";
          break;
        }
      }
    }
%><jsp:forward page="<%=whatPage%>"/>