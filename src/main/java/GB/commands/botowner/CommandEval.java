package GB.commands.botowner;

import GB.Handler;
import GB.core.Main;
import GB.commands.Command;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import static util.Constants.progBars;


public class CommandEval implements Command {
    String help() {
        return "jda = event.getJDA()\nguild = event.getGuild()\nchannel = event.getChannel()\nmessage = event.getMessage()\nauthor = event.getAuthor()\nshardmanager = Main.shardmanager";
    }
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (Owner.get(event.getAuthor())) {
            new Thread(() -> {
            ScriptEngineFactory scriptEngineFactory = new NashornScriptEngineFactory();
            ScriptEngine se = scriptEngineFactory.getScriptEngine();
            String ret = null;
            Throwable error = null;
            Throwable initError = null;
            try {
                se.eval("var imports = new JavaImporter(" +
                        "java.nio.file," +
                        "Packages.net.dv8tion.jda.core.Permission," +
                        "Packages.net.dv8tion.jda.core," +
                        "java.lang," +
                        "java.lang.management," +
                        "java.text," +
                        "java.sql," +
                        "java.util," +
                        "java.time," +
                        "Packages.com.sun.management" +
                        ");");
            } catch (Throwable e) {
                initError = e;
            }
            se.put("jda", event.getJDA());
            se.put("guild", event.getMessage().getGuild());
            se.put("channel", event.getMessage().getChannel());
            se.put("message", event.getMessage());
            se.put("author", event.getMessage().getAuthor());
            se.put("shardmanager", Main.shardManager);
            se.put("help", help());

            progBars.forEach(se::put);

            String input = event.getMessage().getContentRaw().replaceFirst(new Handler().getMessageHandler().getprefix(event.getGuild()), "").replaceFirst("eval", "").trim();
            try {
                if (input.equals("1+1")) {
                    ret = "1";
                } else {
                    ret = se.eval("{" +
                            "with (imports) {\n" +
                            "function complex(re, im){\n" +
                            "  return new Complex(re,im);\n" +
                            "};\n" +
                            "\n" +
                            "function thread() {\n" +
                            "  return Thread.currentThread();\n" +
                            "}\n" +
                            input +
                            "\n}\n" +
                            "}") + "";
                }
            } catch (Throwable e) {
                error = e;
            }
            EmbedBuilder eB = new EmbedBuilder()
                    .setTitle("Eval'd")
                    .setFooter(event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getEffectiveAvatarUrl())
                    .addField(":inbox_tray:Input", "```java\n" + input + "\n```", false);
            if (initError != null) {
                eB.addField(":x:Error! (During Init)", "```java\n" + initError + "\n```", false);
            }
            if (ret != null) {
                eB.addField(":outbox_tray:Output", "```java\n" + ret + "\n```", false);
            }
            if (error != null) {
                eB.addField(":x:Error!", "```java\n" + error + "\n```", false);
            }
            event.getMessage().getTextChannel().sendMessage(eB.build()).queue();

            }).start();

        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

}