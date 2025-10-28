package mames1.community.japan.osu.utils.http.request;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.net.InetAddress;

public abstract class ClientIpExtractor {

    // プロキシ配下でも実クライアントIPを取得
    public static String getIP(HttpExchange exchange) {
        Headers h = exchange.getRequestHeaders();

        // 優先順: Cloudflare -> X-Forwarded-For(先頭) -> X-Real-IP -> RemoteAddress
        String cf = safeTrim(h.getFirst("CF-Connecting-IP"));
        if (isValidIp(cf)) return cf;

        String xff = safeTrim(h.getFirst("X-Forwarded-For"));
        if (xff != null && !xff.isEmpty()) {
            String[] parts = xff.split(",");
            for (String p : parts) {
                String ip = safeTrim(p);
                if (isValidIp(ip)) return ip;
            }
        }

        String xri = safeTrim(h.getFirst("X-Real-IP"));
        if (isValidIp(xri)) return xri;

        return exchange.getRemoteAddress().getAddress().getHostAddress();
    }

    private static boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) return false;
        try {
            // 名前解決せず書式妥当性のみを確認
            InetAddress addr = InetAddress.getByName(ip);
            return addr != null;
        } catch (Exception ignored) {
            return false;
        }
    }

    private static String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
