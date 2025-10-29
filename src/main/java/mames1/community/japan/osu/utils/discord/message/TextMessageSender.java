package mames1.community.japan.osu.utils.discord.message;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Objects;

public abstract class TextMessageSender {

    public static void sendMessage(Guild guild, TextChannel channel, String message) {
        JDA jda = guild.getJDA();

        Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild.getIdLong())).getTextChannelById(channel.getIdLong())).sendMessage(message).queue();
    }

    public static void sendEmbed(Guild guild, TextChannel channel, net.dv8tion.jda.api.EmbedBuilder embed) {
        JDA jda = guild.getJDA();

        Objects.requireNonNull(Objects.requireNonNull(jda.getGuildById(guild.getIdLong())).getTextChannelById(channel.getIdLong())).sendMessageEmbeds(embed.build()).queue();
    }
    
}
