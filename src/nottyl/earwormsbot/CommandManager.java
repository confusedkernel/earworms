package nottyl.earwormsbot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.commands.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CommandManager {
    public final static  List<ICommand> commands = new ArrayList<>(
            Arrays.asList(
                    new Hello(),
                    new Join(),
                    new Play(),
                    new Pause(),
                    new Resume(),
                    new Disconnect(),
                    new Stop(),
                    new Ping(),
                    new Skip(),
                    new NowPlaying(),
                    new Info(),
                    new Clear()

                    /* TODO:
                        View queues,
                        Embed builder,
                        Youtube search,
                        SoundCloud integration. */
            )
    );


    public static void handle(MessageCreateEvent event) {
        String query = event.getMessage().getContent().substring(1);
        for(ICommand cmd : commands) {
            if(query.startsWith(cmd.name())){
                cmd.execute(event);
            }
        }
    }
}
