package mames1.community.japan.osu.utils.http.response;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class SendResponse {

    public static void writeHtml(HttpExchange exchange, int statusCode, String html) throws Exception {
        byte[] response = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
        exchange.close();
    }
}
