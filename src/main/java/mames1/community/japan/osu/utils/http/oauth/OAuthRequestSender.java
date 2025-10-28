package mames1.community.japan.osu.utils.http.oauth;

import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.http.encode.FormUrlEncoder;
import mames1.community.japan.osu.utils.log.AppLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public abstract class OAuthRequestSender {

    public static HttpResponse<String> sendOAuthRequest(
            String clientId,
            String clientSecret,
            String code,
            String redirectUri,
            String tokenUrl
    ) {
        try {
            String form = FormUrlEncoder.encode(
                    Map.of(
                            "client_id", String.valueOf(clientId),
                            "client_secret", clientSecret,
                            "code", code,
                            "grant_type", "authorization_code",
                            "redirect_uri", redirectUri
                    )
            );

            HttpRequest request = HttpRequest.newBuilder(URI.create(tokenUrl))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            AppLogger.log("OAuthリクエストの送信に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }

        return null;
    }
}
