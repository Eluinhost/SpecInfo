package gg.uhc.specinfo.command;

import gg.uhc.specinfo.listeners.veins.VeinBreakListener;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpecInfoCommand implements CommandExecutor {

    protected final VeinBreakListener veinBreakListener;

    public SpecInfoCommand(VeinBreakListener veinBreakListener) {
        this.veinBreakListener = veinBreakListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (args[0].equalsIgnoreCase("clear")) {
            veinBreakListener.clearTracking();

            sender.sendMessage(ChatColor.AQUA + "Cleared stored information");
            return true;
        }

        return false;
    }
}
