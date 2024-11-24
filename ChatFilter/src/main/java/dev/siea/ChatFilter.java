package dev.siea;

import dev.siea.manager.FilterManager;
import net.vitacraft.api.MBModule;

public class ChatFilter extends MBModule {
    public ChatFilter() {
    }

    public void onEnable() {
        this.registerEventListener(new Object[]{new FilterManager(this)});
    }
}