package nottyl.earwormsbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import reactor.core.publisher.Mono;
import discord4j.core.object.Embed;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackScheduler extends AudioEventAdapter{
    final Queue<AudioTrack> queue;
    private final AudioPlayer player;


    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        final AudioTrack nowPlaying = player.getPlayingTrack();
        if (nowPlaying != track) {
            this.queue.add(track);
            System.out.println("added to queue");
        }
        this.player.startTrack(track, true);
    }

    public void clear() {
        queue.clear();
    }

    public void next() {
        this.player.startTrack(queue.poll(), false);
        System.out.println("skipped");
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.FINISHED) {
            next();
            AudioTrack current = player.getPlayingTrack();
            System.out.println("on track end, next track");
        }
    }
}
