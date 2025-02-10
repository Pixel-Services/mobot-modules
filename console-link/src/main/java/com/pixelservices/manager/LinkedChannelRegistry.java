package com.pixelservices.manager;

import com.pixelservices.logger.events.LogEvent;

import java.util.ArrayList;
import java.util.List;

public class LinkedChannelRegistry {
    private final List<LinkedChannel> channels = new ArrayList<>();

    public LinkedChannelRegistry() {

    }

    public void logAllChannels(LogEvent logEvent) {

    }

    public List<LinkedChannel> getChannels() {
        return channels;
    }
}
