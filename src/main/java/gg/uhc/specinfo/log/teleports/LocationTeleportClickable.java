package gg.uhc.specinfo.log.teleports;

import org.bukkit.Location;

public class LocationTeleportClickable implements TeleportClickable {

    protected static final String COORD_FORMAT = "%s:%d:%d:%d";

    protected final Location location;
    protected final String coords;

    public LocationTeleportClickable(Location location) {
        this.location = location;
        this.coords = String.format(COORD_FORMAT, location.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public String getSubcommand() {
        return "location " + coords;
    }

    @Override
    public String getDisplayText() {
        return coords;
    }

    @Override
    public String getHoverText() {
        return "Teleport to this location";
    }
}
