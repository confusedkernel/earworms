package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class Pause implements ICommand {

    @Override
    public String name() {
        return "pause";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final TrackScheduler scheduler = new TrackScheduler(Main.player);
        System.out.println("I am being executed");
        Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.setPaused(true))
                .then(event.getMessage().getChannel().block().createMessage("⏯ | The music is paused."))
                .block();
    }
}
