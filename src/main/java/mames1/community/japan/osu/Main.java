package mames1.community.japan.osu;

import mames1.community.japan.osu.object.Bot;
import mames1.community.japan.osu.object.Link;
import mames1.community.japan.osu.object.Server;
import mames1.community.japan.osu.object.VoiceChat;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static Bot bot;
    public static Map<String, Link> linkCache;
    public static VoiceChat voiceChat;

    public static void main(String[] args) {

        Server server = new Server();
        bot = new Bot();
        voiceChat = new VoiceChat();
        linkCache = new HashMap<>();
        bot.start();
        server.start();
    }
}
