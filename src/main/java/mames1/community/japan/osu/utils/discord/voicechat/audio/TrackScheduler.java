package mames1.community.japan.osu.utils.discord.voicechat.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import mames1.community.japan.osu.utils.log.Level;
import mames1.community.japan.osu.utils.log.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    public final AudioPlayer audioPlayer;
    public final BlockingDeque<AudioTrack> queue;

    public TrackScheduler(AudioPlayer audioPlayer ) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void queue(AudioTrack track) {
        if(!this.audioPlayer.startTrack(track, true)) {
            System.out.println("Track could not be started");
            this.queue.offer(track);
        }
    }

    public void nextTrack() {

        AudioTrack track = this.queue.poll();
        this.audioPlayer.startTrack(track, false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext) {
            nextTrack();
        }
        // ファイルを削除するように (出力したwavファイル)
        try {
            Files.deleteIfExists(Path.of(track.getInfo().uri));
        } catch (Exception e) {
            Logger.log("オーディオファイルの削除中にエラーが発生しました: " + e.getMessage(), Level.ERROR);
        }
    }
}
