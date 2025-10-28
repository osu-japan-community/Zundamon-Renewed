package mames1.community.japan.osu.event.manage;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.log.AppLogger;
import mames1.community.japan.osu.utils.log.LogLevel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class BannedWordReceiveListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        List<String> banWords = Main.banWords;
        String messageContent = e.getMessage().getContentRaw();

        if(banWords == null || banWords.isEmpty()) {
            return;
        }

        if (e.getChannel().getIdLong() == ChannelID.BANWORD_REMOVE.getId()) {
            return;
        }

        if (e.getChannel().getIdLong() == ChannelID.BANWORD_ADD.getId()) {
            return;
        }

        if(banWords.contains(messageContent.toLowerCase())) {
            e.getMessage().delete().queue();
        }

        AppLogger.log("Banwordを検出しました: " + messageContent, LogLevel.INFO);
    }
}
