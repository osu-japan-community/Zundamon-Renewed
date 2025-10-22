package mames1.community.japan.osu.utils.http.request;

import com.sun.net.httpserver.HttpExchange;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

public abstract class PrintRequest {

    public static void print(HttpExchange exchange) {
        try {
            String ip = GetClientIP.getIP(exchange);

            Logger.log(
                    exchange.getRequestMethod() + ": " +
                            exchange.getRequestURI().getPath() + "?" +
                            exchange.getRequestURI().getQuery() + " (" + ip + ")", Level.DEBUG
            );

        } catch (Exception e) {
            Logger.log("リクエストの表示に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}