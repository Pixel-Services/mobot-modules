package com.pixelservices.commands;

import com.pixelservices.api.commands.SlashCommand;
import com.pixelservices.api.commands.SlashCommandArgument;
import com.pixelservices.api.commands.SlashCommandHandler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class TestCommand implements SlashCommandHandler {
    @SlashCommand(name = "test")
    @SlashCommandArgument(name = "test", type = OptionType.STRING)
    public void onSlashCommand(SlashCommandInteractionEvent event){

    }
}
