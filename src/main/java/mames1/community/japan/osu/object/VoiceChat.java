package mames1.community.japan.osu.object;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VoiceChat {

    long channelId;
    boolean isActive;

    public VoiceChat() {
        isActive = false;
        channelId = 0L;
    }
}
