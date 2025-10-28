package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.discord.embed.DisconnectEmbedBuilder;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoiceDisconnectRequestListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        boolean isVoiceConnected= Main.voiceChat.isActive();

        if (!isVoiceConnected) {
            return;
        }

        if (Objects.requireNonNull(e.getMember()).getVoiceState() == null) {
            return;
        }

        if (!e.getMember().getVoiceState().inAudioChannel()) {
            return;
        }

        if (e.getMember().getVoiceState().getChannel() == null) {
            return;
        }

        if (e.getMember().getVoiceState().getChannel().getIdLong() == ChannelID.VC1.getId() ||
        e.getMember().getVoiceState().getChannel().getIdLong() == ChannelID.VC2.getId()) {

            if (e.getMessage().getContentRaw().equalsIgnoreCase("!disconnect")) {

                e.getMessage().replyEmbeds(
                        DisconnectEmbedBuilder.getDisconnectEmbed().build()
                ).queue();

                e.getGuild().getAudioManager().closeAudioConnection();

                Main.voiceChat.setActive(false);
                Main.voiceChat.setChannelId(0L);

                AppLogger.log(e.getMember().getEffectiveName() + "のコマンドにより、VCから切断しました。", LogLevel.INFO);
            }

        }
    }
}
