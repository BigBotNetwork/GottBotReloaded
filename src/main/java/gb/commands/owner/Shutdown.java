package gb.commands.owner;

import gb.Handler.CommandHandling.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Shutdown implements Command {
    @Override
    public String[] Aliases() {
        return new String[]{"close", "ShutdownListener","exit","stop"};
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        event.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Shutdown!").setDescription("Stopping Shard "+event.getJDA().getShardInfo().getShardId()).build()).queue();
        event.getJDA().shutdown();
    }
}
