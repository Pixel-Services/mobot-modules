package dev.siea.logger;

import dev.siea.message.BetterEmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class CustomAppender extends AppenderBase<ILoggingEvent> {
    private final List<TextChannel> channels = new ArrayList<>();

    public CustomAppender() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        this.setContext(loggerContext);
        this.start();
        loggerContext.getLoggerList().forEach(logger -> logger.addAppender(this));
    }

    public void addChannel(TextChannel channel) {
        channels.add(channel);
    }

    @Override
    protected void append(ILoggingEvent event) {
        for (TextChannel channel : channels) {
            channel.sendMessageEmbeds(BetterEmbedBuilder.generateEmbed(event.getFormattedMessage())).queue();
        }
    }
}