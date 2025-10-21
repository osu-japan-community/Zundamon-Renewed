package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum ServerGuild {

    OJC(1089160066797686846L);

    final long id;

    ServerGuild(long id) {
        this.id = id;
    }
}
