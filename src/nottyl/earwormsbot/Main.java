package nottyl.earwormsbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.voice.AudioProvider;
import nottyl.earwormsbot.CommandManager;
import nottyl.earwormsbot.lavaplayer.LavaPlayerAudioProvider;

public class Main {

    public static AudioProvider provider;
    public static AudioPlayerManager playerManager;
    public static AudioPlayer player;
    static final String prefix = Config.get("prefix");

    public static void main(String[] args){
        playerManager = new DefaultAudioPlayerManager();
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        player = playerManager.createPlayer();
        provider = new LavaPlayerAudioProvider(player);
        final GatewayDiscordClient client = DiscordClientBuilder.create(Config.get("token"))
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(event -> event.getMessage().getContent().startsWith(prefix))
                .filter(event -> event.getMessage().getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(CommandManager::handle);
        client.onDisconnect().block();
    }
}