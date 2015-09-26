package gg.uhc.specinfo.log;

import gg.uhc.specinfo.log.extras.LogExtra;
import gg.uhc.specinfo.log.extras.PlayerTeleportClickable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;


public class SpectatorsMessageLogger extends MessageLogger {

    public static final String SPECTATE_PERMISSION = "uhc.specinfo.spectate";

    @Override
    public void logMessage(Player related, String message, LogExtra... extras) {
        BaseComponent component = new TextComponent(PREFIX);
        component.setColor(ChatColor.DARK_GRAY);

        // add related player
        if (related != null) {
            TextComponent relatedPlayer = (TextComponent) new PlayerTeleportClickable(related).getChatComponent();
            relatedPlayer.setText("[" + relatedPlayer.getText() + "] ");
            component.addExtra(relatedPlayer);
        }

        // add message
        component.addExtra(message);

        // add extra info
        for (LogExtra extra : extras) {
            component.addExtra(" | ");
            component.addExtra(extra.getChatComponent());
        }

        // send the message
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getGameMode() == GameMode.SPECTATOR || player.hasPermission(SPECTATE_PERMISSION)) {
                player.spigot().sendMessage(component);
            }
        }
    }
}
