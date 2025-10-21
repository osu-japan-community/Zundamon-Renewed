package mames1.community.japan.osu.event.oauth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.community.japan.osu.utils.http.request.PrintRequest;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

public class ReceiveResponse implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {

        try {

            PrintRequest.print(exchange);


        } catch (Exception e) {
            Logger.log("OAuthレスポンスの処理に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
