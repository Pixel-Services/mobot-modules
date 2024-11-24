package dev.siea;

import dev.siea.listener.DefaultListeners;
import dev.siea.logger.CustomAppender;
import dev.siea.message.BetterEmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.vitacraft.api.MBModule;
import org.simpleyaml.configuration.ConfigurationSection;
import java.util.HashMap;
import java.util.Map;

public class ConsoleLink extends MBModule {
    private CustomAppender appender;
    private Map<String, String> consoleChannels = new HashMap<>();

    @Override
    public void onEnable() {
        consoleChannels = new HashMap<>();
        appender = new CustomAppender();
        ConfigurationSection section = getConfigLoader().getConfig().getConfigurationSection("console_channels");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                consoleChannels.put(key, section.getString(key));
            }
        }
        registerEventListener(new DefaultListeners(this));
    }

    @Override
    public void onDisable() {
        appender.stop();
    }

    public void guildReady(Guild guild) {
        String channelId = consoleChannels.get(guild.getId());

        if (channelId == null) {
            getLogger().error("No console channel found for guild {}!", guild.getName());
            return;
        }

        TextChannel channel = getBotEnvironment().getShardManager().getTextChannelById(channelId);

        if (channel != null) {
            channel.sendMessageEmbeds(BetterEmbedBuilder.generateEmbed("Successfully linked console to this channel!")).queue();
            appender.addChannel(channel);
        } else {
            getLogger().error("Channel with ID {} not found in {}!", channelId, guild.getName());
        }
    }
}