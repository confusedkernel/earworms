package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

public class Resume implements ICommand {

    @Override
    public String name() {
        return "resume";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        event.getMessage().getChannel()
                .subscribe(ch -> {
                    final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
                    mgr.resume();
                    EmbedCreateSpec embed;
                    embed = EmbedCreateSpec.builder()
                            .color(Color.SEA_GREEN)
                            .title("Track Resumed")
                            .build();
                    event.getMessage()
                            .getChannel().block()
                            .createMessage(embed)
                            .block();
                });
    }
}
