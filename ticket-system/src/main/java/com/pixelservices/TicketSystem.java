package com.pixelservices;

import com.pixelservices.api.modules.MbModule;
import com.pixelservices.listeners.OnBotReady;
import com.pixelservices.plugin.configuration.PluginConfig;
import com.pixelservices.utils.EmbedUtil;
import net.dv8tion.jda.api.sharding.ShardManager;

public class TicketSystem extends MbModule {
    private ShardManager shardManager;
    private EmbedUtil embedUtil;
    private PluginConfig embedConfig;


    @Override
    public void onEnable() {
        getLogger().debug("Loading module.");

        shardManager = getShardManager();

        embedConfig = getConfig("customization/embeds.yml");
        embedConfig.save();

        // utils
        getLogger().debug("Loading Utils");
        embedUtil = new EmbedUtil(this);

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
        registerEventListener(new OnBotReady(this));
    }

    /// Getters
    public EmbedUtil getEmbedUtil() {
        return embedUtil;
    }
    public PluginConfig getEmbedConfig() {
        return embedConfig;
    }
}
