package mames1.community.japan.osu.utils.http.request;

import com.sun.net.httpserver.HttpExchange;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;

public abstract class RequestPrinter {

    public static void print(HttpExchange exchange) {
        try {
            String ip = ClientIpExtractor.getIP(exchange);

            AppLogger.log(
                    exchange.getRequestMethod() + ": " +
                            exchange.getRequestURI().getPath() + "?" +
                            exchange.getRequestURI().getQuery() + " (" + ip + ")", LogLevel.DEBUG
            );

        } catch (Exception e) {
            AppLogger.log("リクエストの表示に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }
}