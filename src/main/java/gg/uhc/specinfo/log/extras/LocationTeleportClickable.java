package gg.uhc.specinfo.log.extras;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;

public class LocationTeleportClickable extends TeleportClickable {

    protected static final BaseComponent[] HOVER = new BaseComponent[]{new TextComponent("Teleport to this location")};
    protected static final String COORD_FORMAT = "%s:%d:%d:%d";

    protected final String command;
    protected final String coords;

    public LocationTeleportClickable(Location location) {
        this.coords = String.format(COORD_FORMAT, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        this.command = "location " + coords;
    }

    @Override
    public String getSubcommand() {
        return "location " + coords;
    }

    @Override
    public BaseComponent[] getHoverText() {
        return HOVER;
    }

    @Override
    public String getRawText() {
        return coords;
    }
}
