package nottyl.earwormsbot.lavaplayer;

import nottyl.earwormsbot.lavaplayer.LavaPlayerAudioProvider;
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
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Manages playerManager, player, and audioProvider of a specific guild.
 */
public class MusicManager {


    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;
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
                queue(track, noInterrupt);

                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("Track loaded !"))
                        .subscribe();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                queue(tracks.remove(0), noInterrupt);
                queue.addAll(tracks);

                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("Playlist loaded !"))
                        .subscribe();
            }

            @Override
            public void noMatches() {
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("Nothing matched your query..."))
                        .subscribe();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage("Oops.. Something went wrong"))
                        .subscribe();
            }
        });
    }

    public void queue(AudioTrack track, boolean noInterrupt) {
        boolean isPlaying = !player.startTrack(track, noInterrupt);
        if (isPlaying) {
            this.queue.add(track);
        }

    }

    public void nextTrack() {
        final AudioTrack next = this.queue.poll();

        Mono.justOrEmpty(next).subscribe((track -> {
                    player.startTrack(next, false);
                })
        );
    }

    public void pause() {
        player.setPaused(true);
    }

    public void resume() {player.setPaused(false);}

    public void stop() {
        player.stopTrack();
    }



}
