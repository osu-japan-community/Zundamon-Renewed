package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.discord.embed.ConnectEmbedBuilder;
import mames1.community.japan.osu.utils.discord.voicechat.JoinedUserChecker;
import mames1.community.japan.osu.utils.log.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceJoinRequestListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent e) {

        JDA jda = e.getJDA();
        boolean isVoiceConnected= Main.voiceChat.isActive();

        if (e.getVoiceState().getChannel() == null) {
            return;
        }

        if(JoinedUserChecker.isReadBotJoined(e.getVoiceState())) {
            return;
        }

        if (isVoiceConnected) {
            return;
        }

        // VC1かVC2に接続した場合のみ反応
        if (e.getVoiceState().getChannel().getIdLong() == ChannelID.VC1.getId() ||
            e.getVoiceState().getChannel().getIdLong() == ChannelID.VC2.getId()) {

            AudioManager audioManager = e.getGuild().getAudioManager();
            TextChannel channel = jda.getTextChannelById(ChannelID.KIKISEN.getId());

            if (channel == null) {
                return;
            }

            channel.sendMessageEmbeds(ConnectEmbedBuilder.getConnectedEmbed(e.getVoiceState().getChannel().asVoiceChannel()).build()).queue();

            audioManager.openAudioConnection(e.getVoiceState().getChannel());

            Main.voiceChat.setActive(true);
            Main.voiceChat.setChannelId(e.getVoiceState().getChannel().getIdLong());

            AppLogger.log("VCに接続しました。接続先: " + e.getVoiceState().getChannel().getName(), LogLevel.INFO);
        }
    }
}
