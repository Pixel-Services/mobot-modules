package com.pixelservices.listeners;

import com.pixelservices.TicketSystem;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnBotReady extends ListenerAdapter {
    private TicketSystem module;

    public OnBotReady(TicketSystem module){
        this.module = module;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();

        // Check for "Tickets" Category
        Category ticketsCategory = guild.getCategoriesByName("Tickets", true).stream().findFirst().orElse(null);
        if (ticketsCategory == null){
            ticketsCategory = guild.createCategory("Tickets").complete();
        }

        // Check for "Tickets" Channel
        TextChannel ticketChannel = ticketsCategory.getTextChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase("Tickets"))
                .findFirst().orElse(null);
        if (ticketChannel == null){
            ticketsCategory.createTextChannel("Tickets").queue();
        }

        // Check for "Archive" Category
        Category archiveCategory = guild.getCategoriesByName("Archived", true).stream().findFirst().orElse(null);
        if (archiveCategory == null){
            guild.createCategory("Archived").queue();
        }

        MessageEmbed normalTicketEmbed = module.getEmbedUtil().customEmbed("embeds.normal_ticket");
    }
}
