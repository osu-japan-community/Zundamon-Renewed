package mames1.community.japan.osu.utils.http.encode;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public abstract class FormURLEncoder {

    public static String encode(Map<String, String> map) {
        if (map == null || map.isEmpty()) return "";
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> e : map.entrySet()) {
            String key = encodeComponent(e.getKey());
            String val = encodeComponent(e.getValue());
            joiner.add(key + "=" + val);
        }
        return joiner.toString();
    }

    private static String encodeComponent(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }
}
