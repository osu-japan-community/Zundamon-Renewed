package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.discord.embed.DisconnectEmbedBuilder;
import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VoiceAutoDisconnectListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent e) {

        boolean isVoiceConnected= Main.voiceChat.isActive();

        if (!isVoiceConnected) {
            return;
        }

        if (e.getChannelLeft() == null && e.getChannelJoined() == null) {
            return;
        }

        if (e.getChannelJoined() != null ) {
            return;
        }

        if (e.getGuild().getSelfMember().getVoiceState() == null || e.getGuild().getSelfMember().getVoiceState().getChannel() == null) {
            return;
        }

        if (e.getGuild().getSelfMember().getVoiceState().getChannel().getIdLong() != e.getChannelLeft().getIdLong()) {
            return;
        }

        boolean existsUser = e
                .getChannelLeft()
                .getMembers()
                .stream()
                .anyMatch(member -> !member.getUser().isBot());

        if (!existsUser) {
            e.getGuild().getAudioManager().closeAudioConnection();
            Objects.requireNonNull(e.getJDA().getTextChannelById(ChannelID.KIKISEN.getId())).sendMessageEmbeds(
                    DisconnectEmbedBuilder.getDisconnectEmbed().build()
            ).queue();
            Main.voiceChat.setActive(false);

            AppLogger.log("VCに誰もいなくなった為、自動切断しました。", LogLevel.INFO);
        }
    }
}
