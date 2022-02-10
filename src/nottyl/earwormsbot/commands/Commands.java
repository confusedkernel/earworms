package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.AudioProvider;
import discord4j.voice.VoiceConnection;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;
import sx.blah.discord.handle.audio.IAudioProvider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static sx.blah.discord.util.audio.providers.GlobalProvider.setProvider;


public class Commands {
    public static final Map<String, Command> commands = new HashMap<>();

    public interface Command {
        void execute(MessageCreateEvent event);
    }


    static {
        commands.put("hello", event -> event.getMessage()
                .getChannel().block()
                .createMessage("👋 | Hello to you too!").block());

        commands.put("join", event -> {
            final Member member = event.getMember().orElse(null);
            if (member != null) {
                final VoiceState voiceState = member.getVoiceState().block();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel().block();
                    if (channel != null) {
                        event.getMessage().getChannel().block().createMessage("🎛 | Joined the Voice Channel.").block();
                        channel.join(spec -> spec.setProvider(Main.provider)).block();
;                    }
                }
            }
        });

        commands.put("disconnect", event -> {
            final Member member = event.getMember().orElse(null);
            if (member != null) {
                final VoiceState voiceState = member.getVoiceState().block();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel().block();
                    if (channel != null) {
                        event.getMessage().getChannel().block().createMessage("🎛 | Bye. Until next time!").block();
                        channel.sendDisconnectVoiceState().block();
                    }
                }
            }
        });

        final TrackScheduler scheduler = new TrackScheduler(Main.player);
        commands.put("play", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .doOnNext(command -> Main.playerManager.loadItem(command.get(1), scheduler))
                .then(event.getMessage().getChannel().block().createMessage("▶️ | Now Playing..."))
                .block());

        commands.put("stop", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.stopTrack())
                .then(event.getMessage().getChannel().block().createMessage("⏹ | The music has stopped."))
                .block());

        commands.put("pause", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.setPaused(true))
                .then(event.getMessage().getChannel().block().createMessage("⏯ | The music is paused."))
                .block());

        commands.put("resume", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.setPaused(false))
                .then(event.getMessage().getChannel().block().createMessage("⏯ | The music is resumed."))
                .block());
    }
}
