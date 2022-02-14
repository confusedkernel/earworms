package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;

public class Fuck implements ICommand {
    @Override
    public String name() {
        return "fuck";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage()
                .getChannel().block()
                .createMessage("🖕 | Fuck you too. Bitch.").block();
    }
}
