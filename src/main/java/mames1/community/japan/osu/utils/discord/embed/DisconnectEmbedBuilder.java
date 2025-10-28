package mames1.community.japan.osu.utils.discord.embed;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.constants.ServerEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.awt.*;
import java.util.Date;
import java.util.Objects;

public abstract class DisconnectEmbedBuilder {

    public static EmbedBuilder getDisconnectEmbed() {

        EmbedBuilder disconnectEmbed = new EmbedBuilder();
        JDA jda = Main.bot.getJda();
        VoiceChannel vc1 = jda.getVoiceChannelById(ChannelID.VC1.getId());
        VoiceChannel vc2 = jda.getVoiceChannelById(ChannelID.VC2.getId());

        disconnectEmbed.setTitle(ServerEmoji.X.getId() + " 切断完了");
        disconnectEmbed.setDescription("また呼んでくれるのを待っているのだ！\n" +
                Objects.requireNonNull(vc1).getAsMention() + "か" + Objects.requireNonNull(vc2).getAsMention() + "でミュートをすると再接続するのだ！");
        disconnectEmbed.setColor(Color.RED);
        disconnectEmbed.setTimestamp(new Date().toInstant());

        return disconnectEmbed;
    }
}
