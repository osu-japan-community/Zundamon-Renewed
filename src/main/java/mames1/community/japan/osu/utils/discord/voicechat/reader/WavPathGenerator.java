package mames1.community.japan.osu.utils.discord.voicechat.reader;

import java.nio.file.Path;

public abstract class WavPathGenerator {

    public static Path getWavPath(long voiceId) {

        return Path.of(voiceId + ".wav");
    }
}
