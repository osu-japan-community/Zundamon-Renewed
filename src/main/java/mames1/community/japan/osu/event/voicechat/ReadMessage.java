package mames1.community.japan.osu.event.voicechat;

import mames1.community.japan.osu.Main;
import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.utils.discord.voicechat.audio.PlayerManager;
import mames1.community.japan.osu.utils.discord.voicechat.reader.HonorificNameGenerator;
import mames1.community.japan.osu.utils.discord.voicechat.reader.URLTitleReplacer;
import mames1.community.japan.osu.utils.discord.voicechat.reader.VoiceGenerator;
import mames1.community.japan.osu.utils.discord.voicechat.reader.WavPathGenerator;
import mames1.community.japan.osu.utils.file.ResponseByteSave;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ReadMessage extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        boolean isVoiceConnected = Main.voiceChat.isActive();
        HttpResponse<byte[]> response;
        StringBuilder readText = new StringBuilder();
        String finalText;
        long voiceQueueId = Main.bot.getVoiceQueueID() + 1;

        if (!isVoiceConnected) {
            return;
        }

        if(e.getAuthor().isBot()) {
            return;
        }

        if(e.getChannel().getIdLong() != ChannelID.KIKISEN.getId()) {
            return;
        }

        if(Objects.requireNonNull(e.getMember()).getVoiceState() == null) {
            return;
        }

        if (!e.getMessage().getContentRaw().isEmpty()) {
            readText.append(URLTitleReplacer.replaceUrlWithTitleInMessage(e.getMessage().getContentRaw()))
                    .append("。");
        }

        String name = HonorificNameGenerator.getName(e.getMember());

        if(!e.getMessage().getAttachments().isEmpty()) {
            List<Message.Attachment> attachments = e.getMessage().getAttachments();
            int imageCount = 0;
            int videoCount = 0;

            Logger.log("添付ファイルを検出: " + attachments.size() + "個", Level.INFO);

            for (Message.Attachment attachment : attachments) {
                if (attachment.isImage()) {
                    imageCount++;
                } else if (attachment.isVideo()) {
                    videoCount++;
                } else {
                    readText.append(attachment.getFileExtension()).append("形式のファイルを添付。");
                }
            }

            if (imageCount > 0) {
                readText.append(imageCount).append("個の画像を添付。");
            }

            if (videoCount > 0) {
                readText.append(videoCount).append("個の動画を添付。");
            }
        }

        finalText = name + readText;

        if(finalText.length() > 256) {
            finalText = finalText.substring(0, 256) + "、以下省略するのだ";
        }

        response = VoiceGenerator.generate(finalText);

        Path path = WavPathGenerator.getWavPath(voiceQueueId);
        boolean isSuccess = ResponseByteSave.save(response, path);

        if(isSuccess) {
            Main.bot.setVoiceQueueID(voiceQueueId);
            PlayerManager.getManager().loadAndPlay(e.getGuild(), path.toString());
        }
    }
}
