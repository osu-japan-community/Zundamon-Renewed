package mames1.community.japan.osu.utils.file;

import mames1.community.japan.osu.utils.log.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;

import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ResponseByteSaver {

    public static boolean save(HttpResponse<byte[]> r, Path path) {
        try {
            if (r.statusCode() == 200) {
                Files.write(path, r.body());
                return true;
            }
            return false;
        } catch (Exception e) {
            AppLogger.log("ファイル保存中にエラーが発生しました: " + e.getMessage(), LogLevel.ERROR);
            return false;
        }
    }
}
