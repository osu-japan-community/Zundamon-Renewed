package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.utils.discord.voicechat.audio.PlayerManager;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotVoiceDisconnectListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent e) {
        // Bot自身のボイス状態のみ監視
        if (!e.getMember().equals(e.getGuild().getSelfMember())) {
            return;
        }
        // 切断（どのVCにも参加していない状態）になったときだけ実行
        if (e.getChannelLeft() != null && e.getChannelJoined() == null) {
            PlayerManager.getManager().stopAndClear(e.getGuild());
            // VoiceChat状態もリセット
            Main.voiceChat.setActive(false);
            Main.voiceChat.setChannelId(0L);

            AppLogger.log("BotがVCから切断されました。再生中の音声を停止し、キューをクリアしました。", LogLevel.INFO);
        }
    }
}