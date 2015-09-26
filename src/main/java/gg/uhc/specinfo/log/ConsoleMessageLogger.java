package gg.uhc.specinfo.log;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import gg.uhc.specinfo.log.teleports.TeleportClickable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ConsoleMessageLogger extends MessageLogger {

    protected final CommandSender console = Bukkit.getConsoleSender();
    protected final ClickableToTextConverter converter = new ClickableToTextConverter();

    @Override
    public void logMessage(Player related, String message, TeleportClickable... teleports) {
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX);
        builder.append("[");
        builder.append(related.getName());
        builder.append("] ");
        builder.append(message);

        if (teleports.length > 0) {
            builder.append(" ");
            List<TeleportClickable> clicks = ImmutableList.copyOf(teleports);
            Joiner.on(" | ").appendTo(builder, Collections2.transform(clicks, converter));
        }

        console.sendMessage(builder.toString());
    }

    class ClickableToTextConverter implements Function<TeleportClickable, String> {
        @Override
        public String apply(TeleportClickable input) {
            return input.getDisplayText();
        }
    }
}
