package gg.uhc.specinfo.log;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import gg.uhc.specinfo.log.extras.LogExtra;
import org.bukkit.entity.Player;

import java.util.List;

public class MultiMessageLogger extends MessageLogger {

    protected final List<MessageLogger> wrapped;

    public MultiMessageLogger(List<MessageLogger> initial) {
        this.wrapped = initial;
    }

    public MultiMessageLogger(MessageLogger... loggers) {
        this(Lists.newArrayList(loggers));
    }

    public void addLogger(MessageLogger logger) {
        this.wrapped.add(logger);
    }

    public void removeLogger(MessageLogger logger) {
        this.wrapped.remove(logger);
    }

    public List<MessageLogger> getSenders() {
        return ImmutableList.copyOf(wrapped);
    }

    @Override
    public void logMessage(Player related, String message, LogExtra... extras) {
        for (MessageLogger sender : wrapped) {
            sender.logMessage(related, message, extras);
        }
    }
}
