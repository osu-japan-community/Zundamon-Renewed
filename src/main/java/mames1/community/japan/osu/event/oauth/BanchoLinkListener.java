package mames1.community.japan.osu.event.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.object.Bancho;
import mames1.community.japan.osu.object.Discord;
import mames1.community.japan.osu.object.Link;
import mames1.community.japan.osu.utils.http.encode.FormUrlEncoder;
import mames1.community.japan.osu.utils.http.request.ClientIpExtractor;
import mames1.community.japan.osu.utils.http.request.QueryParser;
import mames1.community.japan.osu.utils.http.request.RequestPrinter;
import mames1.community.japan.osu.utils.log.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

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
            String clientSecret, redirectUri, form;
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

            form = FormUrlEncoder.encode(
                    Map.of(
                            "client_id", String.valueOf(clientId),
                            "client_secret", clientSecret,
                            "code", code,
                            "grant_type", "authorization_code",
                            "redirect_uri", redirectUri
                    )
            );

            HttpRequest request = HttpRequest.newBuilder(URI.create(tokenURL))
                            .header("Accept", "application/json")
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(form))
                            .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                reLogin(exchange);
                AppLogger.log("OAuthトークンの取得に失敗しました: " + response.body(), LogLevel.WARN);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode tokenJson = mapper.readTree(response.body());
            String accessToken = tokenJson.get("access_token").asText(null);

            if (accessToken == null) {
                reLogin(exchange);
                AppLogger.log("OAuthレスポンスにアクセストークンが含まれていません: " + response.body(), LogLevel.WARN);
                return;
            }

            HttpRequest meRequest = HttpRequest.newBuilder(URI.create(meURL))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<String> meResponse = HttpClient.newHttpClient().send(meRequest, HttpResponse.BodyHandlers.ofString());

            if (meResponse.statusCode() != 200) {
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
