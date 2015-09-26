package gg.uhc.specinfo.log.teleports;

import org.bukkit.entity.Player;

/**
 * Teleport to the player's current location
 */
public class PlayerTeleportClickable implements TeleportClickable {

    protected final String uuidString;
    protected final String name;

    public PlayerTeleportClickable(Player player) {
        this.uuidString = player.getUniqueId().toString();
        this.name = player.getName();
    }

    @Override
    public String getSubcommand() {
        return "player " + uuidString;
    }

    @Override
    public String getDisplayText() {
        return name;
    }

    @Override
    public String getHoverText() {
        return "Teleport to this player's current location";
    }
}
