package mames1.community.japan.osu.object;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;
import mames1.community.japan.osu.event.oauth.DiscordLinkListener;
import mames1.community.japan.osu.event.oauth.BanchoLinkListener;
import mames1.community.japan.osu.utils.http.request.RequestPrinter;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;

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
                    RequestPrinter.print(exchange);
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            server.createContext("/oauth", new BanchoLinkListener());
            server.createContext("/discord", new DiscordLinkListener());

            AppLogger.log("サーバーをポート " + port + " で起動しました。", LogLevel.INFO);

            server.setExecutor(null);
            server.start();

        } catch (Exception e) {
            AppLogger.log("サーバーの起動に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }
}
