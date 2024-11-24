package dev.siea.listeners;

import dev.siea.guild.VoiceChatManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChatListener extends ListenerAdapter {
    private final String autoVoiceChat;
    private final VoiceChatManager manager;

    public VoiceChatListener(VoiceChatManager manager, String autoVoiceChat) {
        this.manager = manager;
        this.autoVoiceChat = autoVoiceChat;
    }

    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent e) {
        Member member = e.getEntity();
        AudioChannelUnion var4 = e.getChannelJoined();
        if (var4 instanceof VoiceChannel channel) {
            if (this.autoVoiceChat.equals(channel.getId())) {
                this.manager.createVoiceChat(member);
            } else {
                this.manager.userConnectionStatusChanged(member, channel.getId(), true);
            }
        }

        var4 = e.getChannelLeft();
        if (var4 instanceof VoiceChannel channel) {
            if (this.autoVoiceChat.equals(channel.getId())) {
                return;
            }

            this.manager.userConnectionStatusChanged(member, channel.getId(), false);
        }

    }
}
