package dev.siea.guild;

import dev.siea.QuickVoice;
import dev.siea.listeners.VoiceChatListener;
import java.util.HashMap;
import java.util.Iterator;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.vitacraft.api.config.ConfigLoader;
import org.simpleyaml.configuration.ConfigurationSection;
import org.slf4j.Logger;

public class VoiceChatManager {
    private final Guild guild;
    private final QuickVoice quickVoice;
    private final HashMap<String, VoiceChat> voiceChats = new HashMap();
    private final Logger logger;
    private Category category;
    private VoiceChatListener voiceChatListener;

    public VoiceChatManager(Guild guild, QuickVoice quickVoice) {
        this.logger = quickVoice.getLogger();
        this.guild = guild;
        this.quickVoice = quickVoice;
        this.setupGuild();
    }

    public void createVoiceChat(Member member) {
        this.category.createVoiceChannel(member.getEffectiveName() + "'s Voice Chat").queue((channel) -> {
            VoiceChat voiceChat = new VoiceChat(channel, member);
            this.voiceChats.put(channel.getId(), voiceChat);
            this.guild.moveVoiceMember(member, channel).queue((success) -> {
                voiceChat.userConnected(member);
            });
        });
    }

    public void userConnectionStatusChanged(Member member, String channelId, boolean connected) {
        VoiceChat voiceChat = (VoiceChat)this.voiceChats.get(channelId);
        if (voiceChat != null) {
            this.logger.info("Voice chat found for channel {}", channelId);
            if (connected) {
                this.logger.info("User {} connected to voice chat", member.getEffectiveName());
                voiceChat.userConnected(member);
            } else {
                this.logger.info("User {} disconnected from voice chat", member.getEffectiveName());
                if (!voiceChat.userDisconnected(member)) {
                    this.logger.info("Voice chat is empty, deleting");
                    this.voiceChats.remove(channelId);
                    voiceChat.getChannel().delete().queue();
                }
            }

        }
    }

    public void onDisable() {
        for (VoiceChat voiceChat : this.voiceChats.values()) {
            voiceChat.getChannel().delete().queue();
        }
    }

    private void setupGuild() {
        ConfigLoader configLoader = this.quickVoice.getConfigLoader("data.yml");
        configLoader.save();
        ConfigurationSection section = configLoader.getConfig().getConfigurationSection(this.guild.getId());
        if (section == null) {
            section = configLoader.getConfig().createSection(this.guild.getId());
            configLoader.save();
        }

        String autoVoiceChatId = section.getString("autoVoiceChat");
        if (autoVoiceChatId != null && this.guild.getVoiceChannelById(autoVoiceChatId) != null) {
            this.voiceChatListener = new VoiceChatListener(this, autoVoiceChatId);
            this.quickVoice.registerEventListener(new Object[]{this.voiceChatListener});
        } else {
            try {
                ConfigurationSection finalSection = section;
                this.guild.createVoiceChannel("New Voice (auto)").queue((channel) -> {
                    this.voiceChatListener = new VoiceChatListener(this, channel.getId());
                    this.quickVoice.registerEventListener(new Object[]{this.voiceChatListener});
                    finalSection.set("autoVoiceChat", channel.getId());
                    configLoader.save();
                });
                this.logger.info("Automatically created auto voice chat for {} because it was missing.", this.guild.getName());
            } catch (Exception var7) {
                Exception e = var7;
                this.logger.error("Failed to create auto voice chat: {}", e.getMessage());
            }
        }

        String categoryId = section.getString("category");
        if (categoryId == null || (this.category = this.guild.getCategoryById(categoryId)) == null) {
            try {
                this.category = (Category)this.guild.createCategory("Voice Chats").complete();
                section.set("category", this.category.getId());
                configLoader.save();
                this.logger.info("Automatically created Voice Chat category for {} because it was missing.", this.guild.getName());
            } catch (Exception var6) {
                Exception e = var6;
                this.logger.error("Failed to create category: {}", e.getMessage());
            }
        }

    }
}
