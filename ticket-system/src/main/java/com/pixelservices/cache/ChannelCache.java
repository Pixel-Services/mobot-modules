package com.pixelservices.cache;

import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;

public class ChannelCache {
    private Category ticketCategory;
    private Category archiveCategory;
    private Channel ticketChannel;

    public ChannelCache(Category ticketCategory, Category archiveCategory, Channel ticketChannel){
        this.ticketCategory = ticketCategory;
        this.archiveCategory = archiveCategory;
        this.ticketChannel = ticketChannel;
    }

    public Category getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(Category ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public Category getArchiveCategory() {
        return archiveCategory;
    }

    public void setArchiveCategory(Category archiveCategory) {
        this.archiveCategory = archiveCategory;
    }

    public Channel getTicketChannel() {
        return ticketChannel;
    }

    public void setTicketChannel(Channel ticketChannel) {
        this.ticketChannel = ticketChannel;
    }
}
