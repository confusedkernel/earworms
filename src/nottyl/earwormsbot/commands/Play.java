package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class Play implements ICommand {
    @Override
    public String name() {
        return "play";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final TrackScheduler scheduler = new TrackScheduler(Main.player);

        Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .doOnNext(command -> Main.playerManager.loadItem(command.get(1), scheduler))
                .then(event.getMessage().getChannel().block().createMessage("▶️ | Now Playing..."))
                .block();
    }
}
