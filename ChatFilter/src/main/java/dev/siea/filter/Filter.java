package dev.siea.filter;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public interface Filter {
    void onMessageReceived(@NotNull MessageReceivedEvent var1);
}
