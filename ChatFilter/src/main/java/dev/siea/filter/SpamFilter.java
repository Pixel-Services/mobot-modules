package dev.siea.filter;

import dev.siea.punish.Punishment;
import java.util.HashMap;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

public class SpamFilter implements Filter {
    private final Punishment punishment;
    private final boolean deleteMessages;
    private final int messageLimit;
    private final HashMap<Member, Integer> messageCount = new HashMap<>();
    private final HashMap<Member, String> lastMessage = new HashMap<>();
    private final Logger logger;

    public SpamFilter(ConfigurationSection config, Logger logger) {
        this.punishment = new Punishment(config.getConfigurationSection("action.punishment"));
        this.deleteMessages = config.getBoolean("action.delete");
        this.messageLimit = config.getInt("max-messages");
        this.logger = logger;
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member != null) {
            String message = event.getMessage().getContentRaw();
            if (message.equals(this.lastMessage.get(member))) {
                this.messageCount.put(member, this.messageCount.getOrDefault(member, 0) + 1);
                if ((Integer)this.messageCount.get(member) >= this.messageLimit) {
                    this.punishment.punish(member, this.logger);
                }

                if (this.deleteMessages) {
                    event.getMessage().delete().queue();
                }
            } else {
                this.messageCount.put(member, 1);
                this.lastMessage.put(member, message);
            }

        }
    }
}
