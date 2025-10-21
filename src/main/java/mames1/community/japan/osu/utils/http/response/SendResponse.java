package mames1.community.japan.osu.utils.http.response;

import com.sun.net.httpserver.HttpExchange;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class SendResponse {

    public static void write(HttpExchange exchange, int status, String body) {

        try {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(status, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } catch (Exception e) {
            Logger.log("レスポンスの送信に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
