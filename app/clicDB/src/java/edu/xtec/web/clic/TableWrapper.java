/*
 * TableWrapper.java
 *
 * Created on 4 de julio de 2003, 15:43
 */
package edu.xtec.web.clic;

/**
 *
 * @author fbusquet
 */
public abstract class TableWrapper {

    public static final String S = "s", N = "n", SEP = "_";

    public TableWrapper() {
    }

    //public static final String[] FIELD_NAMES; 
    //public static final String TABLE_NAME;
    //public static final String PREFIX;
    protected static String buildSelectFields(String[] fn, String[] prFn, String pr) {
        //String[] fn=FIELD_NAMES;
        //String[] prFn=getPrefixedFieldNames();
        //String pr=PREFIX;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fn.length; i++) {
            sb.append(pr).append(".").append(fn[i]).append(" as ");
            sb.append(prFn[i]).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
    //private static String SELECT_FIELDS;
    //public static String getSelectFields(){
    //    if(SELECT_FIELDS==null)
    //        SELECT_FIELDS=buildSelectFields(FIELD_NAMES, getPrefixedFieldNames(), PREFIX);
    //    return SELECT_FIELDS;
    //}

    protected static String[] buildPrefixedFieldNames(String[] fn, String pr) {
        //String[] fn=FIELD_NAMES;
        //String pr=getPrefix();
        String[] result = new String[fn.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fn.length; i++) {
            sb.setLength(0);
            sb.append(pr).append(SEP).append(fn[i]);
            result[i] = sb.substring(0);
        }
        return result;
    }
    //private static String[] PREFIXED_FIELD_NAMES;
    //public static String[] getPrefixedFieldNames(){
    //    if(PREFIXED_FIELDS==null)
    //        PREFIXED_FIELDS=buildPrefixedFieldNames(FIELD_NAMES, PREFIX);
    //    return PREFIXED_FIELDS;
    //}

    public abstract void load(java.sql.ResultSet rs, edu.xtec.util.db.ConnectionBean con, String idioma) throws Exception;

    protected static boolean getBoolean(java.sql.ResultSet rs, String fieldName) throws java.sql.SQLException {
        return S.equals(rs.getString(fieldName));
    }

    public String getMainText() {
        return "";
    }
}
