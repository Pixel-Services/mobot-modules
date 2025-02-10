package com.pixelservices.commands;

import com.pixelservices.api.addons.SlashCommandAddon;
import com.pixelservices.manager.LinkedChannelRegistry;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class ConsoleCommand implements SlashCommandAddon {
    private final LinkedChannelRegistry registry;

    public ConsoleCommand(LinkedChannelRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent slashCommandInteractionEvent) {

    }
}
