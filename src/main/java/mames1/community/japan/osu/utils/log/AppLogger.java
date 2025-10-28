package mames1.community.japan.osu.utils.log;

import mames1.community.japan.osu.utils.date.Date;

public abstract class AppLogger {

    public static void log(String message, LogLevel level) {

        String log = Date.now() + " [Zundamon] [" + level + "] " + message;

        if (level.equals(LogLevel.ERROR) || level.equals(LogLevel.FATAL)) {
            LogSaver.save(log + System.lineSeparator(), level);
        }

        System.out.println(log);
    }
}