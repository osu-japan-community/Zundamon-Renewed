package mames1.community.japan.osu.object;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Link {

    long banchoId;
    long lastRequestTime;
    String banchoName;

    public Link(long banchoId, String banchoName, long lastRequestTime) {
        this.banchoId = banchoId;
        this.lastRequestTime = lastRequestTime;
        this.banchoName = banchoName;
    }
}
