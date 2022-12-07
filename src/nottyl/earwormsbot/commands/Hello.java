package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.GatewayClient;
import nottyl.earwormsbot.ICommand;


public class Hello implements ICommand {

    @Override
    public String name() {
        return "hello";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage()
                .getChannel().block()
                .createMessage("Hi! I'm online right now.").block();
    }
}
