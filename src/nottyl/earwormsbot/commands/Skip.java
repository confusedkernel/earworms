package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

public class Skip implements ICommand {
    @Override
    public String name() {
        return "skip";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);

        event.getMessage().getChannel()
                .subscribe(ch -> {
                    mgr.nextTrack();
                    ch.createMessage("⏭ | Skipped.").subscribe();
                });
    }

}
