package gg.uhc.specinfo.log.extras;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * Teleport to the player's current location
 */
public class PlayerTeleportClickable extends TeleportClickable {

    protected static final BaseComponent[] HOVER = new BaseComponent[]{new TextComponent("Teleport to this player's current location")};

    protected final String name;
    protected final String command;

    public PlayerTeleportClickable(Player player) {
        this.command = "player " + player.getUniqueId();
        this.name = player.getName();

    }

    @Override
    public String getSubcommand() {
        return command;
    }

    @Override
    public BaseComponent[] getHoverText() {
        return HOVER;
    }

    @Override
    public String getRawText() {
        return name;
    }
}
