package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.gateway.GatewayClient;
import nottyl.earwormsbot.ICommand;

public class Ping implements ICommand {
    @Override
    public String name() {
        return "ping";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage()
                .getChannel().block()
                .createMessage("⏲ | Current Ping is " + "`" +
                        event.getMessage().getClient().getGatewayClient(0)
                        .map(GatewayClient::getResponseTime).get().toMillis() + "ms" + "`")
        .block();
    }
}
