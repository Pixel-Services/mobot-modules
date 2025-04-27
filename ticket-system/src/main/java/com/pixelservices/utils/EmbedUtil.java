package com.pixelservices.utils;

import com.pixelservices.TicketSystem;
import com.pixelservices.data.EmbedData;
import com.pixelservices.plugin.configuration.PluginConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class EmbedUtil {
    private TicketSystem module;
    private PluginConfig config;

    public EmbedUtil(TicketSystem module){
        this.module = module;
        config = module.getConfig("customization/embeds.yml");
    }

    public MessageEmbed customEmbed(String embedName, EmbedData embedData){
        EmbedBuilder embed = new EmbedBuilder();
        String search = "embeds." + embedName + ".";

        String authorText = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "author.text"), embedData);
        String authorUrl = config.getString(search + "author.url");
        String authorIcon;
        String thumbnailImage = config.getString(search + "thumbnail.image");
        String titleText = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "title.text"), embedData);
        String titleURL = config.getString(search + "title.url");
        String descriptionText = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "description.text"), embedData);
        List<Map<String, Object>> fields = (List<Map<String, Object>>) config.get(search + "fields");
        String imageURL = config.getString(search + "image.image");
        String footerText = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "footer.text"), embedData);
        String footerIcon;
        boolean timeStamp = config.getBoolean(search + "timestamp.enabled");

        if (config.getString(search + "author.icon") != null && !config.getString(search + "author.icon").isEmpty()){
            authorIcon = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "author.icon"), embedData) ;
        }else{
            authorIcon = "https://th.bing.com/th/id/OIP.klzJHptpCAiuyVNQ9EUumgAAAA?rs=1&pid=ImgDetMain";
        }

        if (config.getString(search + "footer.icon") != null && !config.getString(search + "footer.icon").isEmpty()){
            footerIcon = module.getPlaceholders().checkEmbedPlaceHolders(config.getString(search + "footer.icon"), embedData);
        }else{
            footerIcon = "https://th.bing.com/th/id/OIP.klzJHptpCAiuyVNQ9EUumgAAAA?rs=1&pid=ImgDetMain";
        }

        // adding author to the embed.
        if (authorText != null && !authorText.isEmpty()){
            if (authorUrl != null && !authorUrl.isEmpty()){
                if (authorIcon != null && (authorIcon.startsWith("http://") || authorIcon.startsWith("https://"))){
                    embed.setAuthor(authorText, authorUrl, authorIcon);
                }else{
                    embed.setAuthor(authorText, authorUrl);
                }
            }else{
                if (authorIcon != null && (authorIcon.startsWith("http://") || authorIcon.startsWith("https://"))){
                    embed.setAuthor(authorText, null, authorIcon);
                }else{
                    embed.setAuthor(authorText, null);
                }
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
        if (fields != null && !fields.isEmpty()) {
            for (Map<String, Object> field : fields) {
                String name = module.getPlaceholders().checkEmbedPlaceHolders((String) field.get("name"), embedData);
                String value = module.getPlaceholders().checkEmbedPlaceHolders((String) field.get("value"), embedData);
                boolean inline = field.get("inline") != null && (boolean) field.get("inline");

                if (name != null && !name.isEmpty() && value != null && !value.isEmpty()) {
                    embed.addField(name, value, inline);
                }
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

        if (config.getString(search + "color.hex-code") != null && !config.getString(search + "color.hex-code").isEmpty()){
            embed.setColor(Color.decode(config.getString(search + "color.hex-code")));
        }

        return embed.build();
    }
}
