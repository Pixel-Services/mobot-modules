package dev.siea.global;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.simpleyaml.configuration.ConfigurationSection;

import java.awt.*;
import java.util.HashMap;

public class Messages {
    private final ConfigurationSection messageSection;
    private final HashMap<String, String> messageCache = new HashMap<>();

    public Messages(ConfigurationSection messages) {
        this.messageSection = messages;
    }

    public String get(String key) {
        return messageCache.computeIfAbsent(key, n -> getMessage(key));
    }

    public MessageEmbed generateEmbed(String title, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setTitle(title);
        builder.setAuthor("Tickets");
        builder.setColor(Color.decode("#A020F0"));
        return builder.build();
    }

    private String getMessage(String key){
        return messageSection.getString(key, "Message not found for key: " + key);
    }
}
