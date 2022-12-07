package nottyl.earwormsbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import discord4j.voice.AudioProvider;

import java.time.Instant;
import java.util.List;

/**
 * Manages playerManager, player, and audioProvider of a specific guild.
 */

public class MusicManager extends AudioEventAdapter{

    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;
    private final AudioProvider provider;
    public final MessageCreateEvent event;


    private final TrackScheduler scheduler;

    public MusicManager(MessageCreateEvent event) {
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        provider = new LavaPlayerAudioProvider(player);
        this.event = event;
    }



    public AudioProvider getProvider() {
        return provider;
    }

    /**
     * @param trackUrl    query or URL of a song
     *  interrupt the song currently playing
     */

    public void play(String trackUrl) {
        playerManager.loadItemOrdered(event, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                boolean isPlaying = !player.startTrack(track, true);
                if (!isPlaying) {
                    nowPlaying(track);
                }
                scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();
                EmbedCreateSpec embed = EmbedCreateSpec.builder()
                        .color(Color.SUMMER_SKY)
                        .title("Playlist Loaded!")
                        .description("Loaded" + playlist.getTracks().size() + "songs")
                        .build();
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage(embed))
                        .subscribe();
                AudioTrack firstTrack = playlist.getTracks().get(0);
                boolean isPlaying = player.startTrack(firstTrack, true);
                if (!isPlaying) {
                    nowPlaying(firstTrack);
                }
                scheduler.queue(tracks.remove(0));
                scheduler.queue.offer((AudioTrack) tracks);
            }

            @Override
            public void noMatches() {
                EmbedCreateSpec embed = EmbedCreateSpec.builder()
                        .color(Color.RED)
                        .title("Error")
                        .description("Error: No matches...")
                        .build();
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage(embed))
                        .subscribe();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                EmbedCreateSpec embed = EmbedCreateSpec.builder()
                        .color(Color.RED)
                        .title("Error")
                        .description("Error: Something went wrong... Please try again")
                        .build();
                event.getMessage().getChannel()
                        .flatMap(replyChannel -> replyChannel.createMessage(embed))
                        .subscribe();
            }
        });

    }


    public void pause() {
        player.setPaused(true);
    }

    public void resume() {
        player.setPaused(false);
    }

    public void stop() {
        player.stopTrack();
    }

    public void clear(){scheduler.clear();}

    public void currentSong() {
        AudioTrack track = player.getPlayingTrack();
        nowPlaying(track);
    }


    public void nextTrack(){
        scheduler.next();
        currentSong();
    }

    public void nowPlaying(AudioTrack track) {
        if (track != null) {
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.SEA_GREEN)
                    .title("Now Playing")
                    .description(track.getInfo().title)
                    .build();
            event.getMessage().getChannel()
                    .flatMap(replyChannel -> replyChannel.createMessage(embed))
                    .subscribe();
        }
        else {
            EmbedCreateSpec embed = EmbedCreateSpec.builder()
                    .color(Color.RED)
                    .title("Now Playing")
                    .description("Nothing is playing...")
                    .build();
            event.getMessage().getChannel()
                    .flatMap(replyChannel -> replyChannel.createMessage(embed))
                    .subscribe();
        }
    }
}

