package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.utils.discord.voicechat.audio.PlayerManager;
import mames1.community.japan.osu.utils.discord.voicechat.reader.HonorificNameGenerator;
import mames1.community.japan.osu.utils.discord.voicechat.reader.VoiceGenerator;
import mames1.community.japan.osu.utils.discord.voicechat.reader.WavPathGenerator;
import mames1.community.japan.osu.utils.file.ResponseByteSave;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.Objects;

public class MemberUpdate extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent e) {
        long id = Main.bot.getVoiceQueueID() + 1;
        long channelId = Main.voiceChat.getChannelId();
        String name;
        HttpResponse<byte[]> response;
        StringBuilder resultText = new StringBuilder();
        Path path = WavPathGenerator.getWavPath(id);

        if (e.getMember().getUser().isBot()) {
            return;
        }

        if (e.getChannelLeft() == null && e.getChannelJoined() == null) {
            return;
        }

        try {

            name = HonorificNameGenerator.getName(e.getMember());

            if (e.getChannelLeft() == null || e.getChannelJoined() != null) {

                if (Objects.requireNonNull(e.getChannelJoined()).getIdLong() != channelId) {
                    return;
                }

                resultText.append(name).append("が参加したのだ！こんにちは。");

                Logger.log("参加: " + e.getMember().getEffectiveName(), Level.INFO);
            } else {

                if(e.getChannelLeft().getIdLong() != channelId) {
                    return;
                }

                resultText.append(name).append("が退出したのだ！お疲れ様でした。");

                Logger.log("退出: " + e.getMember().getEffectiveName(), Level.INFO);
            }

            response = VoiceGenerator.generate(resultText.toString());
            boolean isSaved = ResponseByteSave.save(response, path);

            if (isSaved) {
                Main.bot.setVoiceQueueID(id);
                PlayerManager.getManager().loadAndPlay(e.getGuild(), path.toString());
            }

        } catch (Exception ex) {
            Logger.log("VC参加時の読み上げ処理でエラーが発生しました: " + ex.getMessage(), Level.ERROR);
        }
    }
}
