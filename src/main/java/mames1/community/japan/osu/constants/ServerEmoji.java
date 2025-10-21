package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ServerEmoji {

    CHECK("<:check:1305395371169546280>"),
    X("<:x_:1305398003904942120>"),
    WARNING("<:warning:1305402733200932894>"),
    PEOPLE("<:people:1316704452060643348>"),
    BOOK("<:book:1316753856557744299>");

    final String id;

    ServerEmoji(String id) {
        this.id = id;
    }
}
