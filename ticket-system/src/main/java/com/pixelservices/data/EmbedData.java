package com.pixelservices.data;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class EmbedData {
    /// provide placeholders for in the Configs
    private String username;
    private String userId;
    private String userIcon;
    private String guildName;
    private String guildId;
    private String guildIcon;

    public EmbedData(User user, Guild guild){
        this.username = user.getName() != null ? user.getName() : null;
        this.userId = user.getId() != null ? user.getId() : null;
        this.userIcon = user.getAvatarUrl() != null ? user.getAvatarUrl() : null;
        this.guildName = guild.getName() != null ? guild.getName() : null;
        this.guildId = guild.getId() != null ? guild.getId() : null;
        this.guildIcon = guild.getIconUrl() != null ? guild.getIconUrl() : null;
    }

    public String getUsername() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public String getGuildName() {
        return guildName;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getGuildIcon() {
        return guildIcon;
    }
}
