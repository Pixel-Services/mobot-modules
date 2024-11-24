package dev.siea.filter;

import dev.siea.punish.Punishment;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

public class LinkFilter implements Filter {
    private final Punishment punishment;
    private final boolean deleteMessages;
    private final Mode mode;
    private final Set<String> links;
    private final Logger logger;

    public LinkFilter(ConfigurationSection config, Logger logger) {
        this.punishment = new Punishment(config.getConfigurationSection("action.punishment"));
        this.deleteMessages = config.getBoolean("action.delete");
        this.mode = dev.siea.filter.LinkFilter.Mode.valueOf(config.getString("mode", "WHITELIST").toUpperCase());
        this.links = Set.copyOf(config.getStringList("list"));
        this.logger = logger;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        List<String> domains = this.extractDomains(message);

        for (String domain : domains) {
            if (this.mode == Mode.WHITELIST && !this.links.contains(domain)) {
                if (this.deleteMessages) {
                    event.getMessage().delete().queue();
                }

                this.punishment.punish(event.getMember(), this.logger);
                break;
            }

            if (this.mode == Mode.BLACKLIST && this.links.contains(domain)) {
                if (this.deleteMessages) {
                    event.getMessage().delete().queue();
                }

                this.punishment.punish(event.getMember(), this.logger);
                break;
            }
        }

    }

    private List<String> extractDomains(String input) {
        List<String> domains = new ArrayList<>();
        String regex = "\\b(?:https?|ftp)://(?:www\\.)?([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);

        while(matcher.find()) {
            domains.add(matcher.group(1));
        }

        return domains;
    }

    private enum Mode {
        BLACKLIST,
        WHITELIST
    }
}
