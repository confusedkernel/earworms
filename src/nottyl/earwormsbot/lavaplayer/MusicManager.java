package nottyl.earwormsbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.voice.AudioProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Manages playerManager, player, and audioProvider of a specific guild.
 */

public class MusicManager extends AudioEventAdapter{

    private final AudioPlayerManager playerManager;
    public final AudioPlayer player;
    private final AudioProvider provider;
    private final MessageCreateEvent event;
    private final Queue<AudioTrack> queue;

    public MusicManager(MessageCreateEvent event) {
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        provider = new LavaPlayerAudioProvider(player);

        this.event = event;
        queue = new LinkedBlockingQueue<>();
    }

    public AudioProvider getProvider() {
        return provider;
    }

    /**
     * @param trackUrl    query or URL of a song
     * @param noInterrupt interrupt the song currently playing
     */

    public void play(String trackUrl, boolean noInterrupt) {
        playerManager.loadItemOrdered(event, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                boolean isPlaying = !player.startTrack(track, true);
                if (!isPlaying) {
                    nowPlaying(track);
                }
                queue(track, false);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("🎛 | Playlist loaded..."))
                        .subscribe();
                AudioTrack firstTrack = playlist.getTracks().get(0);
                boolean isPlaying = !player.startTrack(firstTrack, true);
                if (!isPlaying) {
                    nowPlaying(firstTrack);
                }
                tracks.remove(0);
                queue.addAll(tracks);
            }

            @Override
            public void noMatches() {
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("🎛 | No matches..."))
                        .subscribe();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("🎛 | Something went wrong... Please try again"))
                        .subscribe();
            }
        });

    }

    public void queue(AudioTrack track, boolean noInterrupt) {
        boolean isPlaying = !player.startTrack(track, true);
        final AudioTrack nowPlaying = player.getPlayingTrack();
        if (isPlaying && nowPlaying != track) {
            this.queue.add(track);
            System.out.println("added to queue");
        }
    }

    public void clear() {
        this.queue.clear();
    }

    public void pause() {
        this.player.setPaused(true);
    }

    public void resume() {
        this.player.setPaused(false);
    }

    public void stop() {
        this.player.stopTrack();
    }

    public void nextTrack() {
        this.player.startTrack(this.queue.poll(), false);
        final AudioTrack track = player.getPlayingTrack();
        nowPlaying(track);
        System.out.println("skipped");
    }

    public void currentSong() {
        AudioTrack track = this.player.getPlayingTrack();
        nowPlaying(track);
    }

    public void nowPlaying(AudioTrack track) {
        if (track != null) {
            event.getMessage().getChannel()
                    .flatMap(replyChannel -> replyChannel.createMessage("▶️ | **Now Playing:** " + track.getInfo().title))
                    .subscribe();
        } else {
            event.getMessage().getChannel()
                    .flatMap(replyChannel -> replyChannel.createMessage("🎛 | Nothing is playing..."))
                    .subscribe();
        }
    }

    @Override
    public void onTrackEnd(AudioPlayer player, @NotNull AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println("track ends" + track.getInfo().title + endReason.name());
        if (endReason.mayStartNext) {
            this.player.startTrack(this.queue.poll(), false);
            track = player.getPlayingTrack();
            nowPlaying(track);
        }
    }

}

