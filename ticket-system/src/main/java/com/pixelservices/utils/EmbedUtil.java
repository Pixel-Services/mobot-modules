package com.pixelservices.utils;

import com.pixelservices.TicketSystem;
import com.pixelservices.plugin.configuration.PluginConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class EmbedUtil {
    private TicketSystem module;
    private PluginConfig config;

    public EmbedUtil(TicketSystem module){
        this.module = module;
        config = module.getConfig("customization/embed.yml");
    }

    public MessageEmbed customEmbed(String embedName){
        EmbedBuilder embed = new EmbedBuilder();
        String search = embedName + ".";

        String authorUrl = config.getString(search + "author.url");
        String authorText = config.getString(search + "author.text");
        String authorIcon;
        String thubnailImage = config.getString(search + "thubnail.image");
        String titleText = config.getString(search + "title.text");
        String titleURL = config.getString(search + "title.url");
        String descriptionText = config.getString(search + "decription.text");
        List<Map<String, Object>> fields = (List<Map<String, Object>>) config.get(search + "fields");
        String imageURL = config.getString(search + "image.image");
        String footerText = config.getString(search + "footer.text");
        String footerIcon;
        boolean timeStamp = config.getBoolean(search + "timestamp.enabled");

        if (config.getString(search + "author.icon") != null && !config.getString(search + "author.icon").isEmpty()){
            authorIcon = config.getString(search + "author.icon");
        }else{
            authorIcon = null;
        }

        if (config.getString(search + "footer.icon") != null && !config.getString(search + "footer.icon").isEmpty()){
            footerIcon = config.getString(search + "footer.icon");
        }else{
            footerIcon = null;
        }



        // adding author to the embed.
        if (authorText != null && !authorText.isEmpty()){
            if (authorUrl != null && !authorUrl.isEmpty()){
                embed.setAuthor(authorText, authorUrl, authorIcon);
            }else{
                embed.setAuthor(authorText, null, authorIcon);
            }
        }

        // adding the thubnail to the embed.
        if (thubnailImage != null && !thubnailImage.isEmpty()){
            embed.setThumbnail(thubnailImage);
        }

        // adding a title to the embed.
        if (titleText != null && !titleText.isEmpty()){
            if (titleURL != null && !titleURL.isEmpty()){
                embed.setTitle(titleText, titleURL);
            }else{
                embed.setTitle(titleText);
            }
        }

        // Adding description to the embed.
        if (descriptionText != null && !descriptionText.isEmpty()){
            embed.setDescription(descriptionText);
        }

        // adding fields to the embed.
        if (fields != null && !fields.isEmpty()){
            for (Map<String, Object> field : fields) {
                String name = (String) field.get("name");
                String value = (String) field.get("value");
                boolean inline = field.get("inline") != null && (boolean) field.get("inline");

                embed.addField(name, value, inline);
            }
        }

        // Adding the image to the embed.
        if (imageURL != null && !imageURL.isEmpty()){
            embed.setImage(imageURL);
        }

        // adding the footer to the embed.
        if (footerText != null && !footerText.isEmpty()){
            if (footerIcon != null && !footerIcon.isEmpty()){
                embed.setFooter(footerText, footerIcon);
            }else{
                embed.setFooter(footerText);
            }
        }

        if (timeStamp){
            embed.setTimestamp(Instant.now());
        }

        return embed.build();
    }
}
