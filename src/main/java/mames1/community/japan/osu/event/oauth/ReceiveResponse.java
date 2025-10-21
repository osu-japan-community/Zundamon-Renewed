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
            int clientId;
            String code = params.get("code");

            if (code == null || code.isEmpty()) {
                reLogin(exchange);
                Logger.log("OAuthレスポンスにコードが含まれていません。", Level.WARN);
                return;
            }

            if (Main.link != null) {
                if(System.currentTimeMillis() - Main.link.getLastRequestTime() < 360000) {
                    // 待機時間を計算
                    long remainingMillis = 360000 - (System.currentTimeMillis() - Main.link.getLastRequestTime());
                    long remainingSeconds = Math.max(0, remainingMillis / 1000);

                    String html = """
                        <!DOCTYPE html>
                        <html lang="ja">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>認証エラー</title>
                            <style>
                                body {
                                    font-family: 'Roboto', Arial, sans-serif;
                                    background-color: #f8f9fa;
                                    color: #202124;
                                    display: flex;
                                    justify-content: center;
                                    align-items: center;
                                    height: 100vh;
                                    margin: 0;
                                    text-align: center;
                                }
                                .container {
                                    max-width: 600px;
                                    padding: 48px;
                                    background-color: #ffffff;
                                    border: 1px solid #dadce0;
                                    border-radius: 8px;
                                    box-shadow: 0 1px 2px 0 rgba(60,64,67,0.3), 0 1px 3px 1px rgba(60,64,67,0.15);
                                }
                                h1 {
                                    font-size: 24px;
                                    font-weight: 400;
                                    margin-bottom: 16px;
                                }
                                p {
                                    font-size: 16px;
                                    line-height: 1.5;
                                }
                                .timer {
                                    font-size: 20px;
                                    font-weight: 500;
                                    color: #d93025;
                                    margin-top: 24px;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <h1>認証処理がロックされています</h1>
                                <p>現在、他のユーザーが認証処理を行っています。<br>しばらく待ってから再度お試しください。</p>
                                <p class="timer">
                                    <span id="countdown">%d</span> 秒後に再試行可能になります。
                                </p>
                            </div>
                            <script>
                                (function() {
                                    let seconds = %d;
                                    const countdownElement = document.getElementById('countdown');
                                    const interval = setInterval(function() {
                                        seconds--;
                                        countdownElement.textContent = seconds;
                                        if (seconds <= 0) {
                                            clearInterval(interval);
                                            window.location.reload();
                                        }
                                    }, 1000);
                                })();
                            </script>
                        </body>
                        </html>
                        """.formatted(remainingSeconds, remainingSeconds);

                    SendResponse.writeHtml(exchange, 400, html);
                    Logger.log("待機用ウェブを送信しました。", Level.INFO);
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

            Main.link = new Link(userId, username, System.currentTimeMillis());

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
