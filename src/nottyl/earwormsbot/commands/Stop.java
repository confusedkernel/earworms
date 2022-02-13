package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;

public class Stop implements ICommand {
    @Override
    public String name() {
        return "stop";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final TrackScheduler scheduler = new TrackScheduler(Main.player);
        Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.stopTrack())
                .then(event.getMessage().getChannel().block().createMessage("⏹ | The music has stopped."))
                .block();
    }
}
