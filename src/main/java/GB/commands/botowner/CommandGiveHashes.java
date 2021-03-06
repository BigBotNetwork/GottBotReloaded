package GB.commands.botowner;

import GB.Handler;
import GB.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandGiveHashes implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (Owner.get(event.getAuthor())) {
            if (event.getMessage().getMentionedUsers().size()==1) {
                long hashes = Long.parseLong(new Handler().getMySQL().get("user", "id", event.getMessage().getMentionedUsers().get(0).getId(), "hashes"))+Long.parseLong(args[0]);
                new Handler().getMySQL().update("user", "hashes", String.valueOf(hashes), "id", event.getMessage().getMentionedUsers().get(0).getId());
                event.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Sucess!").setDescription("Sucessfully add "+event.getMessage().getMentionedUsers().get(0).getName()+" "+args[0]+" hashes").build()).queue();
            }
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

}
