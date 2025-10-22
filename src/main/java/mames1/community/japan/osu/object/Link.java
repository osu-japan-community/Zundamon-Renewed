package mames1.community.japan.osu.object;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Link {

    long banchoId;
    String banchoName;

    public Link(long banchoId, String banchoName) {
        this.banchoId = banchoId;
        this.banchoName = banchoName;
    }
}
