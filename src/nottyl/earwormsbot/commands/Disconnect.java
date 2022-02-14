package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
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
        event.getMessage()
                .getChannel().block()
                .createMessage("🎛 | Disconnected from the Voice Channel").block();
    }
}
