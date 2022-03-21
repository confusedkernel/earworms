package nottyl.earwormsbot.lavaplayer;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * This class maps each guild with a unique Music manager (and all that music nonsense)
 */

public class GuildMusicManager {

    private static Map<Optional<Snowflake>, MusicManager> guildId2MusicManager;

    public GuildMusicManager() {
        guildId2MusicManager = new HashMap<>();
    }

    public MusicManager getMusicManager(MessageCreateEvent event) {
        return guildId2MusicManager.computeIfAbsent(event.getGuildId(),
                (guildId) -> new MusicManager(event)
        );
    }

}
