package dev.siea.filter;

import dev.siea.punish.Punishment;
import java.util.HashSet;
import java.util.Set;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

public class WordFilter implements Filter {
    private final Punishment punishment;
    private final Set<String> words;
    private final boolean deleteMessage;
    private final Logger logger;

    public WordFilter(ConfigurationSection config, Logger logger) {
        this.punishment = new Punishment(config.getConfigurationSection("action.punishment"));
        this.deleteMessage = config.getBoolean("action.delete");
        this.words = new HashSet<>(config.getStringList("blacklist"));
        this.logger = logger;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        for (String word : this.words) {
            if (message.toLowerCase().contains(word)) {
                if (this.deleteMessage) {
                    event.getMessage().delete().queue();
                }

                this.punishment.punish(event.getMember(), this.logger);
                break;
            }
        }
    }
}