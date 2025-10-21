package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ServerEmoji {

    CHECK("<:check:1305395371169546280>"),
    X("<:x_:1305398003904942120>"),
    WARNING("<:warning:1305402733200932894>"),
    PEOPLE("<:people:1316704452060643348>"),
    BOOK("<:book:1316753856557744299>"),
    OSU("<:modeosu2x:1091054411276615700>"),
    TAIKO("<:modetaiko2x:1091054413096964157>"),
    CATCH("<:modefruits2x:1091054408437071923>"),
    MANIA("<:modemania2x:1091054415781302275>"),
    LOUD_SOUND("U+1F50A"),
    VIDEO_GAME("U+1F3AE"),
    PAINT_BRUSH("U+1F58C"),
    MAP("U+1F5FA"),
    NOTES("U+1F3B6"),
    ART("U+1F3A8"),
    FLAG_WHITE("U+1F3F3"),
    FLAG_BLACK("U+1F3F4"),;

    final String id;

    ServerEmoji(String id) {
        this.id = id;
    }
}
