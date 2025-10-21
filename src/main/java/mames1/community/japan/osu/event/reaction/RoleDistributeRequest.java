package mames1.community.japan.osu.event.reaction;

import mames1.community.japan.osu.constants.ChannelID;
import mames1.community.japan.osu.constants.ServerEmoji;
import mames1.community.japan.osu.constants.ServerGuild;
import mames1.community.japan.osu.constants.ServerRole;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.Objects;

public class RoleDistributeRequest extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {

        RestAction<Member> member = e.retrieveMember();

        if(Objects.requireNonNull(e.getUser()).isBot()) {
            return;
        }

        if(e.getGuild().getIdLong() != ServerGuild.OJC.getId()) {
            return;
        }

        if(e.getChannel().getIdLong() != ChannelID.ROLE.getId()) {
            return;
        }

        if(e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.OSU.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.STD.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.TAIKO.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.TAIKO.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.CATCH.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.CATCH.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.MANIA.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.MANIA.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.VIDEO_GAME.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.MULTI.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.LOUD_SOUND.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.TUUWA.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.PAINT_BRUSH.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.SKINNER.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.MAP.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.MAPPER.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.NOTES.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.SAKKYOKU.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.ART.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.ESI.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.FLAG_WHITE.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.FOURK.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.FLAG_BLACK.getId()))) {
            e.getGuild().addRoleToMember(member.complete(), ServerRole.SEVENK.getRole()).queue();
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
        RestAction<Member> member = e.retrieveMember();

        if(Objects.requireNonNull(e.getUser()).isBot()) {
            return;
        }

        if(e.getGuild().getIdLong() != ServerGuild.OJC.getId()) {
            return;
        }

        if(e.getChannel().getIdLong() != ChannelID.ROLE.getId()) {
            return;
        }

        if(e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.OSU.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.STD.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.TAIKO.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.TAIKO.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.CATCH.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.CATCH.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromFormatted(ServerEmoji.MANIA.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.MANIA.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.VIDEO_GAME.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.MULTI.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.LOUD_SOUND.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.TUUWA.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.PAINT_BRUSH.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.SKINNER.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.MAP.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.MAPPER.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.NOTES.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.SAKKYOKU.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.ART.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.ESI.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.FLAG_WHITE.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.FOURK.getRole()).queue();
        }
        else if (e.getReaction().getEmoji().equals(Emoji.fromUnicode(ServerEmoji.FLAG_BLACK.getId()))) {
            e.getGuild().removeRoleFromMember(member.complete(), ServerRole.SEVENK.getRole()).queue();
        }
    }
}
