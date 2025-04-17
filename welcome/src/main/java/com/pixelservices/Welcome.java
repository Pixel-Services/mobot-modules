package com.pixelservices;

import com.pixelservices.api.modules.MbModule;
import com.pixelservices.events.OnMemberJoin;
import com.pixelservices.plugin.configuration.PluginConfig;
import com.pixelservices.utils.EmbedUtil;
import net.dv8tion.jda.api.sharding.ShardManager;


public class Welcome extends MbModule {
    private ShardManager shardManager;
    private EmbedUtil embedUtil;
    private PluginConfig config;

    @Override
    public void onEnable() {
        getLogger().debug("Enabling Welcome.");

        config = getDefaultConfig();
        saveDefaultConfig();


        shardManager = getBotEnvironment().getShardManager();

        getLogger().debug("Loading utils");
        embedUtil = new EmbedUtil();

        getLogger().debug("Registering Listeners.");
        registerListeners();

        getLogger().debug("Loading succes.");

    }

    @Override
    public void onDisable() {

    }

    private void registerListeners(){
        registerEventListener(new OnMemberJoin(this));
    }

    /// Getters
    public ShardManager getShardManager() {
        return shardManager;
    }

    public EmbedUtil getEmbedUtil() {
        return embedUtil;
    }

    public PluginConfig getConfig() {
        return config;
    }
}
