package nottyl.earwormsbot.commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import discord4j.voice.AudioProvider;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class Join implements ICommand {
    @Override
    public String name() {
        return "join";
    }

    @Override
    public void execute(MessageCreateEvent event) {

        final Mono<MessageChannel> textChannel = event.getMessage().getChannel();
        final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
        final AudioProvider provider = mgr.getProvider();
        final Optional<Snowflake> guildId = event.getGuildId();

        Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .subscribe(voiceChannel -> {
                    voiceChannel.join(spec -> spec.setProvider(provider)).subscribe();
                });
        EmbedCreateSpec embed;
        embed = EmbedCreateSpec.builder()
                .color(Color.BLUE)
                .title("Joined the Voice Channel")
                .build();
        event.getMessage()
                .getChannel().block()
                .createMessage(embed)
                .block();

    }
}