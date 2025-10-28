package mames1.community.japan.osu;

import mames1.community.japan.osu.object.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static Bot bot;
    public static Map<String, Link> linkCache;
    public static List<String> banWords;
    public static VoiceChat voiceChat;

    public static void main(String[] args) {

        Server server = new Server();
        BannedWord banWord = new BannedWord();
        bot = new Bot();
        voiceChat = new VoiceChat();
        linkCache = new HashMap<>();

        banWords = banWord.getBanWords();
        bot.start();
        server.start();
    }
}
