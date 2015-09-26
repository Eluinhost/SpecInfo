package gg.uhc.specinfo.log;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;


public class SpectatorsMessageLogger extends MessageLogger {

    public static final String SPECTATE_PERMISSION = "uhc.specinfo.spectate";

    protected final BaseComponent tp;

    public SpectatorsMessageLogger() {
        tp = new TextComponent(" TP");
        tp.setBold(true);
        tp.setColor(ChatColor.GRAY);
        tp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent("Teleport to associated player")}));
    }

    protected void log(Player related, String message) {
        TextComponent component = new TextComponent(message);

        if (related != null) {
            BaseComponent teleport = tp.duplicate();
            teleport.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/specinfo:silenttp " + related.getName()));

            component.addExtra(teleport);
        }

        // log to all online in specatator mode or permission
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR || player.hasPermission(SPECTATE_PERMISSION)) {
                player.spigot().sendMessage(component);
            }
        }
    }
}
