package dev.siea.storage.models;

import dev.siea.storage.models.enums.Status;

public class Ticket {
    private final String channelId;
    private final String guildId;
    private final int id;
    private String openedBy;
    private String claimedBy;
    private Status status;

    public Ticket(int id, String openedBy, String claimedBy, String status, String channelId, String guildId) {
        this.id = id;
        this.openedBy = openedBy;
        this.claimedBy = claimedBy;
        this.status = Status.valueOf(status);
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public Ticket(int id, String openedBy, String claimedBy, Status status, String channelId, String guildId) {
        this.id = id;
        this.openedBy = openedBy;
        this.claimedBy = claimedBy;
        this.status = status;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public int getId() {
        return id;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public String getClaimedBy() {
        return claimedBy;
    }

    public void setClaimedBy(String claimedBy) {
        this.claimedBy = claimedBy;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }
}