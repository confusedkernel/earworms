package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import reactor.core.publisher.Mono;

public class Join implements ICommand {
    @Override
    public String name() {
        return "join";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join(spec -> spec.setProvider(Main.provider)))
                .block();
        event.getMessage()
                .getChannel().block()
                .createMessage("🎛 | Joined the Voice Channel").block();
    }
}