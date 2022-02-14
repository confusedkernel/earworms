package nottyl.earwormsbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;


public final class TrackScheduler implements AudioLoadResultHandler {

    public static AudioPlayer player;

    public TrackScheduler(final AudioPlayer player) {
        TrackScheduler.player = player;
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        player.playTrack(track);
    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        //Multiple songs in a playlist
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
    }
}

/*TODO: New queue system*/
/*
public final class TrackScheduler extends AudioEventAdapter {

    public static AudioPlayer player;
    public final QueueArray<AudioTrack> queue;
    public AudioTrack currentTrack;
    public final QueueArray<AudioTrack> playedQueue;
    private final Logger logger;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new QueueArray<>();
        this.playedQueue = new QueueArray<>();
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public boolean queue(AudioTrack track, boolean noInterrupt) {
        boolean playing = !this.player.startTrack(track, noInterrupt);
        if (playing) {
            this.queue.pushTail(track);
        }
        updateCurrentTrack();
        return !playing;
    }

    private void updateCurrentTrack() {
        currentTrack = player.getPlayingTrack();
    }

    public void clearQueue() {
        this.queue.clear();
        this.playedQueue.clear();
    }

    public void nextTrack() throws IllegalStateException {
        final AudioTrack next = this.queue.popHead();
        this.playedQueue.pushTail(currentTrack);
        this.player.startTrack(next.makeClone(), false);
        updateCurrentTrack();
    }

    public void prevTrack() throws IllegalStateException {
        final AudioTrack last = this.playedQueue.popTail();
        this.queue.pushHead(currentTrack);
        this.player.startTrack(last.makeClone(), false);
        updateCurrentTrack();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
}*/
