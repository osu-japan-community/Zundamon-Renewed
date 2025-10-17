package mames1.community.japan.osu.utils.discord.embed;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.constants.ServerEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public abstract class ConnectEmbed {

    public static EmbedBuilder getConnectedEmbed(VoiceChannel voiceChannel) {

        EmbedBuilder successEmbed = new EmbedBuilder();
        JDA jda = Main.bot.getJda();
        TextChannel channel = jda.getTextChannelById(ChannelID.KIKISEN.getId());

        successEmbed.setTitle(ServerEmoji.CHECK.getId() + " 接続成功");
        successEmbed.setDescription(voiceChannel.getAsMention() + "に呼んでくれてありがとうなのだ！\n" +
                Objects.requireNonNull(channel).getAsMention() + " に書かれたメッセージを僕が読み上げるのだ！\n" +
                "``!disconnect`` で切断できるのだ！");
        successEmbed.setColor(Color.GREEN);
        successEmbed.setTimestamp(new Date().toInstant());

        return successEmbed;
    }
}
