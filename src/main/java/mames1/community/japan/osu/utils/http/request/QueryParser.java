package mames1.community.japan.osu.utils.http.request;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class QueryParser {

    private static String urlDecode(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    public static Map<String, String> parse(String query) {

        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            String key = idx >= 0 ? pair.substring(0, idx) : pair;
            String val = (idx >= 0 && idx + 1 < pair.length()) ? pair.substring(idx + 1) : "";
            map.put(urlDecode(key), urlDecode(val));
        }
        return map;
    }
}
