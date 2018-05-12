package GB;

import GB.Handler.CommandHandling.commandHandler;
import GB.Handler.CommandHandling.commandListener;
import GB.Handler.DB;
import GB.Handler.Info;
import GB.commands.admin.CommandPrefix;
import GB.commands.owner.CommandGuilds;
import GB.commands.owner.CommandTest;
import GB.commands.user.CommandPing;
import GB.commands.user.CommandStats;
import GB.listener.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.rethinkdb.net.Cursor;
import net.dv8tion.jda.bot.sharding.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.utils.SessionController;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.Collection;

import GB.Handler.Config;

public class GottBot {
    private static DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
    public static ShardManager shardManager;
    private static SessionController sessionController;
    private static Config config;
    private static Collection<Integer> shards;
    private static JDA Oneshardbot;

    public static void main(String[] args) {
        Startall();
    }

    private static void Startall() {
        read();
        DB.Connect();
        getInfos();
        listentochanges();
        startBot();
        startBot2();
    }

    private static void getInfos() {
        System.out.println("Max Shards: "+getInfo().getMaxShards());
        System.out.println("Total Shards: "+getInfo().getTotalShards());
        System.out.println("StartShards: "+getInfo().getstartShards());
        System.out.println("Online Shards: "+getInfo().getShards());
    }

    private static void listentochanges() {
        Thread t = new Thread(() -> {
            Cursor cursor = getDB().getR().table("requests").changes().run(getDB().getConn());
            for (Object doc : cursor) {
                System.out.println(doc);
                requestHandler(doc.toString());
            }
        });
        t.start();
    }

    private static void requestHandler(String request) {
        if (request.startsWith("{new_val={Request=Stop,")) {
            System.out.println(getDB().removeShards());
            System.out.println("Stopping Shards because of the database request....");
            System.exit(1);
        }
    }

    private static void startBot() {
            registerListener();
            registerCommands();
            shards=getInfo().getstartShards();
            builder.setShardsTotal(Integer.parseInt(getInfo().getMaxShards()));
            builder.setShards(shards);
            builder.setToken(getConfig().getToken());
            builder.setSessionController(sessionController);
            builder.setAutoReconnect(true);
            builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
            builder.setGame(Game.playing("Starting"));
            writeShards();
            try {
                shardManager = builder.build();
            } catch (LoginException e) {
                e.printStackTrace();
            }
    }
    private static void startBot2() {
        JDABuilder builder = new JDABuilder(AccountType.BOT).setToken(getConfig().getToken()).setAutoReconnect(true);
        try {
            Oneshardbot=builder.buildAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeShards() {
        getDB().updateShards();
    }

    private static void read() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            config=mapper.readValue(new File("config.yml"), Config.class);
            System.out.println(ReflectionToStringBuilder.toString(config, ToStringStyle.MULTI_LINE_STYLE));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void registerListener() {
        builder.addEventListeners(
          new commandListener(),
                new Message()
        );
    }

    private static void registerCommands() {
        commandHandler.commands.put("test", new CommandTest());
        commandHandler.commands.put("guilds", new CommandGuilds());
        commandHandler.commands.put("prefix", new CommandPrefix());
        commandHandler.commands.put("ping", new CommandPing());
        commandHandler.commands.put("stats", new CommandStats());
    }

    public static Config getConfig() {
        return config;
    }

    public static DB getDB() {
        return new DB();
    }

    public static Info getInfo() {
        return new Info();
    }

    public static JDA getOneshardbot() {
        return Oneshardbot;
    }

}
