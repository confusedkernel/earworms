package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import nottyl.earwormsbot.ICommand;
import reactor.core.publisher.Mono;

public class Disconnect implements ICommand {
    @Override
    public String name() {
        return "disconnect";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(VoiceChannel::sendDisconnectVoiceState)
                .block();
        EmbedCreateSpec embed;
        embed = EmbedCreateSpec.builder()
                .color(Color.SUMMER_SKY)
                .title("Disconnected from the Voice Channel")
                .build();
        event.getMessage()
                .getChannel().block()
                .createMessage(embed)
                .block();
    }
}
