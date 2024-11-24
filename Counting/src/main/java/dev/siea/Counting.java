package dev.siea;

import dev.siea.data.CountingSettings;
import dev.siea.listeners.CountingListener;
import net.vitacraft.api.MBModule;
import net.vitacraft.api.PrimitiveBotEnvironment;

public class Counting extends MBModule {

    @Override
    public void preEnable(PrimitiveBotEnvironment primitiveBotEnvironment) {
        getConfigLoader().save();
    }

    @Override
    public void onEnable(){
        String countingChannelId = getConfigLoader().getConfig().getString("channel_id");
        int countAlongAfterSeconds = getConfigLoader().getConfig().getInt("count-along-after-seconds");
        boolean allowSameUserCount = getConfigLoader().getConfig().getBoolean("allow-same-user-count");
        if (countingChannelId == null || countingChannelId.equals("CHANNEL_ID")){
            getLogger().info("No channel set in config");
            return;
        }
        CountingSettings settings = new CountingSettings(countingChannelId, countAlongAfterSeconds, allowSameUserCount);
        registerEventListener(new CountingListener(settings, getLogger()));
    }
}
