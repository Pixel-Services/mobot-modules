package dev.siea.data;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.Permission;
import org.simpleyaml.configuration.ConfigurationSection;

public record BypassConfiguration(Set<Permission> bypassPermissions, Set<String> ignoredChannels) {
    public BypassConfiguration(ConfigurationSection config) {
        this(EnumSet.noneOf(Permission.class), new HashSet<>(config.getStringList("ignored-channels")));
        for (String permission : config.getStringList("bypass-permissions")) {
            this.bypassPermissions.add(Permission.valueOf(permission));
        }

    }

    public BypassConfiguration(Set<Permission> bypassPermissions, Set<String> ignoredChannels) {
        this.bypassPermissions = bypassPermissions;
        this.ignoredChannels = ignoredChannels;
    }

    public Set<Permission> bypassPermissions() {
        return this.bypassPermissions;
    }

    public Set<String> ignoredChannels() {
        return this.ignoredChannels;
    }
}