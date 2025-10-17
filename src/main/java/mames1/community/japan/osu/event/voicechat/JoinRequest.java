package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.constants.ServerEmoji;
import mames1.community.japan.osu.utils.discord.voicechat.JoinedUserChecker;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.awt.*;
import java.util.Date;

public class JoinRequest extends ListenerAdapter {

    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent e) {

        JDA jda = e.getJDA();

        if (e.getVoiceState().getChannel() == null) {
            return;
        }

        if(e.getVoiceState().getChannel().getIdLong() != ChannelID.VC1.getId()) {
            return;
        }

        if(JoinedUserChecker.isReadBotJoined(e.getVoiceState())) {
            return;
        }

        if (JoinedUserChecker.isSelfJoined(e.getVoiceState())) {
            return;
        }

        AudioManager audioManager = e.getGuild().getAudioManager();
        TextChannel channel = jda.getTextChannelById(ChannelID.KIKISEN.getId());

        if (channel == null) {
            return;
        }

        EmbedBuilder successEmbed = new EmbedBuilder();
        successEmbed.setTitle(ServerEmoji.CHECK.getId() + " 接続成功");
        successEmbed.setDescription("呼んでくれてありがとうなのだ！\n" +
                channel.getAsMention() + " に書かれたメッセージを僕が読み上げるのだ！");
        successEmbed.setColor(Color.GREEN);
        successEmbed.setTimestamp(new Date().toInstant());

        audioManager.openAudioConnection(e.getVoiceState().getChannel());

        Main.bot.setVoiceConnected(true);

        channel.sendMessageEmbeds(successEmbed.build()).queue();
    }
}
