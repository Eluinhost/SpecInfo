package gg.uhc.specinfo.command;

import gg.uhc.specinfo.log.SpectatorsMessageLogger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectatorTPCommand implements CommandExecutor {

    protected static final String ONLY_PLAYER = ChatColor.RED + "This command can only be ran by a player";
    protected static final String SPEC_ONLY = ChatColor.RED + "This command is supposed to be ran by spectators only";
    protected static final String INVALID_TARGET = ChatColor.RED + "Player is not online";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ONLY_PLAYER);
            return true;
        }

        Player player = (Player) sender;

        if (player.getGameMode() != GameMode.SPECTATOR && !player.hasPermission(SpectatorsMessageLogger.SPECTATE_PERMISSION)) {
            player.sendMessage(SPEC_ONLY);
            return true;
        }

        if (args.length != 1) {
            return false;
        }

        Player to = Bukkit.getPlayerExact(args[0]);

        if (to == null) {
            player.sendMessage(INVALID_TARGET);
            return true;
        }

        player.teleport(to);
        return true;
    }
}
