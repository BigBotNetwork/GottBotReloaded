package commands.botowner;

import commands.Command;
import core.MySQL;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Embed;

public class CommandRegisterServer implements Command {
    private static Logger logger = LoggerFactory.getLogger(CommandRegisterServer.class);
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (Owner.get(event.getAuthor())) {
            int i = 0;
            while (event.getJDA().getGuilds().size()-1>=i) {
                if (MySQL.get("server", "id", event.getJDA().getGuilds().get(i).getId(), "id")==null) {
                    MySQL.insert("server", "id", event.getJDA().getGuilds().get(i).getId()+"");
                    MySQL.insert("log", "serverid", event.getJDA().getGuilds().get(i).getId() + "");
                    logger.info("neuer Server in database Name: " + event.getJDA().getGuilds().get(i).getName() + " ID: " + event.getJDA().getGuilds().get(i).getId());
                    event.getTextChannel().sendMessage(Embed.success("Registered", "Succesfully registered server!").build()).queue();
                }
                i++;
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

}
