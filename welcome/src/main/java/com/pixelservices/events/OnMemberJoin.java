package com.pixelservices.events;

import com.pixelservices.Welcome;
import com.pixelservices.plugin.configuration.PluginConfig;
import com.pixelservices.plugin.configuration.YamlPluginConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class OnMemberJoin extends ListenerAdapter {
    private Welcome module;
    private PluginConfig config;

    public OnMemberJoin(Welcome module){
        this.module = module;
        this.config = module.getConfig();
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel channel = module.getShardManager().getTextChannelById(config.getString("channel.welcome"));
        Member member = event.getMember();

        MessageEmbed embed = module.getEmbedUtil().welcome(member, event.getGuild());

        if (channel == null){
            return;
        }

        channel.sendMessageEmbeds(embed);
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel channel = module.getShardManager().getTextChannelById(config.getString("channel.leave"));
        Member member = event.getMember();

        MessageEmbed embed = module.getEmbedUtil().goodbye(member, event.getGuild());

        if (channel == null){
            return;
        }

        channel.sendMessageEmbeds(embed);
    }
}
