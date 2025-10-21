package mames1.community.japan.osu.object;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;
import mames1.community.japan.osu.event.oauth.LinkDiscord;
import mames1.community.japan.osu.event.oauth.ReceiveResponse;
import mames1.community.japan.osu.utils.http.request.PrintRequest;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.net.InetSocketAddress;

public class Server {

    public final int port;

    public Server () {
        Dotenv dotenv = Dotenv.configure().load();
        this.port = Integer.parseInt(dotenv.get("SERVER_PORT"));
    }

    public void start() {

        try {

            HttpServer server = HttpServer.create(
                    new InetSocketAddress(port), 0
            );

            // レスポンスのデバッグ用コンテキスト
            server.createContext("/", exchange -> {
                try {
                    PrintRequest.print(exchange);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            server.createContext("/oauth", new ReceiveResponse());
            server.createContext("/discord", new LinkDiscord());

            Logger.log("サーバーをポート " + port + " で起動しました。", Level.INFO);

            server.setExecutor(null);
            server.start();

        } catch (Exception e) {
            Logger.log("サーバーの起動に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
