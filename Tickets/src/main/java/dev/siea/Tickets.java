package dev.siea;

import dev.siea.global.GlobalSettings;
import dev.siea.global.Universe;
import dev.siea.manager.GuildManager;
import dev.siea.global.Messages;
import dev.siea.storage.Storage;
import dev.siea.storage.db.MySQLWrapper;
import dev.siea.storage.file.FileWrapper;
import net.dv8tion.jda.api.entities.Guild;
import net.vitacraft.api.MBModule;
import net.vitacraft.api.config.ConfigLoader;
import org.simpleyaml.configuration.Configuration;

import java.util.HashMap;

public class Tickets extends MBModule {
        private Storage storage;
        private final HashMap<Guild, GuildManager> guilds = new HashMap<>();
        private Messages messages;

        @Override
        public void onEnable() {
            Configuration config = getConfigLoader().getConfig();
            String storageType = getConfigLoader().getConfig().getString("type");

            switch (storageType.toUpperCase()) {
                case "FILE" -> storage = new FileWrapper();
                case "MYSQL" -> storage = new MySQLWrapper(config.getConfigurationSection("credentials"), getLogger());
                default -> getLogger().error("Invalid storage type specified in the configuration file!");
            }

            if (storage == null) {
                getLogger().error("An error occurred while initializing the storage system!");
                getLogger().error("Please check your configuration file!");
                return;
            }

            GlobalSettings globalSettings = new GlobalSettings(config.getBoolean("delete_on_close"), config.getBoolean("save_messages_to_db"), config.getBoolean("archive_completed_tickets"));
            Universe universe = new Universe(storage, messages, getLogger(), globalSettings);

            for (Guild guild : getBotEnvironment().getShardManager().getGuilds()) {
                ConfigLoader guildConfig = getConfigLoader("guilds/" + guild.getId());
                guildConfig.save();
                guilds.put(guild, new GuildManager(guild, universe, guildConfig));
            }

            messages = new Messages(config.getConfigurationSection("messages"));
        }
}
