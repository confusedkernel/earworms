package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
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
        EmbedCreateSpec embed;
        embed = EmbedCreateSpec.builder()
                .color(Color.BLUE)
                .title("Queue cleared")
                .build();
        event.getMessage()
                .getChannel().block()
                .createMessage(embed)
                .block();
    }
}
