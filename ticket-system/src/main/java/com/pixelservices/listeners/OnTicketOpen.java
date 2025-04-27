package com.pixelservices.listeners;

import com.pixelservices.TicketSystem;
import com.pixelservices.data.EmbedData;
import com.pixelservices.mobot.api.modules.listener.ModuleListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class OnTicketOpen extends ModuleListener {
    private TicketSystem module;

    public OnTicketOpen(TicketSystem module){
        this.module = module;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("open-normal-ticket")){
            Guild guild = event.getGuild();

            Category category = module.getChannelCache().getTicketCategory();

            TextChannel ticket;

            if (category == null){
                return;
            }else{
               ticket = createTicket(event, category);
            }

            if (ticket == null){
                return;
            }

            User user = event.getUser();
            EmbedData data = new EmbedData(user, guild);

            // Proceeding to send a message to the ticket channel. Ensure all required objects are initialized correctly.
           ticket.sendMessage(module.getRoleCache().getSupporter().getAsMention())
                    .addEmbeds(module.getEmbedUtil().customEmbed("ticket-info", data))
                    .setActionRow(Button.danger("close-ticket", "Close"))
                    .queue();

            event.reply("Creating ticket!").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(1, TimeUnit.MILLISECONDS));
        }
    }

    private TextChannel createTicket(ButtonInteractionEvent event, Category category){
        boolean ticketExists = category.getChannels().stream()
                .anyMatch(channel -> channel.getName().contains(event.getUser().getName() + "s-ticket"));

        if (ticketExists) {
            event.reply("You already have an open ticket.").setEphemeral(true).queue();
            return null;
        }

        TextChannel textChannel = category.createTextChannel(  event.getUser().getName()+"s-ticket").complete();

        textChannel.upsertPermissionOverride(event.getGuild().getPublicRole())
                .deny(Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY, Permission.VIEW_CHANNEL)
                .queue(success -> module.getLogger().debug("Text channel permissions set for role: " + event.getGuild().getPublicRole().getName()),
                        error -> module.getLogger().error("Failed to set text channel permissions: " + error.getMessage())
                );

        if (module.getRoleCache().getSupporter() != null) {
            textChannel.upsertPermissionOverride(module.getRoleCache().getSupporter())
                    .grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)
                    .queue(success -> module.getLogger().debug("Supporter role permissions set successfully."),
                            error -> module.getLogger().error("Failed to set permissions for supporter role: " + error.getMessage()));
        } else {
            module.getLogger().warn("Supporter role not found in cache.");
        }

        if (event.getMember() == null){
            event.reply("Failed to create a ticket.").queue();
            return null;
        }

        textChannel.upsertPermissionOverride(event.getMember())
                .grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY)
                .deny(Permission.CREATE_PRIVATE_THREADS, Permission.CREATE_PUBLIC_THREADS)
                .queue();
        ;

        return textChannel;
    }
}
