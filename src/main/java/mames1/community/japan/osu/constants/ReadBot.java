package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ReadBot {

    SHOVEL(533698325203910668L),
    VOICE_MASTER(472911936951156740L),
    ALBOT(727508841368911943L);

    final long id;

    ReadBot(long id) {
        this.id = id;
    }
}
