package gg.uhc.specinfo.log;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class MessageLogger {

    protected static final String PREFIX = ChatColor.GOLD + "S» " + ChatColor.DARK_GRAY;

    /**
     * Log a message
     *
     * @param related the player related for the TP command, if null no TP is added
     * @param message the message to send
     *
     * @see MessageLogger#logFormattedMessage(Player, String, Object...)
     */
    public void logMessage(Player related, String message) {
        log(related, PREFIX + message);
    }

    protected abstract void log(Player related, String message);

    /**
     * Log a message with formatting
     *
     * @param related the player related for the TP command, if null no TP is added
     * @param format the String.format format string
     * @param parameters the paramters to pass to String.format
     *
     * @see MessageLogger#logMessage(Player, String)
     * @see String#format(String, Object...)
     */
    public void logFormattedMessage(Player related, String format, Object... parameters) {
        logMessage(related, String.format(format, parameters));
    }
}
