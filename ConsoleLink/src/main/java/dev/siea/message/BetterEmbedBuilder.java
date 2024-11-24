package dev.siea.message;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import java.awt.*;

public class BetterEmbedBuilder {

    public static MessageEmbed generateEmbed(String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(Color.decode("#A020F0"));
        builder.setFooter("Powered by Pixel Services");
        return builder.build();
    }
}
