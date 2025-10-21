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
import mames1.community.japan.osu.utils.http.request.ParseQuery;
import mames1.community.japan.osu.utils.http.request.PrintRequest;
import mames1.community.japan.osu.utils.http.response.SendResponse;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class ReceiveResponse implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {

        try {

            final String tokenURL = "https://osu.ppy.sh/oauth/token";
            final String meURL = "https://osu.ppy.sh/api/v2/me";
            final String discordAuthURL = "https://discord.com/api/oauth2/authorize";

            Map<String, String> params = ParseQuery.parse(exchange.getRequestURI().getQuery());
            Bancho bancho;
            String clientSecret, redirectUri, form;
            int clientId;
            String code = params.get("code");

            if (code == null || code.isEmpty()) {
                SendResponse.write(exchange, 400, "Code is missing.");
                return;
            }

            if (Main.link != null) {
                if(System.currentTimeMillis() - Main.link.getLastRequestTime() < 360000) {
                    SendResponse.write(exchange, 400, "現在他の認証中のユーザーがいるため、しばらく待ってから再度お試しください。");
                    return;
                }
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
                SendResponse.write(exchange, 500, "Failed to retrieve OAuth token.");
                Logger.log("OAuthトークンの取得に失敗しました: " + response.body(), Level.ERROR);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode tokenJson = mapper.readTree(response.body());
            String accessToken = tokenJson.get("access_token").asText(null);

            if (accessToken == null) {
                SendResponse.write(exchange, 500, "Access token is missing in the response.");
                Logger.log("OAuthレスポンスにアクセストークンが含まれていません: " + response.body(), Level.ERROR);
                return;
            }

            HttpRequest meRequest = HttpRequest.newBuilder(URI.create(meURL))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<String> meResponse = HttpClient.newHttpClient().send(meRequest, HttpResponse.BodyHandlers.ofString());

            if (meResponse.statusCode() != 200) {
                SendResponse.write(exchange, 500, "Failed to retrieve user information.");
                Logger.log("ユーザー情報の取得に失敗しました: " + meResponse.body(), Level.ERROR);
                return;
            }

            JSONObject meJson = new JSONObject(meResponse.body());
            long userId = meJson.getLong("id");
            String username = meJson.getString("username");

            Main.link = new Link(userId, username, System.currentTimeMillis());

            Logger.log("Bancho user linked: id=" + userId + ", username=" + username, Level.INFO);

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
