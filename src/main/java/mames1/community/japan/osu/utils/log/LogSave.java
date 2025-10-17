package mames1.community.japan.osu.utils.log;

import mames1.community.japan.osu.utils.date.Date;
import mames1.community.japan.osu.utils.file.FileEnsure;
import mames1.community.japan.osu.utils.file.PathEnsure;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class LogSave {

    public static void save(String message, Level level) {

        if(PathEnsure.ensureDirectory(Path.of("logs"))) {

            String dateFormatted = Date.now().replace(" ", "_").replace(":", "-");
            Path logFilePath = Path.of("logs",  level.name() + "_" + dateFormatted + ".log");

            if (FileEnsure.ensureFile(logFilePath)) {
                try {
                    Files.writeString(logFilePath, message, StandardOpenOption.APPEND);
                } catch (Exception e) {
                    // 再帰対策
                    System.out.println("ログの保存中にエラーが発生しました: " + e.getMessage());
                }
            }
        }
    }
}