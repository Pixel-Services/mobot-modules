package dev.siea.storage.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.siea.storage.Storage;
import dev.siea.storage.models.Message;
import dev.siea.storage.models.Ticket;
import dev.siea.storage.models.enums.Status;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLWrapper implements Storage {
    private final HikariDataSource dataSource;
    private final Logger logger;

    public MySQLWrapper(ConfigurationSection configuration, Logger logger) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configuration.getString("url"));
        config.setUsername(configuration.getString("username"));
        config.setPassword(configuration.getString("password"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
        this.logger = logger;
    }

    @Override
    public Message[] getMessages(int ticketId) {
        List<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM Messages WHERE ticket_id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, ticketId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Message message = new Message(
                            resultSet.getInt("id"),
                            resultSet.getInt("ticket_id"),
                            resultSet.getString("sender_id"),
                            resultSet.getString("message"),
                            resultSet.getTimestamp("timestamp")
                    );
                    messages.add(message);
                }
        } catch (SQLException e) {
            logger.error("An error occurred while fetching messages for ticket {}", ticketId);
        }

        return messages.toArray(new Message[0]);
    }

    @Override
    public Ticket getTicket(int ticketId) {
        Ticket ticket = null;
        String query = "SELECT * FROM Tickets WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, ticketId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    ticket = new Ticket(
                            resultSet.getInt("id"),
                            resultSet.getString("opened_by"),
                            resultSet.getString("claimed_by"),
                            resultSet.getString("status"),
                            resultSet.getString("channel_id"),
                            resultSet.getString("guild_id")
                    );
                }
        } catch (SQLException e) {
            logger.error("An error occurred while fetching ticket {}", ticketId);
        }

        return ticket;
    }

    @Override
    public Ticket[] getTickets(String userId, String guildId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE opened_by = ? AND guild_id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, userId);
                statement.setString(2, guildId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Ticket ticket = new Ticket(
                            resultSet.getInt("id"),
                            resultSet.getString("opened_by"),
                            resultSet.getString("claimed_by"),
                            resultSet.getString("status"),
                            resultSet.getString("channel_id"),
                            resultSet.getString("guild_id")
                    );
                    tickets.add(ticket);
                }
        } catch (SQLException e) {
            logger.error("An error occurred while fetching tickets for user {}", userId);
        }

        return tickets.toArray(new Ticket[0]);
    }

    @Override
    public Ticket[] getTickets(int index, int count, String guildId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE guild_id = ? LIMIT ?, ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, guildId);
                statement.setInt(2, index);
                statement.setInt(3, count);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Ticket ticket = new Ticket(
                            resultSet.getInt("id"),
                            resultSet.getString("opened_by"),
                            resultSet.getString("claimed_by"),
                            resultSet.getString("status"),
                            resultSet.getString("channel_id"),
                            resultSet.getString("guild_id")
                    );
                    tickets.add(ticket);
                }
        } catch (SQLException e) {
            logger.error("An error occurred while fetching tickets");
        }

        return tickets.toArray(new Ticket[0]);
    }

    @Override
    public Ticket[] getTickets(Status status, String guildId) {
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM Tickets WHERE status = ? AND guild_id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, status.toString());
                statement.setString(2, guildId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    Ticket ticket = new Ticket(
                            resultSet.getInt("id"),
                            resultSet.getString("opened_by"),
                            resultSet.getString("claimed_by"),
                            resultSet.getString("status"),
                            resultSet.getString("channel_id"),
                            resultSet.getString("guild_id")
                    );
                    tickets.add(ticket);
                }
        } catch (SQLException e) {
            logger.error("An error occurred while fetching tickets with status {}", status);
        }

        return tickets.toArray(new Ticket[0]);
    }

    @Override
    public Ticket registerTicket(String openedBy, String channelId, String guildId) {
        String query = "INSERT INTO Tickets (opened_by, claimed_by, status, channel_id, guild_id) VALUES (?, ?, ?, ?, ?)";
        Ticket ticket = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, openedBy);
            statement.setString(2, null);
            statement.setString(3, Status.OPEN.toString());
            statement.setString(4, channelId);
            statement.setString(5, guildId);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int ticketId = generatedKeys.getInt(1);
                ticket = getTicket(ticketId);
            }
        } catch (SQLException e) {
            logger.error("An error occurred while registering ticket", e);
        }

        return ticket;
    }

    @Override
    public void updateTicket(Ticket ticket) {
        String query = "UPDATE Tickets SET opened_by = ?, claimed_by = ?, status = ?, channel_id = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, ticket.getOpenedBy());
                statement.setString(2, ticket.getClaimedBy());
                statement.setString(3, ticket.getStatus().toString());
                statement.setString(4, ticket.getChannelId());
                statement.setInt(5, ticket.getId());
                statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("An error occurred while updating ticket");
        }
    }

    @Override
    public void registerMessage(Message message) {
        String query = "INSERT INTO Messages (ticket_id, sender_id, message, timestamp) VALUES (?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, message.getTicketId());
                statement.setString(2, message.getSenderId());
                statement.setString(3, message.getMessage());
                statement.setTimestamp(4, message.getTimestamp());
                statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("An error occurred while registering message");
        }
    }
}