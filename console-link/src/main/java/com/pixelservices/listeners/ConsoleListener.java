package com.pixelservices.listeners;

import com.pixelservices.logger.events.LogEvent;
import com.pixelservices.logger.listeners.LoggerLogEventListener;
import com.pixelservices.manager.LinkedChannelRegistry;

public class ConsoleListener implements LoggerLogEventListener {
    private final LinkedChannelRegistry registry;

    public ConsoleListener(LinkedChannelRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void onLogEvent(LogEvent logEvent) {
        registry.logAllChannels(logEvent);
    }
}
