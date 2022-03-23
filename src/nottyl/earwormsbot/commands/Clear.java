package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

public class Clear implements ICommand {
    @Override
    public String name() {
        return "clear";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
        mgr.clear();
        event.getMessage().getChannel()
                .subscribe(replyChannel -> {
                    replyChannel.createMessage("🎛 | Queue cleared. ").subscribe();
                });
    }
}
