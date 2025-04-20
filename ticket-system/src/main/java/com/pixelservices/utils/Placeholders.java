package com.pixelservices.utils;

import com.pixelservices.data.EmbedData;

import java.util.Map;

public class Placeholders {
    public String checkEmbedPlaceHolders(String text, EmbedData embedData){
        if (text == null ) return "";
        Map<String, String> replacements = Map.of(
                "%username%", embedData.getUsername() != null ? embedData.getUsername() : "%username%",
                "%userid%", embedData.getUserId() != null ? embedData.getUserId() : "%userid%",
                "%usericon%", embedData.getUserIcon() != null ? embedData.getUserIcon() : "https://th.bing.com/th/id/OIP.klzJHptpCAiuyVNQ9EUumgAAAA?rs=1&pid=ImgDetMain",
                "%guildname%", embedData.getGuildName() != null ? embedData.getGuildName() : "%guildname%",
                "%guildid%", embedData.getGuildId() != null ? embedData.getGuildId() : "%guildid%",
                "%guildicon%", embedData.getGuildIcon() != null ? embedData.getGuildIcon() : "https://th.bing.com/th/id/OIP.klzJHptpCAiuyVNQ9EUumgAAAA?rs=1&pid=ImgDetMain"
        );

        for (Map.Entry<String, String> entry : replacements.entrySet()){
            if (text.contains(entry.getKey())){
                text = text.replace(entry.getKey(), entry.getValue());
            }
        }

        return text;
    }
}
