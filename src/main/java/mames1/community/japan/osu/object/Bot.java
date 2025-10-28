package mames1.community.japan.osu.object;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import mames1.community.japan.osu.event.manage.AddBannedWordListener;
import mames1.community.japan.osu.event.manage.BannedWordReceiveListener;
import mames1.community.japan.osu.event.manage.RemoveBannedWordListener;
import mames1.community.japan.osu.event.oauth.OAuthAuthorizationListener;
import mames1.community.japan.osu.event.role.RoleAssignmentListener;
import mames1.community.japan.osu.event.voicechat.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Getter
@Setter
public class Bot {
    long voiceQueueID = 0L;
    String token;
    JDA jda;

    public Bot() {
        Dotenv dotenv = Dotenv.configure().load();

        token = dotenv.get("BOT_TOKEN");
    }

    public void start() {
        jda = JDABuilder.createDefault(token)
                .setRawEventsEnabled(true)
                .enableIntents(
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_VOICE_STATES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS
                ).enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI
                )
                .disableCache(
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ).setActivity(
                        Activity.listening("osu!")
                ).setMemberCachePolicy(
                        MemberCachePolicy.ALL
                ).setChunkingFilter(
                        ChunkingFilter.ALL
                ).addEventListeners(
                        // ロールの付与機能
                        new RoleAssignmentListener(),
                        // 読み上げ機能
                        new VoiceJoinRequestListener(),
                        new VoiceDisconnectRequestListener(),
                        new VoiceMessageReadListener(),
                        //参加
                        new VoiceMemberUpdateListener(),
                        // 退出
                        new VoiceAutoDisconnectListener(),
                        new BotVoiceDisconnectListener(),
                        // 認証機能
                        new OAuthAuthorizationListener(),
                        // バンワード
                        new BannedWordReceiveListener(),
                        new AddBannedWordListener(),
                        new RemoveBannedWordListener()
                )
                .build();
    }
}
