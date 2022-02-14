package nottyl.earwormsbot;

import discord4j.core.event.domain.message.MessageCreateEvent;

public interface ICommand {

    String name();

    void execute(MessageCreateEvent event);

}