/*
 * Utilities.java
 *
 * Created on 10 de enero de 2003, 17:25
 */
package edu.xtec.web.clic;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author fbusquet
 */
public abstract class Utilities {

    public static final String s1 = "ÁÉÍÓÚÀÈÌÒÙÄËÏÖÜÂÊÎÔÛáéíóúàèìòùäëïöüâêîôûÑñÇç";
    public static final String s2 = "AEIOUAEIOUAEIOUAEIOUAEIOUAEIOUAEIOUAEIOUÑÑÇÇ";

    public static String en_majuscules(String s) {
        String result = s;
        if (s != null && s.length() > 0) {
            result = s.toUpperCase();
            for (int i = 0; i < s1.length(); i++) {
                result = result.replace(s1.charAt(i), s2.charAt(i));
            }
        }
        return result;
    }

    public static String[] parseSearchText(String s) {
        String[] result = null;
        if (s != null && s.length() > 0) {
            ArrayList v = new ArrayList(10);
            StringTokenizer st = new StringTokenizer(s, "\"", true);
            boolean enComes = false;
            while (st.hasMoreTokens()) {
                String tk = st.nextToken();
                if ("\"".equals(tk)) {
                    enComes = !enComes;
                } else {
                    if (enComes) {
                        v.add(en_majuscules(tk.trim()));
                    } else {
                        StringTokenizer st2 = new StringTokenizer(tk, " ", false);
                        while (st2.hasMoreTokens()) {
                            v.add(en_majuscules(st2.nextToken()));
                        }
                    }
                }
            }
            if (!v.isEmpty()) {
                result = (String[]) v.toArray(new String[v.size()]);
            }
        }
        return result;
    }

    public static int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String paramString = request.getParameter(paramName);
        int paramValue;
        try {
            paramValue = Integer.parseInt(paramString);
        } catch (NumberFormatException nfe) { // null or bad format
            paramValue = defaultValue;
        }
        return (paramValue);
    }

    public static String toNbsp(String src) {
        StringBuilder sb = new StringBuilder();
        if (src != null) {
            StringTokenizer st = new StringTokenizer(src);
            if (st.hasMoreTokens()) {
                while (true) {
                    sb.append(st.nextToken());
                    if (st.hasMoreTokens()) {
                        sb.append("&nbsp;");
                    } else {
                        break;
                    }
                }
            }
        }
        return sb.toString();
    }
    public static String VALID_CHARS = "'-_.!~*()ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 0123456789";

    public static String urlEncode(String url, boolean escapeApo) {
        //url=url.replace('\'', '\u2019');
        byte[] urlBytes;
        try {
            //urlBytes = url.getBytes("UTF-8");
            urlBytes = url.getBytes("ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            urlBytes = url.getBytes();
        }
        StringBuilder out = new StringBuilder(urlBytes.length);
        for (int i = 0; i < urlBytes.length; i++) {
            int c = (int) urlBytes[i];
            if (VALID_CHARS.indexOf(c) != -1) {
                if (c == ' ') {
                    out.append('+');
                } else if (escapeApo && c == '\'') {
                    out.append("\\'");
                } else {
                    out.append((char) c);
                }
            } else {
                out.append("%").append(Long.toHexString((long) (c & 0xff)).toUpperCase());
            }
        }
        return out.substring(0);
    }
    public static final String FORBIDDEN_XML_CHARS = "\"'&<>";
    public static final String[] XML_REPLACING_ENTITIES = {"&quot;", "&#39;", "&amp;", "&lt;", "&gt;"};

    public static String xmlEncode(String txt) {
        String result = null;
        if (txt != null) {
            StringBuilder sb = new StringBuilder(txt.length() * 2);
            StringTokenizer st = new StringTokenizer(txt, FORBIDDEN_XML_CHARS, true);
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                int p = FORBIDDEN_XML_CHARS.indexOf(s.charAt(0));
                if (p >= 0) {
                    sb.append(XML_REPLACING_ENTITIES[p]);
                } else {
                    sb.append(s);
                }
            }
            result = sb.substring(0);
        }
        return result;
    }

    public static String getKeyFor(String[][] dictionary, String value) {
        String result = value;
        if (dictionary != null && value != null) {
            for (int i = 0; i < dictionary.length; i++) {
                if (value.equals(dictionary[i][0])) {
                    result = dictionary[i][2];
                    break;
                }
            }
        }
        return result;
    }
}
