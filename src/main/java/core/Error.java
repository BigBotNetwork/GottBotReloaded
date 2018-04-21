package core;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import org.apache.commons.net.ftp.FTPClient;
import stuff.DATA;
import stuff.SECRETS;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class Error {

    public static void handle(Exception e) {
        try {
            String error = Main.geterrorString(e);
            String substring="";
            String filename = "Error-"+new Date().toGMTString().replaceAll(" ", "-").replaceAll(":", "-")+".txt";
            substring += "An error gönn dir: https://bigbotnetwork.de/errors/"+filename;
            new File(filename).createNewFile();
            PrintWriter pWriter = null;
            try {
                pWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
                pWriter.print(error);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                if (pWriter != null){
                    pWriter.flush();
                    pWriter.close();
                }
            }
            FTPClient client = new FTPClient();
            FileInputStream fis = null;
            client.connect("ftp.bigbotnetwork.de");
            client.login(SECRETS.FTPUSER, SECRETS.FTPPW);
            fis = new FileInputStream(filename);
            client.storeFile("httpdocs/errors/"+filename, fis);
            client.logout();
            System.out.println(substring);
            Main.shardManager.getGuildById(DATA.BBNS).getTextChannelById(DATA.BBNLOG).sendMessage(new EmbedBuilder().setTitle(":warning: Error :warning:").setDescription("<@401817301919465482> <@261083609148948488> A ERROR: "+substring).build()).queue();
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

}
