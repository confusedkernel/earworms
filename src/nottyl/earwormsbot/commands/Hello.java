package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;

import java.util.Objects;

public class Hello implements ICommand {

    @Override
    public String name() {
        return "hello";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        Objects.requireNonNull(event.getMessage()
                        .getChannel().block())
                .createMessage("👋 | Hello to you too!").block();
    }
}
