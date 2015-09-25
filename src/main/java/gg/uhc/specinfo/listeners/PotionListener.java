package gg.uhc.specinfo.listeners;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;
import gg.uhc.specinfo.log.MessageLogger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import java.util.Collection;

public class PotionListener implements Listener {

    protected static final String DRANK_FORMAT = "%s drank a potion (%s)";
    protected static final String THROWN_FORMAT = "%s threw a potion (%s)";

    protected final MessageLogger sendTo;

    public PotionListener(MessageLogger sendTo) {
        this.sendTo = sendTo;
    }

    protected String stringifyEffects(Collection<PotionEffect> effects) {
        Collection<String> stringified = Collections2.transform(effects, new Function<PotionEffect, String>() {
            @Nullable
            @Override
            public String apply(PotionEffect effect) {
                return effect.getType().getName() + ":" + (effect.getAmplifier() + 1);
            }
        });

        return Joiner.on(" + ").join(stringified);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(PlayerItemConsumeEvent event) {
        if (event.getItem().getType() != Material.POTION) return;

        Collection<PotionEffect> effects = Potion.fromItemStack(event.getItem()).getEffects();

        Player player = event.getPlayer();

        sendTo.logFormattedMessage(player, DRANK_FORMAT, player.getName(), stringifyEffects(effects));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof ThrownPotion)) return;

        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        Collection<PotionEffect> effects = ((ThrownPotion) event.getEntity()).getEffects();

        sendTo.logFormattedMessage(player, THROWN_FORMAT, player.getName(), stringifyEffects(effects));
    }
}
