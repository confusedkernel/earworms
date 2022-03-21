package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;
import reactor.core.publisher.Mono;

public class Stop implements ICommand {

    @Override
    public String name() {
        return "stop";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage().getChannel()
                .subscribe(ch -> {
                    final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
                    mgr.stop();
                    ch.createMessage("⏹ | The music has stopped.").subscribe();
                });
    }
}
