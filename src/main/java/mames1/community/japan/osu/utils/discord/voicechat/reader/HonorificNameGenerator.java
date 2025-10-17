package mames1.community.japan.osu.utils.discord.voicechat.reader;

import com.moji4j.MojiConverter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;

public abstract class HonorificNameGenerator {

    public static String getName(Member member) {

        String userName = member.getEffectiveName();
        JDA jda = member.getJDA();

        if (member.getNickname() != null) {
            userName = member.getNickname();
        }

        MojiConverter converter = new MojiConverter();
        String jpName = converter.convertRomajiToHiragana(userName);

        if (member.getIdLong() != jda.getSelfUser().getIdLong()) {
            jpName += "さん";
        }

        return jpName + "、";
    }
}
