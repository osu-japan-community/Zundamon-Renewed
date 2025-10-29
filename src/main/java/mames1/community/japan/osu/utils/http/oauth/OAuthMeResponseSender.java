package mames1.community.japan.osu.utils.http.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public abstract class OAuthMeResponseSender {

    public static HttpResponse<String> send (
            HttpResponse<String> response,
            String meURL
    ) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode tokenJson = mapper.readTree(response.body());
            String accessToken = tokenJson.get("access_token").asText(null);

            if (accessToken == null) {
                AppLogger.log("OAuthレスポンスにアクセストークンが含まれていません: " + response.body(), LogLevel.WARN);
                return null;
            }

            HttpRequest meRequest = HttpRequest.newBuilder(URI.create(meURL))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();

            return HttpClient.newHttpClient().send(meRequest, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            AppLogger.log("OAuthレスポンスの解析に失敗しました: " + e.getMessage(), LogLevel.ERROR);
        }

        return null;
    }
}
