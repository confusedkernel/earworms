package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import nottyl.earwormsbot.ICommand;
import reactor.core.publisher.Mono;

public class Gay implements ICommand {
    @Override
    public String name() {
        return "gay";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage()
                .getChannel().block()
                .createMessage("🖕 | Ya Gay").block();
        Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(VoiceChannel::sendDisconnectVoiceState)
                .block();
        event.getMessage()
                .getChannel().block()
                .createMessage("🎛 | Disconnected from the Voice Channel").block();
    }
}
