package com.pixelservices.listeners;

import com.pixelservices.TicketSystem;
import com.pixelservices.data.EmbedData;
import com.pixelservices.mobot.api.modules.listener.ModuleListener;
import com.pixelservices.plugin.configuration.PluginConfig;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class OnBotReady extends ModuleListener {
    private TicketSystem module;

    public OnBotReady(TicketSystem module){
        this.module = module;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();

        String ticketCategoryName;
        String ticketChannelName;
        String archiveCategoryName;

        if (module.getDefaultConfig().getString("names.ticket-category") != null && !module.getDefaultConfig().getString("names.ticket-category").isEmpty()){
            ticketCategoryName = module.getDefaultConfig().getString("names.ticket-category");
        }else{
            ticketCategoryName = "Tickets";
        }

        if (module.getDefaultConfig().getString("names.ticket-channel") != null && !module.getDefaultConfig().getString("names.ticket-channel").isEmpty()){
            ticketChannelName = module.getDefaultConfig().getString("names.ticket-channel");
        }else{
            ticketChannelName = "Tickets";
        }

        if (module.getDefaultConfig().getString("names.archive-category") != null && !module.getDefaultConfig().getString("names.archive-category").isEmpty()){
            archiveCategoryName = module.getDefaultConfig().getString("names.archive-category");
        }else{
            archiveCategoryName = "archive";
        }

        ensureSupporterRoleExists(guild);
        ensureCategoryAndChannelExist(guild, ticketCategoryName, ticketChannelName, archiveCategoryName);
    }

    private void ensureCategoryAndChannelExist(Guild guild, String ticketCategoryName, String ticketChannelName, String archiveCategoryName) {
        // Ensure category exists
        Category category = guild.getCategories().stream()
                .filter(cat -> cat.getName().equalsIgnoreCase(ticketCategoryName))
                .findFirst()
                .orElseGet(() -> createTicketCategory(guild, ticketCategoryName));

        Category archive = guild.getCategories().stream()
                .filter(category1 -> category1.getName().equalsIgnoreCase(archiveCategoryName))
                .findFirst()
                .orElseGet(() -> createArchiveCategory(guild, archiveCategoryName))
                ;

        // Ensure channel exists within category
        TextChannel ticketChannel = category.getChannels().stream()
                .filter(ch -> ch instanceof TextChannel && ch.getName().equalsIgnoreCase(ticketChannelName))
                .map(ch -> (TextChannel) ch)
                .findFirst()
                .orElseGet(() -> createTextChannel(guild, category, ticketChannelName));

        if (category != null) {
            module.getChannelCache().setTicketCategory(category);
        }

        if (archive != null){
            module.getChannelCache().setArchiveCategory(archive);
        }

        if (ticketChannel != null){
            module.getChannelCache().setTicketChannel(ticketChannel);
        }
    }

    /// Tickets Category
    private Category createTicketCategory(Guild guild, String name) {
        Role role = guild.getPublicRole(); // Replace with the role you want

        Category category = guild.createCategory(name).complete();
        module.getLogger().debug("Category created: " + category.getName());

        // Add permissions to the category
        category.upsertPermissionOverride(role)
                .grant(Permission.VIEW_CHANNEL)
                .deny(Permission.MESSAGE_SEND)
                .queue(success -> module.getLogger().debug("Category permissions set for role: " + role.getName()),
                        error -> module.getLogger().error("Failed to set category permissions: " + error.getMessage()));

        return category;
    }

    /// Archive Category
    private Category createArchiveCategory(Guild guild, String name) {
        Role role = guild.getPublicRole(); // Replace with the role you want

        Category category = guild.createCategory(name).complete();
        module.getLogger().debug("Category created: " + category.getName());

        // Add permissions to the category
        category.upsertPermissionOverride(role)
                .grant()
                .deny(Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL)
                .queue(success -> module.getLogger().debug("Category permissions set for role: " + role.getName()),
                        error -> module.getLogger().error("Failed to set category permissions: " + error.getMessage()));

        return category;
    }

    /// Tickets Text Channel
    private TextChannel createTextChannel(Guild guild, Category category, String name) {
        Role role = guild.getPublicRole(); // Replace with the role you want

        PluginConfig config = module.getEmbedConfig();

        TextChannel textChannel = category.createTextChannel(name).complete();
        module.getLogger().debug("Text channel created: " + textChannel.getName());

        // Add permissions to the text channel
        textChannel.upsertPermissionOverride(role)
                .grant(Permission.MESSAGE_HISTORY)
                .deny(Permission.MESSAGE_SEND)
                .queue(success -> module.getLogger().debug("Text channel permissions set for role: " + role.getName()),
                        error -> module.getLogger().error("Failed to set text channel permissions: " + error.getMessage()));

        EmbedData data = new EmbedData(guild.getSelfMember().getUser(), guild);

        if (config.getBoolean("embeds.normal_ticket.enabled")){
            textChannel.sendMessageEmbeds(module.getEmbedUtil().customEmbed("normal_ticket", data)).setActionRow(Button.primary("open-normal-ticket", "📩")).queue();
        }

        return textChannel;
    }

    ///  SupporterRole
    private void ensureSupporterRoleExists(Guild guild) {
        // Check if role exists
        Role role = guild.getRoles().stream()
                .filter(r -> r.getName().equalsIgnoreCase("Supporter"))
                .findFirst()
                .orElseGet(() -> createSupporterRole(guild));

        if (role != null) {
            module.getLogger().debug("Role already exists: " + role.getName());
        }

        module.getRoleCache().setSupporter(role);
    }

    private Role createSupporterRole(Guild guild) {
        Role role = guild.createRole()
                .setName("Supporter")
                .setHoisted(true)
                .setColor(0x1ABC9C) // Example: Green color
                .setPermissions() // Example permissions
                .complete();

        module.getLogger().debug("Role created: " + role.getName());

        return role;
    }

}
