package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.discord.embed.DisconnectEmbed;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DisconnectRequest extends ListenerAdapter {

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
                        DisconnectEmbed.getDisconnectEmbed().build()
                ).queue();

                e.getGuild().getAudioManager().closeAudioConnection();

                Main.voiceChat.setActive(false);
                Main.voiceChat.setChannelId(0L);
                Logger.log("VCから切断しました。", Level.INFO);
            }

        }
    }
}
