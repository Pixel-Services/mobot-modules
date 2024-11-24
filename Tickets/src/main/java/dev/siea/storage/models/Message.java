package dev.siea.storage.models;

import java.sql.Timestamp;

public class Message {
    private int id;
    private final int ticketId;
    private final String senderId;
    private final String message;
    private final Timestamp timestamp;

    public Message(int id, int ticketId, String senderId, String message, Timestamp timestamp) {
        this.id = id;
        this.ticketId = ticketId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(int ticketId, net.dv8tion.jda.api.entities.Message message) {
        this.ticketId = ticketId;
        this.senderId = message.getAuthor().getId();
        this.message = message.getContentRaw();
        this.timestamp = new Timestamp(message.getTimeCreated().toInstant().toEpochMilli());
    }

    public int getId() {
        return id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }


    public Timestamp getTimestamp() {
        return timestamp;
    }
}