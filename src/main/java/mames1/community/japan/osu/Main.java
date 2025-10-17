package mames1.community.japan.osu;

import mames1.community.japan.osu.object.Bot;

public class Main {

    public static Bot bot;

    public static void main(String[] args) {
        bot = new Bot();
        bot.start();
    }
}
