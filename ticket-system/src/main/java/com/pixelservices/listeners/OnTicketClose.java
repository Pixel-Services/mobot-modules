package com.pixelservices.listeners;

import com.pixelservices.TicketSystem;
import com.pixelservices.data.EmbedData;
import com.pixelservices.mobot.api.modules.listener.ModuleListener;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class OnTicketClose extends ModuleListener {
    private TicketSystem module;

    public OnTicketClose(TicketSystem module){
        this.module = module;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getComponentId().equals("close-ticket")){
            TextChannel channel = event.getChannel().asTextChannel();
            Category parent = channel.getParentCategory();

            if (parent != module.getChannelCache().getTicketCategory()){
                event.reply("This is not an active Ticket").setEphemeral(true).queue();
                return;
            }

            if (!event.getMember().getRoles().contains(module.getRoleCache().getSupporter())){
                event.reply("You are not allowed to close the ticket.").queue();
                return;
            }


            if (channel.getName().contains("ticket")) {
                Role supporterRole = module.getRoleCache().getSupporter();

                if (supporterRole == null) {
                    event.reply("Supporter role not found.").queue();
                    return;
                }

                // Remove all non-supporter members from the channel
                channel.getPermissionContainer().getPermissionOverrides().forEach(override -> {
                    Member member = event.getGuild().getMemberById(override.getId());
                    if (member != null && !member.getRoles().contains(supporterRole)) {
                        channel.getManager().removePermissionOverride(member).queue();
                    }
                });



                channel.getManager().setParent(module.getChannelCache().getArchiveCategory()).queue(
                        success -> module.getLogger().debug("Channel moved out of category!"),
                        error -> module.getLogger().error("Failed to move channel: " + error.getMessage())
                );

                // Send a simple message in the channel
                EmbedData data = new EmbedData(event.getUser(), event.getGuild());

                MessageEmbed embed = module.getEmbedUtil().customEmbed("ticket-close", data);

                channel.sendMessageEmbeds(embed).queue();

                // Acknowledge interaction silently without reply
                event.deferEdit().queue();
            } else {
                channel.sendMessage("This is not a ticket.").queue();

                // Acknowledge interaction silently without reply
                event.deferEdit().queue();
            }
        }
    }
}
