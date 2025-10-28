package mames1.community.japan.osu.utils.file;

import mames1.community.japan.osu.constants.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class PathEnsurer {

    // Path.ofを使用してdirを渡す
    public static boolean ensureDirectory (Path dir) {

        try {
            if (Files.notExists(dir)) {
                Files.createDirectories(dir);
                AppLogger.log("新規のログフォルダ: " + dir + "を作成しました.", LogLevel.INFO);
            }
        } catch (Exception e) {
            AppLogger.log("フォルダ作成中にエラーが発生しました: " + e.getMessage(), LogLevel.ERROR);
            return false;
        }

        return true;
    }
}