package commands.botowner;

import commands.Command;
import core.MessageHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class CommandTest implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (Owner.get(event.getAuthor())) {
           event.getTextChannel().sendMessage(MessageHandler.getEmbed("botowner.test.title", "botowner.test.text", "", "normal", event)).queue();





        } else {
            event.getTextChannel().sendMessage(MessageHandler.getEmbed("util.error", "util.nopermissionuser", "", "error", event)).queue();
        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
    }
}
