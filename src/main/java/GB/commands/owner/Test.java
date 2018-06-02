package GB.commands.owner;

import GB.GottBot;
import GB.Handler.CommandHandling.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Test implements Command {
    @Override
    public String[] Aliases() {
        return new String[]{"test", "t"};
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(GottBot.getMessage().getEmbed("commands.owner.test", "commands.owner.test", Color.GREEN, event.getAuthor(), event.getGuild())).queue();
    }
}
