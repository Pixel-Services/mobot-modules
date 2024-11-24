package dev.siea.storage;

import dev.siea.storage.models.Message;
import dev.siea.storage.models.Ticket;
import dev.siea.storage.models.enums.Status;

public interface Storage {
    Message[] getMessages(int ticketId);
    Ticket getTicket(int ticketId);
    Ticket[] getTickets(String userId, String guildId);
    Ticket[] getTickets(int index, int count, String guildId);
    Ticket[] getTickets(Status status, String guildId);
    Ticket registerTicket(String openedBy, String channelId, String guildId);
    void updateTicket(Ticket ticket);
    void registerMessage(Message message);
}
