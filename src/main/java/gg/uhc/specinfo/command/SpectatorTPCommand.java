package gg.uhc.specinfo.command;

import gg.uhc.specinfo.log.SpectatorsMessageLogger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.UUID;

public class SpectatorTPCommand implements CommandExecutor {

    protected static final String ONLY_PLAYER = ChatColor.RED + "This command can only be ran by a player";
    protected static final String SPEC_ONLY = ChatColor.RED + "This command is supposed to be ran by spectators only";
    protected static final String NOT_ONLINE = ChatColor.RED + "Player is not online";
    protected static final String INVALID_UUID = ChatColor.RED + "Invalid UUID, this command is not supposed to be ran manually";
    protected static final String INVALID_COMMAND = ChatColor.RED + "Invalid command, this command is not supposed to be ran manually";

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

        if (args.length != 2) {
            sender.sendMessage(INVALID_COMMAND);
            return true;
        }

        if (args[0].equals("player")) {
            try {
                UUID uuid = UUID.fromString(args[1]);

                Player to = Bukkit.getPlayer(uuid);

                if (to == null) {
                    sender.sendMessage(NOT_ONLINE);
                } else {
                    // dont send a message, SILENT
                    player.teleport(to, PlayerTeleportEvent.TeleportCause.SPECTATE);
                }
            } catch (IllegalArgumentException ex) {
                sender.sendMessage(INVALID_UUID);
            }

            return true;
        }

        if (args[0].equals("location")) {
            String[] parts = args[1].split(":");

            if (parts.length != 4) {
                sender.sendMessage(INVALID_COMMAND);
                return true;
            }

            World world = Bukkit.getWorld(parts[0]);

            if (world == null) {
                sender.sendMessage(INVALID_COMMAND);
                return true;
            }

            int x,y,z;
            try {
                x = Integer.parseInt(parts[1]);
                y = Integer.parseInt(parts[2]);
                z = Integer.parseInt(parts[3]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(INVALID_COMMAND);
                return true;
            }

            // no message, SILENT
            player.teleport(new Location(world, x + .5D, y + .5D, z + .5D));
            return true;
        }

        sender.sendMessage(INVALID_COMMAND);
        return true;
    }
}
