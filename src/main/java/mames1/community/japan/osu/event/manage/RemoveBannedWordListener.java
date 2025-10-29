package mames1.community.japan.osu.event.manage;

import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.object.MySQL;
import mames1.community.japan.osu.utils.log.AppLogger;
import mames1.community.japan.osu.constants.LogLevel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RemoveBannedWordListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getChannel().getIdLong() != ChannelID.BANWORD_REMOVE.getId()) {
            return;
        }

        MySQL mysql = new MySQL();
        String content = e.getMessage().getContentRaw().toLowerCase();

        mysql.removeBanwords(content);

        e.getMessage().addReaction(Emoji.fromUnicode("U+2705")).queue();

        AppLogger.log("BannedWordを削除しました: " + content, LogLevel.INFO);
    }
}
