package rynodevelops.radiationimmersive;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogAppender extends AbstractAppender {

    public LogAppender() {
        // do your calculations here before starting to capture
        super("MyLogAppender", null, null);
        start();
    }

    @Override
    public void append(LogEvent event) {
        // if you don`t make it immutable, than you may have some unexpected behaviours
        LogEvent log = event.toImmutable();

        // do what you have to do with the log

        // you can get only the log message like this:
        String message = log.getMessage().getFormattedMessage();


        if (message.contains("DEBUG")
                && message.contains("Explode") && contieneNomePlayer(message) == false) {

            String world;
            double x, y, z;


            String rawString = message;

            String rawSubstring = rawString.substring(rawString.indexOf("at"));

            world = rawSubstring.substring(3, rawSubstring.indexOf(":"));

            Main.istance.getServer().getConsoleSender().sendMessage(rawSubstring);

            x = Double.parseDouble(rawSubstring.split(":")[1]);
            y = Double.parseDouble(rawSubstring.split(":")[2]);
            z = Double.parseDouble(rawSubstring.split(":")[3]);

            Main.istance.getServer().getConsoleSender().sendMessage(world + " " + x + " " + y + " " + z);
            Main.istance.getServer().getConsoleSender().sendMessage( " " + x + " " + y + " " + z);


            Main.istance.particleExplosion(x, y, z, world);

        }

    }

    public boolean contieneNomePlayer(String messageToTest) {

        for (Player player : Main.istance.getServer().getOnlinePlayers()) {
            String playername = player.getName();
            if (messageToTest.contains(playername)) {
                return true;
            }
        }

        return false;



    }



}
