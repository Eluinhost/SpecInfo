package gg.uhc.specinfo.log;

import gg.uhc.specinfo.log.teleports.PlayerTeleportClickable;
import gg.uhc.specinfo.log.teleports.TeleportClickable;
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

    @Override
    public void logMessage(Player related, String message, TeleportClickable... teleports) {
        BaseComponent component = new TextComponent(PREFIX);
        component.setColor(ChatColor.DARK_GRAY);

        // add related player
        if (related != null) {
            TextComponent relatedPlayer = getTrigger(new PlayerTeleportClickable(related));
            relatedPlayer.setText("[" + relatedPlayer.getText() + "] ");
            component.addExtra(relatedPlayer);
        }

        // add message
        component.addExtra(message);

        // add extra clickables
        for (TeleportClickable clickable : teleports) {
            component.addExtra(" | ");
            component.addExtra(getTrigger(clickable));
        }

        // send the message
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR || player.hasPermission(SPECTATE_PERMISSION)) {
                player.spigot().sendMessage(component);
            }
        }
    }

    protected TextComponent getTrigger(TeleportClickable clickable) {
        TextComponent trigger = new TextComponent(clickable.getDisplayText());
        trigger.setColor(ChatColor.GRAY);
        trigger.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(clickable.getHoverText())}));
        trigger.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/specinfo:silenttp "  + clickable.getSubcommand()));
        return trigger;
    }
}
