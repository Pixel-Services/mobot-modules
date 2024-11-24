package dev.siea;

import dev.siea.guild.VoiceChatManagerRegistry;
import net.vitacraft.api.MBModule;

public class QuickVoice extends MBModule {
    private VoiceChatManagerRegistry registry;

    public QuickVoice() {
    }

    public void onEnable() {
        this.registry = new VoiceChatManagerRegistry(this);
    }

    public void onDisable() {
        this.registry.onDisable();
    }
}
