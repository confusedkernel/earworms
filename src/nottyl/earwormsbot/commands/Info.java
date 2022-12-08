package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

public class Info implements ICommand {
    @Override
    public String name() {
        return "info";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
        mgr.info();
    }
}
