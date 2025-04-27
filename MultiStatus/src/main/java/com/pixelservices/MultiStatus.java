package com.pixelservices;

import com.pixelservices.mobot.api.modules.MbModule;
import com.pixelservices.plugin.configuration.PluginConfig;
import net.dv8tion.jda.api.entities.Activity;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MultiStatus extends MbModule {
    private int currentIndex = 0;
    PluginConfig config;

    @Override
    public void onEnable() {
        config = getDefaultConfig();
        config.save();
        List<String> statusses = (List<String>) config.get("status.list");

        if (statusses == null || statusses.isEmpty()){
            getBotEnvironment().getShardManager().getShards().forEach(jda -> {
                jda.getPresence().setActivity(Activity.customStatus("Status not found!"));
            });
        }else{
            getLogger().debug("Activated Status Switcher");
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                currentIndex = (currentIndex + 1) % statusses.toArray().length;
                getBotEnvironment().getShardManager().getShards().forEach(jda -> {
                    jda.getPresence().setActivity(Activity.customStatus(statusses.get(currentIndex)));
                });
            }, 0, config.getInt("status.time"), TimeUnit.SECONDS);
        }
    }

    @Override
    public void onDisable() {

    }
}