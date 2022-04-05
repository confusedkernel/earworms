package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;
import nottyl.earwormsbot.lavaplayer.MusicManager;

public class Play implements ICommand {
    @Override
    public String name() {
        return "play";
    }

    /*TODO: New play function that taps into QueueArray and new TrackScheduler*/

    @Override
    public void execute(MessageCreateEvent event) {
        final MusicManager mgr = Main.guildMusicManager.getMusicManager(event);
        final String query = event.getMessage().getContent()
                .replaceFirst("^!play", "")
                .trim();

//		TODO : youtube search
        event.getMessage().getChannel()
                .subscribe(replyChannel -> {
                    replyChannel.createMessage("🎛 | Adding to queue... ").subscribe();
                    mgr.play(query);
                });

    }
}


