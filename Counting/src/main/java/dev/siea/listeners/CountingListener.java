package dev.siea.listeners;

import dev.siea.data.CountingSettings;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class CountingListener extends ListenerAdapter {
    private final CountingSettings settings;
    private final Logger logger;
    private ScheduledFuture<?> task;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public CountingListener(CountingSettings settings, Logger logger){
        this.settings = settings;
        this.logger = logger;
    }

    public void onMessageReceived(MessageReceivedEvent event){
        TextChannel channel = (TextChannel) event.getChannel();

        if (!event.getChannel().getId().equals(settings.countingChannelId())){
            return;
        };

        if (event.getAuthor().isBot()) return;

        String msg = event.getMessage().getContentRaw();

        int number;

        try {
            number = Integer.parseInt(msg);}
        catch (NumberFormatException e) {
            event.getMessage().delete().queue();
            return;
        }

        if (number < 1){
            event.getMessage().delete().queue();
            return;
        }

        event.getChannel().getHistory().retrievePast(2)
                .map(messages -> {
                    if (messages.isEmpty() || messages.size() < 2) {
                        return null;
                    }
                    return messages.get(1);
                })
                .queue(message -> {
                    if (message == null) {
                        if (number != 1) {
                            event.getMessage().delete().queue();
                            return;
                        }
                        scheduleCounting(channel, 2);
                        return;
                    }
                    String lastSender = message.getAuthor().getId();
                    int lastNumber = Integer.parseInt(message.getContentRaw());
                    if (lastNumber != number - 1 || (!settings.allowSameUserCount() && lastSender.equals(event.getAuthor().getId()))) {
                        event.getMessage().delete().queue();
                        return;
                    }
                    scheduleCounting(channel, number+1);
                });
    }

    private void scheduleCounting(TextChannel channel, int number){
        if (task != null){
            task.cancel(true);
        }
        startTask(channel, number);
    }

    private void startTask(TextChannel channel, int number){
        task = scheduler.schedule(() -> channel.sendMessage(String.valueOf(number)).queue(), settings.countAlongAfterSeconds(), TimeUnit.SECONDS);

    }
}
