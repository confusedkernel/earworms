package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.gateway.GatewayClient;
import discord4j.rest.util.Color;
import nottyl.earwormsbot.ICommand;

public class Ping implements ICommand {
    @Override
    public String name() {
        return "ping";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        long ping = event.getMessage().getClient().getGatewayClient(0)
                .map(GatewayClient::getResponseTime).get().toMillis();
        EmbedCreateSpec embed;
        if(ping<=100){
            embed = EmbedCreateSpec.builder()
                    .color(Color.SEA_GREEN)
                    .title("Current Ping")
                    .description(ping + "ms")
                    .build();
        }
        else if(ping<=200){
            embed = EmbedCreateSpec.builder()
                    .color(Color.YELLOW)
                    .title("Current Ping")
                    .description(ping + "ms")
                    .build();
        }
        else if(ping<=300){
            embed = EmbedCreateSpec.builder()
                    .color(Color.ORANGE)
                    .title("Current Ping")
                    .description(ping + "ms")
                    .build();
        }
        else{
            embed = EmbedCreateSpec.builder()
                    .color(Color.RED)
                    .title("Current Ping")
                    .description(ping + "ms")
                    .build();
        }

        event.getMessage()
                .getChannel().block()
                .createMessage(embed)
        .block();
    }
}
