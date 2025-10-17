package mames1.community.japan.osu.utils.file;

import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public class PathEnsure {

    // Path.ofを使用してdirを渡す
    public static boolean ensureDirectory (Path dir) {

        try {
            if (Files.notExists(dir)) {
                Files.createDirectories(dir);
                Logger.log("新規のログフォルダ: " + dir + "を作成しました.", Level.INFO);
            }
        } catch (Exception e) {
            Logger.log("フォルダ作成中にエラーが発生しました: " + e.getMessage(), Level.ERROR);
            return false;
        }

        return true;
    }
}