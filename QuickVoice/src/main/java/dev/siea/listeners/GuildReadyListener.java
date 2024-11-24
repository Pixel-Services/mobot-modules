package dev.siea.listeners;

import dev.siea.guild.VoiceChatManagerRegistry;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildReadyListener extends ListenerAdapter {
    private final VoiceChatManagerRegistry registry;

    public GuildReadyListener(VoiceChatManagerRegistry registry) {
        this.registry = registry;
    }

    public void onGuildReady(GuildReadyEvent e) {
        this.registry.loadGuild(e.getGuild());
    }
}
