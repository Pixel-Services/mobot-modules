package dev.siea.storage.file;

import dev.siea.storage.Storage;
import dev.siea.storage.models.Message;
import dev.siea.storage.models.Ticket;
import dev.siea.storage.models.enums.Status;

public class FileWrapper implements Storage {

    public FileWrapper() {

    }

    @Override
    public Message[] getMessages(int ticketId) {
        return new Message[0];
    }

    @Override
    public Ticket getTicket(int ticketId) {
        return null;
    }

    @Override
    public Ticket[] getTickets(String userId, String id) {
        return new Ticket[0];
    }

    @Override
    public Ticket[] getTickets(int index, int count, String id) {
        return new Ticket[0];
    }

    @Override
    public Ticket[] getTickets(Status status, String id) {
        return new Ticket[0];
    }

    @Override
    public Ticket registerTicket(String openedBy, String channelId, String guildId) {
        return null;
    }

    @Override
    public void updateTicket(Ticket ticket) {

    }

    @Override
    public void registerMessage(Message message) {

    }
}
