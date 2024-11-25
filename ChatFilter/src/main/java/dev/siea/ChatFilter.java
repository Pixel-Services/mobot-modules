package dev.siea;

import dev.siea.manager.FilterManager;
import net.vitacraft.api.MBModule;

public class ChatFilter extends MBModule {

    @Override
    public void onEnable() {
        this.registerEventListener(new FilterManager(this));
    }
}