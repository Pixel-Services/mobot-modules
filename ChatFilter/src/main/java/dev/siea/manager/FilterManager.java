package dev.siea.manager;

import dev.siea.ChatFilter;
import dev.siea.data.BypassConfiguration;
import dev.siea.filter.Filter;
import dev.siea.filter.LinkFilter;
import dev.siea.filter.SpamFilter;
import dev.siea.filter.WordFilter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.slf4j.Logger;

public class FilterManager extends ListenerAdapter {
    private final List<Filter> filters = new ArrayList<>();
    private final BypassConfiguration bypassConfig;

    public FilterManager(ChatFilter module) {
        FileConfiguration config = module.getConfigLoader().getConfig();
        this.bypassConfig = new BypassConfiguration(config);
        this.registerFilters(config, module.getLogger());
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!this.bypassConfig.ignoredChannels().contains(event.getChannel().getId())) {
            Member member = event.getMember();
            if (member != null) {
                Iterator var3 = this.bypassConfig.bypassPermissions().iterator();

                while(var3.hasNext()) {
                    Permission permission = (Permission)var3.next();
                    if (member.hasPermission(permission)) {
                        return;
                    }
                }

                var3 = this.filters.iterator();

                while(var3.hasNext()) {
                    Filter filter = (Filter)var3.next();
                    filter.onMessageReceived(event);
                }

            }
        }
    }

    private void registerFilters(FileConfiguration config, Logger logger) {
        if (config.getBoolean("word-filter.enabled")) {
            this.filters.add(new WordFilter(config.getConfigurationSection("word-filter"), logger));
        }

        if (config.getBoolean("spam-filter.enabled")) {
            this.filters.add(new SpamFilter(config.getConfigurationSection("spam-filter"), logger));
        }

        if (config.getBoolean("link-filter.enabled")) {
            this.filters.add(new LinkFilter(config.getConfigurationSection("link-filter"), logger));
        }

    }
}
