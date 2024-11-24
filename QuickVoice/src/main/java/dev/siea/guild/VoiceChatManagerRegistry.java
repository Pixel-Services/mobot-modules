package dev.siea.guild;

import dev.siea.QuickVoice;
import dev.siea.listeners.GuildReadyListener;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.entities.Guild;

public class VoiceChatManagerRegistry {
    private final QuickVoice quickVoice;
    private final List<VoiceChatManager> voiceChatManagers = new ArrayList();

    public VoiceChatManagerRegistry(QuickVoice quickVoice) {
        this.quickVoice = quickVoice;
        quickVoice.registerEventListener(new Object[]{new GuildReadyListener(this)});
    }

    public void loadGuild(Guild guild) {
        this.voiceChatManagers.add(new VoiceChatManager(guild, this.quickVoice));
    }

    public void onDisable() {
        this.voiceChatManagers.forEach(VoiceChatManager::onDisable);
    }
}
