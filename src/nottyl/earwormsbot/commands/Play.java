package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;
import java.util.Arrays;

import static nottyl.earwormsbot.Main.playerManager;

public class Play implements ICommand {
    @Override
    public String name() {
        return "play";
    }

    /*TODO: New play function that taps into QueueArray and new TrackScheduler*/

    @Override
    public void execute(MessageCreateEvent event) {
        final TrackScheduler scheduler = new TrackScheduler(Main.player);
        Mono.justOrEmpty(event.getMessage().getContent())
                //TODO: fix the bug of (command.get(1))
                .map(content -> Arrays.asList(content.split(" ")))
                .doOnNext(command -> playerManager.loadItem(command.get(1), scheduler))
                .block();
        event.getMessage()
                .getChannel().block()
                .createMessage("▶️ | Now Playing...").block();
    }

/*  public void execute(MessageCreateEvent event) {

  }
*/
}
