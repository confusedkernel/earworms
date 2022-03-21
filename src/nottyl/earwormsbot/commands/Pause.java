package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class Pause implements ICommand {

    @Override
    public String name() {
        return "pause";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage().getChannel()
                .subscribe(ch -> {
                    final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
                    mgr.pause();
                    ch.createMessage("⏯ | The music is paused.").subscribe();
                });
    }
}
