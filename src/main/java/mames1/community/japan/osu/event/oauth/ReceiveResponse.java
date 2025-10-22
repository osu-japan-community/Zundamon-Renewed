package mames1.community.japan.osu.event.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.object.Bancho;
import mames1.community.japan.osu.object.Discord;
import mames1.community.japan.osu.object.Link;
import mames1.community.japan.osu.utils.http.encode.FormURLEncoder;
import mames1.community.japan.osu.utils.http.request.GetClientIP;
import mames1.community.japan.osu.utils.http.request.ParseQuery;
import mames1.community.japan.osu.utils.http.request.PrintRequest;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ReceiveResponse implements HttpHandler {

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

            Map<String, String> params = ParseQuery.parse(exchange.getRequestURI().getQuery());
            Bancho bancho;
            String clientSecret, redirectUri, form;
            String ip = GetClientIP.getIP(exchange);
            int clientId;
            String code = params.get("code");

            if (code == null || code.isEmpty()) {
                reLogin(exchange);
                Logger.log("OAuthレスポンスにコードが含まれていません。", Level.WARN);
                return;
            }

            // OAuthトークンを取得
            bancho = new Bancho();
            clientId = bancho.getClientId();
            clientSecret = bancho.getClientSecret();
            redirectUri = bancho.getRedirectUri();

            form = FormURLEncoder.encode(
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
                Logger.log("OAuthトークンの取得に失敗しました: " + response.body(), Level.WARN);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode tokenJson = mapper.readTree(response.body());
            String accessToken = tokenJson.get("access_token").asText(null);

            if (accessToken == null) {
                reLogin(exchange);
                Logger.log("OAuthレスポンスにアクセストークンが含まれていません: " + response.body(), Level.WARN);
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
                Logger.log("ユーザー情報の取得に失敗しました: " + meResponse.body(), Level.WARN);
                return;
            }

            JSONObject meJson = new JSONObject(meResponse.body());

            long userId = meJson.getLong("id");
            String username = meJson.getString("username");

            Main.linkCache.put(ip, new Link(userId, username));

            Logger.log("Banchoユーザーと連携しました: id=" + userId + ", username=" + username, Level.INFO);

            Discord discord = new Discord();

            String discordAuth = discordAuthURL +
                    "?client_id=" + discord.getClientId() +
                    "&response_type=code" +
                    "&redirect_uri=" + discord.getRedirectUri() +
                    "&scope=identify+connections";

            exchange.getResponseHeaders().set("Location", discordAuth);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();

            PrintRequest.print(exchange);

        } catch (Exception e) {
            Logger.log("OAuthレスポンスの処理に失敗しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
