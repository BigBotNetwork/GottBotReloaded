package listener;

import core.MySQL;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import core.commandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class commandListener extends ListenerAdapter {
    public static String beheaded;
    private static Logger logger = LoggerFactory.getLogger(commandListener.class);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        try {
            if (event.getChannelType().equals(ChannelType.TEXT)) {
                if (!event.getAuthor().isBot()) {
                    System.out.println(event.getMessage().getContentRaw());
                    System.out.println(event.getJDA().getSelfUser().getAsMention());
                    String PREFIX = MySQL.get("server", "ID", event.getGuild().getId(), "prefix");
                    if (event.getMessage().getContentRaw().replace("!", "").startsWith(event.getJDA().getSelfUser().getAsMention())) {
                        PREFIX=event.getJDA().getSelfUser().getAsMention();
                    }
                        if (event.getMessage().getContentRaw().startsWith(PREFIX)) {
                            beheaded = event.getMessage().getContentRaw().toLowerCase().replaceFirst(Pattern.quote(PREFIX), "");
                            commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContentRaw().toLowerCase(), event));
                            logger.info(event.getAuthor().getName() + " mit ID " + event.getAuthor().getId() + " auf " + event.getGuild().getName() + " hat den Command genutzt: " + event.getMessage().getContentRaw());
                        }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}