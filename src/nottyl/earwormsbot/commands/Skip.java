package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
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
                    EmbedCreateSpec embed;
                    embed = EmbedCreateSpec.builder()
                            .color(Color.ORANGE)
                            .title("Track Skipped")
                            .build();
                    event.getMessage()
                            .getChannel().block()
                            .createMessage(embed)
                            .block();
                    mgr.nextTrack();
                });
    }

}
