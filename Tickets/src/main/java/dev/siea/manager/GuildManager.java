package dev.siea.manager;

import dev.siea.global.Universe;
import dev.siea.storage.models.Message;
import dev.siea.storage.models.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.vitacraft.api.config.ConfigLoader;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GuildManager extends ListenerAdapter {
    private final Guild guild;
    private final Universe universe;
    private final ConfigLoader guildConfig;
    private final Logger logger;
    private String messageId;
    private TicketManager ticketManager;

    public GuildManager(Guild guild, Universe universe, ConfigLoader guildConfig) {
        this.guild = guild;
        this.universe = universe;
        this.logger = universe.logger();
        this.guildConfig = guildConfig;

        universe.logger().info("Tickets initialized for: {}", guild.getName());
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        if (event.getGuild() != guild) return;
        List<Role> roles = setupRoles();
        Category category = setupCategory(roles);
        this.ticketManager = new TicketManager(guild, universe, roles, category);
    }

    private Message setupMessage() {
        String channelId = guildConfig.getConfig().getString("message");
        return null;
    }

    private List<Role> setupRoles() {
        List<Role> roles = new ArrayList<>();

        for (String roleId : guildConfig.getConfig().getStringList("roles")) {
            Role role = guild.getRoleById(roleId);
            if (role != null) {
                roles.add(role);
            } else {
                logger.error("Role with ID {} not found in guild {}", roleId, guild.getId());
                guildConfig.getConfig().getStringList("roles").remove(roleId);
                guildConfig.save();
            }
        }

        return roles;
    }

    private Category setupCategory(List<Role> roles) {
        Category category;

        try {
            category = guild.getCategoryById(guildConfig.getConfig().getString("category"));
        } catch (Exception e) {
            logger.error("Category with ID {} not found in guild {}", guildConfig.getConfig().getString("category"), guild.getId());
            logger.info("Creating new category for tickets");
            category = guild.createCategory("Tickets").complete();
        }

        if (category == null) {
            logger.error("Failed to create category for tickets");
            return null;
        }

        for (Role role : roles) {
            category.upsertPermissionOverride(role)
                    .grant(Permission.VIEW_CHANNEL)
                    .grant(Permission.ALL_CHANNEL_PERMISSIONS)
                    .grant(Permission.MESSAGE_SEND)
                    .grant(Permission.MESSAGE_HISTORY)
                    .queue();
        }
        category.upsertPermissionOverride(guild.getPublicRole())
                .deny(Permission.VIEW_CHANNEL)
                .queue();

        return category;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getGuild() != guild) return;
        Ticket ticket = ticketManager.getTicket(event.getChannel().getId());
        if (ticket == null) return;
        universe.storage().registerMessage(new Message(ticket.getId(), event.getMessage()));
    }

    @Override
    public void onChannelDelete(ChannelDeleteEvent event) {
        if (event.getGuild() != guild) return;
        Ticket ticket = ticketManager.getTicket(event.getChannel().getId());
        if (ticket == null) return;
        ticketManager.closeTicket(ticket.getChannelId(), "Channel deleted");
    }


}
