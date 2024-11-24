package dev.siea.manager;

import dev.siea.global.Messages;
import dev.siea.global.Universe;
import dev.siea.storage.Storage;
import dev.siea.storage.models.Ticket;
import dev.siea.storage.models.enums.Status;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.vitacraft.api.config.ConfigLoader;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public class TicketManager {
    private final Guild guild;
    private final Universe universe;
    private final HashMap<String, Ticket> activeTickets = new HashMap<>();
    private final List<Role> roles = new ArrayList<>();
    private final Category category;

    public TicketManager(Guild guild, Universe universe, List<Role> roles, Category category) {
        this.guild = guild;
        this.universe = universe;
        this.roles.addAll(roles);
        this.category = category;
        registerOpenTickets();
    }

    public void createTicket(Member member) {
        TextChannel channel = guild.createTextChannel(member.getEffectiveName())
                .setParent(category)
                .setTopic("Ticket created by " + member.getEffectiveName())
                .syncPermissionOverrides()
                .addMemberPermissionOverride(member.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                .complete();

        Messages messages = universe.messages();

        Ticket ticket = universe.storage().registerTicket(member.getId(), channel.getId(), guild.getId());

        String message = messages.get("created").replace("%user%", member.getAsMention());

        Button claimButton = Button.success("claim", "Claim Ticket");
        Button closeButton = Button.danger("close", "Close Ticket");

        channel.sendMessageEmbeds(messages.generateEmbed("Ticket Created", message)).setActionRow(claimButton, closeButton).queue();

        activeTickets.put(channel.getId(), ticket);
    }

    public boolean claimTicketIn(String channelId, Member moderator) {
        Ticket ticket = activeTickets.get(channelId);
        if (ticket == null || ticket.getStatus() != Status.OPEN) {
            return false;
        }

        if (!isModerator(moderator)) {
            return false;
        }

        ticket.setClaimedBy(moderator.getId());
        ticket.setStatus(Status.CLAIMED);
        universe.storage().updateTicket(ticket);

        return true;
    }

    public boolean closeTicketIn(String channelId, Member moderator) {
        Ticket ticket = activeTickets.get(channelId);

        if (ticket == null ) {
            return false;
        }

        if (!isModerator(moderator)) {
            return false;
        }

        ticket.setStatus(Status.CLOSED);

        universe.storage().updateTicket(ticket);

        TextChannel channel = guild.getTextChannelById(channelId);

        if (channel != null) {
            channel.delete().queue();
        }

        activeTickets.remove(channelId);
        return true;
    }

    public void closeTicket(String channelId, String reason) {
        Ticket ticket = activeTickets.get(channelId);

        if (ticket == null) {
            return;
        }

        ticket.setStatus(Status.CLOSED);

        universe.storage().updateTicket(ticket);

        TextChannel channel = guild.getTextChannelById(channelId);

        if (channel != null) {
            channel.sendMessageEmbeds(universe.messages().generateEmbed("Ticket forcefully closed", "The ticket was closed by the system: " + reason)).queue();
            channel.delete().queue();
        }

        activeTickets.remove(channelId);
    }

    public Ticket getTicket(String id) {
        return activeTickets.get(id);
    }

    private void registerOpenTickets(){
        Storage storage = universe.storage();
        for (Ticket ticket : storage.getTickets(Status.OPEN, guild.getId())) {
            activeTickets.put(ticket.getChannelId(), ticket);
        }
        for (Ticket ticket : storage.getTickets(Status.CLAIMED, guild.getId())) {
            activeTickets.put(ticket.getChannelId(), ticket);
        }

        universe.logger().info("Found {} active tickets", activeTickets.size());
    }

    private boolean isModerator(Member member) {
        List<Role> modRoles = member.getRoles();
        for (Role role : roles) {
            if (modRoles.contains(role)) {
                return true;
            }
        }
        return false;
    }
}