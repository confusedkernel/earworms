package com.github.nottyl.earwormsbot;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;

import java.util.HashMap;
import java.util.Map;

public class Commands {
    public static final Map<String, Command> commands = new HashMap<>();

    interface Command {
        void execute(MessageCreateEvent event);
    }
    static {
        commands.put("hello", event -> event.getMessage()
                .getChannel().block()
                .createMessage("What's Up!").block());
        commands.put("join", event -> {
            final Member member = event.getMember().orElse(null);
            if (member != null) {
                final VoiceState voiceState = member.getVoiceState().block();
                if (voiceState != null) {
                    final VoiceChannel channel = voiceState.getChannel().block();
                    if (channel != null) {
                        // join returns a VoiceConnection which would be required if we were
                        // adding disconnection features, but for now we are just ignoring it.
                        channel.join(spec -> spec.setProvider(Main.provider)).block();
                    }
                }
            }
        });
    }

}
