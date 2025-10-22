package mames1.community.japan.osu.event.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.constants.ServerGuild;
import mames1.community.japan.osu.constants.ServerRole;
import mames1.community.japan.osu.object.Discord;
import mames1.community.japan.osu.object.Link;
import mames1.community.japan.osu.object.MySQL;
import mames1.community.japan.osu.utils.http.encode.FormURLEncoder;
import mames1.community.japan.osu.utils.http.request.GetClientIP;
import mames1.community.japan.osu.utils.http.request.ParseQuery;
import mames1.community.japan.osu.utils.http.request.PrintRequest;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

public class LinkDiscord implements HttpHandler {

    private void reLogin(HttpExchange exchange, Discord discord) throws Exception {
        String discordAuth = "https://discord.com/api/oauth2/authorize" +
                "?client_id=" + discord.getClientId() +
                "&response_type=code" +
                "&redirect_uri=" + discord.getRedirectUri() +
                "&scope=identify+connections";

        exchange.getResponseHeaders().set("Location", discordAuth);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {

            final String discordTokenURL = "https://discord.com/api/oauth2/token";
            final String discordMeURL = "https://discord.com/api/users/@me";
            String generalChatURL = "https://discord.com/channels/" + ServerGuild.OJC.getId() + "/" + ChannelID.ZATUDAN.getId();

            Map<String, String> params = ParseQuery.parse(exchange.getRequestURI().getQuery());
            Discord discord = new Discord();
            String accessToken;
            String discordCode = params.get("code");
            String clientSecret = discord.getClientSecret();
            String clientId = discord.getClientId();
            String redirectUri = discord.getRedirectUri();
            String form;
            String ip = GetClientIP.getIP(exchange);

            // Discordのコードが存在するか確認
            if (discordCode == null) {
                reLogin(exchange, discord);
                Logger.log("OAuthレスポンスにDiscordのコードが含まれていません。", Level.WARN);
                return;
            }

            // osu!側で認証が完了しているか確認
            if (!Main.linkCache.containsKey(ip)) {
                reLogin(exchange, discord);
                Logger.log("osu!側で認証が完了していません。", Level.WARN);
                return;
            }

            Link link = Main.linkCache.get(ip);

            form = FormURLEncoder.encode(
                    Map.of(
                            "client_id", String.valueOf(clientId),
                            "client_secret", clientSecret,
                            "code", discordCode,
                            "grant_type", "authorization_code",
                            "redirect_uri", redirectUri
                    )
            );

            HttpRequest request = HttpRequest.newBuilder(URI.create(discordTokenURL))
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                reLogin(exchange, discord);
                Logger.log("Discord OAuthトークンの取得に失敗しました: " + response.body(), Level.WARN);
                return;
            }

            ObjectMapper mapper = new ObjectMapper();

            JsonNode tokenJson = mapper.readTree(response.body());
            accessToken = tokenJson.get("access_token").asText(null);

            if (accessToken == null) {
                reLogin(exchange, discord);
                Logger.log("Discord OAuthレスポンスにアクセストークンが含まれていません: " + response.body(), Level.WARN);
                return;
            }

            HttpRequest meRequest = HttpRequest.newBuilder(URI.create(discordMeURL))
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + accessToken)
                    .GET()
                    .build();
            HttpResponse<String> meResponse = HttpClient.newHttpClient().send(meRequest, HttpResponse.BodyHandlers.ofString());

            // Discordユーザー情報の取得に失敗 (再ログイン)
            if (meResponse.statusCode() != 200) {
                reLogin(exchange, discord);
                Logger.log("Discordユーザー情報の取得に失敗しました: " + meResponse.body(), Level.WARN);
                return;
            }

            JSONObject meJson = new JSONObject(meResponse.body());

            long userId = meJson.getLong("id");

            JDA jda = Main.bot.getJda();
            MySQL mySQL = new MySQL();
            Member verifiedMember;

            verifiedMember = Objects.requireNonNull(jda.getGuildById(ServerGuild.OJC.getId())).getMemberById(userId);

            // 再ログイン
            if (verifiedMember == null) {
                reLogin(exchange, discord);
                Logger.log("Discordサーバー内にユーザーが見つかりません: id=" + userId, Level.WARN);
                return;

            }

            // 検証成功

            mySQL.saveLinkData(verifiedMember, link);

            Objects.requireNonNull(jda.getGuildById(ServerGuild.OJC.getId())).addRoleToMember(
                verifiedMember, ServerRole.MEMBER.getRole()
            ).queue();

            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(ServerGuild.OJC.getId())).getTextChannelById(ChannelID.VERIFICATION_LOG.getId()))
                            .sendMessage("認証成功: " + verifiedMember.getEffectiveName() + " -> " + link.getBanchoName() + " (" + ip + ")").queue();

            Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(ServerGuild.OJC.getId())).getTextChannelById(ChannelID.WELCOME.getId()))
                            .sendMessage(verifiedMember.getAsMention() + " さん、よろしくお願いします :grin:").queue();

            // リダイレクト
            exchange.getResponseHeaders().set("Location", generalChatURL);
            exchange.sendResponseHeaders(302, -1);
            exchange.close();

            Main.linkCache.remove(ip);

            Logger.log("Discordユーザーと連携しました: id=" + userId + ", username=" + verifiedMember.getUser().getAsTag(), Level.INFO);

            PrintRequest.print(exchange);

        } catch (Exception e) {
            Logger.log("Discord連携中にエラーが発生しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
