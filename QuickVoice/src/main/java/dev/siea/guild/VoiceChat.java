package dev.siea.guild;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

public class VoiceChat {
    private final VoiceChannel channel;
    private String ownerId;
    private final List<String> connectedUsers = new ArrayList<>();

    public VoiceChat(VoiceChannel channel, Member creator) {
        this.channel = channel;
        this.ownerId = creator.getId();
        this.adjustPermissions(creator);
    }

    public void userConnected(Member member) {
        if (!this.connectedUsers.contains(member.getId())) {
            this.connectedUsers.add(member.getId());
        }

    }

    public boolean userDisconnected(Member member) {
        this.connectedUsers.remove(member.getId());
        if (member.getId().equals(this.ownerId)) {
            this.ownerId = this.connectedUsers.isEmpty() ? null : (String)this.connectedUsers.get(0);
            if (this.ownerId != null) {
                this.adjustPermissions(this.channel.getGuild().getMemberById(this.ownerId));
            }
        }

        return !this.connectedUsers.isEmpty();
    }

    public VoiceChannel getChannel() {
        return this.channel;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public List<String> getConnectedUsers() {
        return this.connectedUsers;
    }

    private void adjustPermissions(Member owner) {
        if (owner != null && this.channel != null) {
            this.channel.getPermissionOverrides().forEach((override) -> {
                override.delete().queue();
            });
            ((VoiceChannelManager)this.channel.getManager().putMemberPermissionOverride(owner.getIdLong(), Arrays.asList(Permission.values()), List.of())).queue();
        }

    }
}
