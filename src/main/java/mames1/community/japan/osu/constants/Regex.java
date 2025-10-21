package mames1.community.japan.osu.constants;

import lombok.Getter;

@Getter
public enum Regex {

    URL("(https?://[^\\s]+)"),
    UNICODE("[\\p{Punct}\\p{S}]"),
    JAPANESE("[\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}\\u30FC\\uFF66-\\uFF9D\\u3000-\\u303F]");

    final String pattern;

    Regex(String pattern) {
        this.pattern = pattern;
    }
}
