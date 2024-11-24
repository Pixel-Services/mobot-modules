package dev.siea.listener;

import dev.siea.ConsoleLink;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DefaultListeners extends ListenerAdapter {
    private final ConsoleLink link;

    public DefaultListeners(ConsoleLink link) {
        this.link = link;
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        link.guildReady(event.getGuild());
    }
}
