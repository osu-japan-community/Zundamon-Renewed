package mames1.community.japan.osu.utils.file;

import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class FileEnsure {

    public static boolean ensureFile (Path file) {

        try {
            Path parent = file.getParent();

            if (parent != null && Files.notExists(parent)) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(file)) {
                Files.createFile(file);
                Logger.log("新規のログファイル: " + file + "を作成しました.", Level.INFO);
            }
        } catch (Exception e) {
            Logger.log("ファイル作成中にエラーが発生しました: " + e.getMessage(), Level.ERROR);
            return false;
        }

        return true;
    }
}