package mames1.community.japan.osu.object;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;

@Getter @Setter
public class Link {

    Member member;
    long banchoId;
    long lastRequestTime;
    String banchoName;

    public Link(Member member) {
        this.member = member;
    }
}
