package com.collabchat.util;

public class StringUtil {
    // Minimal JSON escaper
    public static String escapeJson(String s) {
        if (s == null) return "\"\"";
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < ' ') {
                        String t = Integer.toHexString(c);
                        sb.append("\\u");
                        for (int k=0;k<4-t.length();k++) sb.append('0');
                        sb.append(t);
                    } else sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }
}
