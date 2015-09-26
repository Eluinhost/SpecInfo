package gg.uhc.specinfo.log;

import gg.uhc.specinfo.log.extras.LogExtra;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class MessageLogger {

    protected static final String PREFIX = ChatColor.GOLD + "S» " + ChatColor.DARK_GRAY;

    /**
     * Log a message
     *
     * @param related the player related for the TP command, if null no TP is added
     * @param message the message to send
     * @param extras additional information to append
     */
    public abstract void logMessage(Player related, String message, LogExtra... extras);
}
