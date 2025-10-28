package mames1.community.japan.osu.utils.discord.voicechat.reader;

import com.moji4j.MojiConverter;
import mames1.community.japan.osu.constants.Regex;
import mames1.community.japan.osu.utils.log.LogLevel;
import mames1.community.japan.osu.utils.log.AppLogger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UrlTitleReplacer {

    public static String replaceUrlWithTitleInMessage(String message) {
        MojiConverter converter = new MojiConverter();
        Pattern urlPattern = Pattern.compile(Regex.URL.getPattern());
        Matcher matcher = urlPattern.matcher(message);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String link = matcher.group(1);
            String replacement;
            try {
                Document document = Jsoup.connect(link).get();
                String jpTitle = converter.convertRomajiToHiragana(document.title());
                replacement = jpTitle + "のURL。";
            } catch (Exception e) {
                replacement = "不明なウェブサイトのURL。";
                AppLogger.log("リンクの内容を取得中にエラーが発生しました: " + e.getMessage(), LogLevel.ERROR);
            }
            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(sb);

        String result = sb.toString();
        result = result.replaceAll(Regex.UNICODE.getPattern(), "");
        return result;
    }
}
