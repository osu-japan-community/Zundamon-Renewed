package mames1.community.japan.osu.event.manage;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.object.MySQL;
import mames1.community.japan.osu.utils.log.AppLogger;
import mames1.community.japan.osu.constants.LogLevel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddBannedWordListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getChannel().getIdLong() != ChannelID.BANWORD_ADD.getId()) {
            return;
        }

        MySQL mysql = new MySQL();
        String content = e.getMessage().getContentRaw().toLowerCase();

        if(!Main.banWords.contains(content)) {

            mysql.addBanwords(content);

            e.getMessage().addReaction(
                    Emoji.fromUnicode("U+2705")
            ).queue();

            AppLogger.log("BannedWordを追加しました: " + content, LogLevel.INFO);

            return;
        }

        e.getMessage().addReaction(
                Emoji.fromUnicode("U+274C")
        ).queue();

        AppLogger.log("既に登録されているBannedWordです: " + content, LogLevel.INFO);
    }
}
