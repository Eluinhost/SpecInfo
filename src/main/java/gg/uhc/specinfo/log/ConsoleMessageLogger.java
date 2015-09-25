package gg.uhc.specinfo.log;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConsoleMessageLogger extends MessageLogger {

    protected final CommandSender console = Bukkit.getConsoleSender();

    @Override
    protected void log(Player related, String message) {
        // ignore the related player as console cannot teleport
        console.sendMessage(message);
    }
}
