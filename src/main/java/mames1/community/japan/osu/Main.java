package mames1.community.japan.osu;

import mames1.community.japan.osu.object.Bot;
import mames1.community.japan.osu.object.Server;
import mames1.community.japan.osu.object.VoiceChat;

public class Main {

    public static Bot bot;
    public static VoiceChat voiceChat;

    public static void main(String[] args) {

        Server server = new Server();
        bot = new Bot();
        voiceChat = new VoiceChat();
        bot.start();
        server.start();
    }
}
