package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ChannelID {
    VERIFY(1316056673177178213L),
    ROLE(1091053198925627432L),
    ZATUDAN(1090165483455664209L),
    KIKISEN(1089160068689309713L),
    VC1(1090163808556818552L),
    VC2(1090163840928468992L),
    VERIFICATION_LOG(1316076149239054336L),
    WELCOME(1089160068454424634L);

    final long id;

    ChannelID(long id) {
        this.id = id;
    }
}
