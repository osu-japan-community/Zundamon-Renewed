package mames1.community.japan.osu.event.oauth;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.object.Bancho;
import mames1.community.japan.osu.object.Discord;
import mames1.community.japan.osu.object.Link;
import mames1.community.japan.osu.utils.http.oauth.OAuthMeResponseSender;
import mames1.community.japan.osu.utils.http.oauth.OAuthRequestSender;
import mames1.community.japan.osu.utils.http.request.ClientIpExtractor;
import mames1.community.japan.osu.utils.http.request.QueryParser;
import mames1.community.japan.osu.utils.http.request.RequestPrinter;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

public class BanchoLinkListener implements HttpHandler {

    private void reLogin(HttpExchange httpExchange) throws IOException {

        Bancho bancho = new Bancho();

        String banchoAuthURL = "https://osu.ppy.sh/oauth/authorize" +
                "?client_id=" + bancho.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + bancho.getRedirectUri() +
                "&scope=public+identify";

        httpExchange.getResponseHeaders().set("Location", banchoAuthURL);
        httpExchange.sendResponseHeaders(302, -1);
        httpExchange.close();
    }

    @Override
    public void handle(HttpExchange exchange) {

        try {

            final String tokenURL = "https://osu.ppy.sh/oauth/token";
            final String meURL = "https://osu.ppy.sh/api/v2/me";
            final String discordAuthURL = "https://discord.com/api/oauth2/authorize";

            Map<String, String> params = QueryParser.parse(exchange.getRequestURI().getQuery());
            Bancho bancho;
            String clientSecret, redirectUri;
            String ip = ClientIpExtractor.getIP(exchange);
            int clientId;
            String code = params.get("code");

            if (code == null || code.isEmpty()) {
                reLogin(exchange);
                AppLogger.log("OAuthレスポンスにコードが含まれていません。", LogLevel.WARN);
                return;
            }

            // OAuthトークンを取得
            bancho = new Bancho();
            clientId = bancho.getClientId();
            clientSecret = bancho.getClientSecret();
            redirectUri = bancho.getRedirectUri();

            HttpResponse<String> response = OAuthRequestSender.send(
                    String.valueOf(clientId),
                    clientSecret,
                    code,
                    redirectUri,
                    tokenURL
            );

            if(Objects.requireNonNull(response).statusCode() != 200) {
                reLogin(exchange);
                AppLogger.log("osu!のOAuthトークンの取得に失敗しました: " + response.body(), LogLevel.WARN);
                return;
            }

            HttpResponse<String> meResponse = OAuthMeResponseSender.send(
                    response,
                    meURL
            );

            if (Objects.requireNonNull(meResponse).statusCode() != 200) {
                reLogin(exchange);
                AppLogger.log("ユーザー情報の取得に失敗しました: " + meResponse.body(), LogLevel.WARN);
                return;
            }

            JSONObject meJson = new JSONObject(meResponse.body());

            long userId = meJson.getLong("id");
            String username = meJson.getString("username");

            Main.linkCache.put(ip, new Link(userId, username));

            AppLogger.log("Banchoユーザーと連携しました: id=" + userId + ", username=" + username, LogLevel.INFO);

            Discord discord = new Discord();

            String discordAuth = discordAuthURL +
                    "?client_id=" + discord.getClientId() +
                    "&response_type=code" +
                    "&redirect_uri=" + discord.getRedirectUri() +
                    "&scope=identify+connections";

            exchange.getResponseHeaders().set("Location", discordAuth);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();

            RequestPrinter.print(exchange);

        } catch (Exception e) {
            AppLogger.log("OAuthレスポンスの処理に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }
    }
}
