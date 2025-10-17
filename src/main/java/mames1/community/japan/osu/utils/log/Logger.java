package mames1.community.japan.osu.utils.log;

import mames1.community.japan.osu.utils.date.Date;

public abstract class Logger {

    public static void log(String message, Level level) {

        String log = Date.now() + " [Zundamon] [" + level + "] " + message;

        if (level.equals(Level.ERROR) || level.equals(Level.FATAL)) {
            LogSave.save(log + System.lineSeparator(), level);
        }

        System.out.println(log);
    }
}