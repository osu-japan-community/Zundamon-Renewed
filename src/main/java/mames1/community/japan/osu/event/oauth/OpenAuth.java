package mames1.community.japan.osu.event.oauth;

import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.object.Bancho;
import mames1.community.japan.osu.utils.discord.embed.LoginEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Objects;

public class OpenAuth extends ListenerAdapter {

    // 認証用メッセージ作成
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getChannel().getIdLong() != ChannelID.VERIFY.getId()) {
            return;
        }

        if (Objects.requireNonNull(e.getMember()).getUser().isBot()) {
            return;
        }

        if(!e.getMessage().getContentRaw().equals("create")) {
            return;
        }

        Bancho bancho = new Bancho();

        int clientId = bancho.getClientId();
        String redirectUri = bancho.getRedirectUri();

        String authUrl = "https://osu.ppy.sh/oauth/authorize?client_id=" +
                clientId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=public+identify";

        e.getMessage().getChannel().sendMessage("# OJCへようこそ！")
                .addEmbeds(LoginEmbed.getEmbed().build())
                .setActionRow(Button.link(authUrl, "認証する"))
                .queue();
    }
}
