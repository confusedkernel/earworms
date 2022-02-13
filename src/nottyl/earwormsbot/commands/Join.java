package nottyl.earwormsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import nottyl.earwormsbot.ICommand;
import nottyl.earwormsbot.Main;

public class Join implements ICommand {
    @Override
    public String name() {
        return "join";
    }

    @Override
    public void execute(MessageCreateEvent event) {
        final Member member = event.getMember().orElse(null);
        if (member != null) {
            final VoiceState voiceState = member.getVoiceState().block();
            if (voiceState != null) {
                final VoiceChannel channel = voiceState.getChannel().block();
                if (channel != null) {
                    event.getMessage().getChannel().block().createMessage("Joined the Voice Channel.").block();
                    channel.join(spec -> spec.setProvider(Main.provider)).block();
                }
            }
        }
    }
}