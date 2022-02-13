package nottyl.earwormsbot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.commands.*;

import java.util.ArrayList;
import java.util.List;


public class CommandManager {
    public final static  List<ICommand> commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new Hello());
        addCommand(new Join());
        addCommand(new Play());
        addCommand(new Pause());
        addCommand(new Resume());
        addCommand(new Stop());
    }

    public static void handle(MessageCreateEvent event) {
        String query = event.getMessage().getContent().substring(1);
        for(ICommand cmd : commands) {
            if(query.equalsIgnoreCase(cmd.name())){
                cmd.execute(event);
            }
        }
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = commands.stream()
                .anyMatch((x) -> x.name().equalsIgnoreCase(cmd.name()));

        if (nameFound) {
            throw new IllegalArgumentException("This command is already existing.");
        } else {
            commands.add(cmd);
        }
    }

}
