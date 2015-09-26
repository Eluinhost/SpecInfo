package gg.uhc.specinfo.log;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import gg.uhc.specinfo.log.extras.LogExtra;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ConsoleMessageLogger extends MessageLogger {

    protected final CommandSender console = Bukkit.getConsoleSender();
    protected final ClickableToTextConverter converter = new ClickableToTextConverter();

    @Override
    public void logMessage(Player related, String message, LogExtra... extras) {
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX);
        builder.append("[");
        builder.append(related.getName());
        builder.append("] ");
        builder.append(message);

        if (extras.length > 0) {
            builder.append(" ");
            List<LogExtra> ext = ImmutableList.copyOf(extras);
            Joiner.on(" | ").appendTo(builder, Collections2.transform(ext, converter));
        }

        console.sendMessage(builder.toString());
    }

    class ClickableToTextConverter implements Function<LogExtra, String> {
        @Override
        public String apply(LogExtra input) {
            return input.getRawText();
        }
    }
}
