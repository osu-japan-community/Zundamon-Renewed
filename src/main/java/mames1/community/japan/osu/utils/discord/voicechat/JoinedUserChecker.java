package mames1.community.japan.osu.utils.discord.voicechat;

import mames1.community.japan.osu.constants.ReadBot;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Objects;

public abstract class JoinedUserChecker {

    public static boolean isReadBotJoined(GuildVoiceState state) {

        List<Long> readBotIds = List.of(
                ReadBot.ALBOT.getId(),
                ReadBot.SHOVEL.getId(),
                ReadBot.VOICE_MASTER.getId()
        );

        for (Member member : Objects.requireNonNull(state.getChannel()).getMembers()) {
            if (readBotIds.contains(member.getIdLong())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSelfJoined(GuildVoiceState state) {

        for (Member member : Objects.requireNonNull(state.getChannel()).getMembers()) {
            if (member.getUser().isBot() && member.getIdLong() == state.getJDA().getSelfUser().getIdLong()) {
                return true;
            }
        }

        return false;
    }
}
