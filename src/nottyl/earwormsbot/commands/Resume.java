package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.TrackScheduler;
import reactor.core.publisher.Mono;

public class Resume implements ICommand {

    @Override
    public String name() {
        return "resume";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final TrackScheduler scheduler = new TrackScheduler(Main.player);
        Mono.justOrEmpty(event.getMessage().getContent())
                .doFirst(() -> Main.player.setPaused(false))
                .then(event.getMessage().getChannel().block().createMessage("⏯ | The music is resumed."))
                .block();
    }
}
