package dev.siea;

import dev.siea.commands.StatusCommand;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.vitacraft.api.MBModule;
import net.vitacraft.api.PrimitiveBotEnvironment;

public class StatusModule extends MBModule {

    @Override
    public void preEnable(PrimitiveBotEnvironment primitiveBotEnvironment) {
        getConfigLoader().save();
        String activityType = getConfigLoader().getConfig().getString("activity.type");
        String activityDetails = getConfigLoader().getConfig().getString("activity.details");
        String url = getConfigLoader().getConfig().getString("activity.streaming_url");
        Activity activity = switch (activityType.toUpperCase()) {
            case "STREAMING" -> Activity.streaming(activityDetails, url);
            case "LISTENING" -> Activity.listening(activityDetails);
            case "WATCHING" -> Activity.watching(activityDetails);
            default -> Activity.playing(activityDetails);
        };
        primitiveBotEnvironment.getBuilder().setActivity(activity);
    }

    @Override
    public void onEnable(){
        OptionData actionTypeOption = new OptionData(OptionType.STRING, "action_type", "Select an action type!", true)
                .addChoice("Streaming", "STREAMING")
                .addChoice("Listening", "LISTENING")
                .addChoice("Watching", "WATCHING")
                .addChoice("Playing", "PLAYING");

        OptionData activityDetailsOption = new OptionData(OptionType.UNKNOWN, "activity_details", "Choose action details!", true);
        OptionData streamingUrlOption = new OptionData(OptionType.UNKNOWN, "streaming_url", "Set a streaming URL!", false);
        CommandData statusCommandData = Commands.slash("status", "Change the Bots Status").addOptions(actionTypeOption, activityDetailsOption, streamingUrlOption);

        registerSlashCommand(statusCommandData, new StatusCommand());
    }
}
