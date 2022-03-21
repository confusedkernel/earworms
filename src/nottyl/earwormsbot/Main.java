package nottyl.earwormsbot;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import nottyl.earwormsbot.lavaplayer.GuildMusicManager;

public class Main {

    public static String prefix;
    public static GuildMusicManager guildMusicManager;

    public static void main(String[] args){

        guildMusicManager = new GuildMusicManager();
        prefix = Config.get("prefix");

        final GatewayDiscordClient client = DiscordClientBuilder.create(Config.get("token")).build().login().block();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(event -> event.getMessage().getContent().startsWith(prefix))
                .filter(event -> event.getMessage().getAuthor().map(user -> !user.isBot()).orElse(false))
                .subscribe(CommandManager::handle);

        client.onDisconnect().block();
    }
}