package mames1.community.japan.osu.utils.discord.embed;

import mames1.community.japan.osu.constants.ServerEmoji;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public abstract class LoginEmbed {

    public static EmbedBuilder getEmbed() {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(ServerEmoji.WARNING.getId() + " 連携してください");
        embed.setDescription("""
                このサーバーを利用するには、Banchoアカウントとの連携が必要です。
                以下のボタンをクリックし、お使いのBanchoアカウントでログインを行ってください。
                連携が完了すると、このサーバーのすべての機能を利用できるようになります。""");
        embed.setColor(Color.RED);
        embed.setFooter("Osu! Japan Community");

        return embed;
    }
}
