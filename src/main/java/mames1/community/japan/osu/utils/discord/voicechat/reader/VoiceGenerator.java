package mames1.community.japan.osu.utils.discord.voicechat.reader;

import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public abstract class VoiceGenerator {

    public static HttpResponse<byte[]> generate(String message) {

        try {

            JSONObject json;
            final int speaker = 1; // ずんだもん

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:50021/audio_query?speaker=1&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8)))
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            json = new JSONObject(response.body());
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:50021/synthesis?speaker=" + speaker + "&text=" + URLEncoder.encode(message, StandardCharsets.UTF_8)))
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            return client.send(request, HttpResponse.BodyHandlers.ofByteArray());
        } catch (Exception e) {
            Logger.log("音声生成中にエラーが発生しました: " + e.getMessage(), Level.ERROR);
        }

        return null;
    }
}
