package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ChannelID {
    KIKISEN(1089160068689309713L),
    VC1(1090163808556818552L),
    VC2(1090163840928468992L);

    final long id;

    ChannelID(long id) {
        this.id = id;
    }
}
