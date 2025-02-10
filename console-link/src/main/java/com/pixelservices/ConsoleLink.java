package com.pixelservices;

import com.pixelservices.api.modules.MbModule;
import com.pixelservices.commands.ConsoleCommand;
import com.pixelservices.listeners.ConsoleListener;
import com.pixelservices.logger.LoggerFactory;
import com.pixelservices.manager.LinkedChannelRegistry;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ConsoleLink extends MbModule {
    @Override
    public void onEnable() {
        getLogger().info("Enabling ConsoleLink...");
        LinkedChannelRegistry registry = new LinkedChannelRegistry();

        getLogger().info("Registering ConsoleListener...");
        LoggerFactory.registerListener(new ConsoleListener(registry));

        getLogger().info("Registering Commands...");
        registerSlashCommand(
                Commands.slash("console", "Forwards something to the console.")
                        .addOption(OptionType.STRING, "command", "The command to forward.", true),
                new ConsoleCommand(registry));

        getLogger().info("✔️ Successfully Linked " + registry.getChannels().size() + " channels!");
    }

    @Override
    public void onDisable() {

    }
}
