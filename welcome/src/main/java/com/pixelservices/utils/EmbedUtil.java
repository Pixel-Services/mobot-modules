package com.pixelservices.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbedUtil {
    public MessageEmbed welcome(Member member, Guild guild){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("New member!")
                .setAuthor(guild.getName(), "https://google.com/", guild.getIconUrl())
                .setDescription("Welcome " + member.getEffectiveName() + " to " + guild.getName() + ".")
                .setThumbnail(member.getAvatarUrl())
                .setColor(Color.BLUE)
                .setFooter("Powered by Pixel-Services", "https://pixel-services.com/")
        ;

        return embed.build();
    }

    public MessageEmbed goodbye(Member member, Guild guild){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("goodbye member!")
                .setAuthor(guild.getName(), "https://google.com/", guild.getIconUrl())
                .setDescription("bye " + member.getEffectiveName() + ".")
                .setThumbnail(member.getAvatarUrl())
                .setColor(Color.BLUE)
                .setFooter("Powered by Pixel-Services", "https://pixel-services.com/")
        ;

        return embed.build();
    }
}
