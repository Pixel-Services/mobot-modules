package dev.siea.punish;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.entities.Member;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

public record Punishment(PunishmentType type, int duration, String message) {
    public Punishment(ConfigurationSection config) {
        this(PunishmentType.valueOf(config.getString("type", "WARN").toUpperCase()), config.getInt("duration", 0), config.getString("message", ""));
    }

    public void punish(Member member, Logger logger) {
        try {
            switch (this.type) {
                case BAN -> member.ban(0, TimeUnit.SECONDS).queue();
                case KICK -> member.kick().queue();
                case TIMEOUT -> member.timeoutFor(Duration.ofSeconds(this.duration)).queue();
            }
        } catch (Exception e) {
            logger.info("Unable to punish member with ID: {}. {}", member.getId(), e.getMessage());
            return;
        }

        if (!this.message.isEmpty()) {
            member.getUser().openPrivateChannel().flatMap((channel) -> channel.sendMessage(this.message)).queue();
        }
    }

    public PunishmentType type() {
        return this.type;
    }

    public int duration() {
        return this.duration;
    }

    public String message() {
        return this.message;
    }
}
