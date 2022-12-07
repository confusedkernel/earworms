package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

import java.util.Objects;

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
                });
        EmbedCreateSpec embed;
        embed = EmbedCreateSpec.builder()
                .color(Color.ORANGE)
                .title("Track Paused")
                .build();
        Objects.requireNonNull(event.getMessage().getChannel().block())
                .createMessage(embed)
                .block();
    }
}
