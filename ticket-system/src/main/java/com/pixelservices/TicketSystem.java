package com.pixelservices;

import com.pixelservices.api.modules.MbModule;
import com.pixelservices.cache.ChannelCache;
import com.pixelservices.cache.RoleCache;
import com.pixelservices.listeners.OnBotReady;
import com.pixelservices.listeners.OnTicketClose;
import com.pixelservices.listeners.OnTicketOpen;
import com.pixelservices.plugin.configuration.PluginConfig;
import com.pixelservices.utils.EmbedUtil;
import com.pixelservices.utils.Placeholders;
import net.dv8tion.jda.api.sharding.ShardManager;

public class TicketSystem extends MbModule {
    private ShardManager shardManager;
    private EmbedUtil embedUtil;
    private PluginConfig embedConfig;
    private ChannelCache channelCache;
    private RoleCache roleCache;
    private Placeholders placeholders;


    @Override
    public void onEnable() {
        getLogger().debug("Loading module.");

        shardManager = getShardManager();

        // Creating config.yml
        getDefaultConfig();
        saveDefaultConfig();

        // Creating embeds.yml
        embedConfig = getConfig("customization/embeds.yml");
        embedConfig.save();

        // utils
        getLogger().debug("Loading Utils");
        embedUtil = new EmbedUtil(this);
        channelCache = new ChannelCache(null, null, null);
        roleCache = new RoleCache(null);
        placeholders = new Placeholders();



        // listeners
        getLogger().debug("Registering listeners");
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    private void registerListeners(){
        registerEventListener(new OnBotReady(this), new OnTicketOpen(this), new OnTicketClose(this));
    }

    /// Getters
    public EmbedUtil getEmbedUtil() {
        return embedUtil;
    }
    public PluginConfig getEmbedConfig() {
        return embedConfig;
    }
    public ChannelCache getChannelCache() {
        return channelCache;
    }
    public RoleCache getRoleCache() {
        return roleCache;
    }
    public Placeholders getPlaceholders() {
        return placeholders;
    }
}
