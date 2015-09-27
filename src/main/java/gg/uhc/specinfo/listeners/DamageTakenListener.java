package gg.uhc.specinfo.listeners;

import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import gg.uhc.specinfo.log.MessageLogger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.text.NumberFormat;

public class DamageTakenListener implements Listener {

    protected static final String LOG_FORMAT = "%s damage from %s %s->%s";

    protected final RangeMap<Double, ChatColor> healths;
    protected final MessageLogger sendTo;
    protected final NumberFormat format;

    public DamageTakenListener(MessageLogger sendTo) {
        this.sendTo = sendTo;
        this.format = NumberFormat.getNumberInstance();
        this.format.setMaximumFractionDigits(2);
        this.format.setMinimumFractionDigits(0);

        double dead = 0D;
        double full = 100D;
        double lowerSplit = full / 3D;
        double higherSplit = lowerSplit * 2D;

        healths = ImmutableRangeMap.<Double, ChatColor>builder()
                .put(Range.closed(dead, lowerSplit), ChatColor.RED)
                .put(Range.open(lowerSplit, higherSplit), ChatColor.GOLD)
                .put(Range.closed(higherSplit, full), ChatColor.GREEN)
                .build();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void on(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player damaged = (Player) event.getEntity();

        EntityDamageEvent.DamageCause cause = event.getCause();

        String causeString = cause.toString();

        if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent dbdee = (EntityDamageByEntityEvent) event;

            Entity entity = dbdee.getDamager();

            String source;
            if (entity instanceof Projectile) {
                ProjectileSource projectileSource = ((Projectile) entity).getShooter();

                if (projectileSource instanceof Entity) {
                    source = getNameOfEntity((Entity) projectileSource);
                } else {
                    source = "Block";
                }
            } else {
                source = getNameOfEntity(entity);
            }

            causeString += " (" + source + ")";
        }

        double max = damaged.getMaxHealth();
        double damage = event.getFinalDamage();
        double previous = damaged.getHealth();
        double after = Math.max(0, previous - damage);

        sendTo.logMessage(damaged,
                String.format(
                        LOG_FORMAT,
                        this.format.format(damage),
                        causeString,
                        healths.get(previous / max * 100D) + this.format.format(previous),
                        healths.get(after / max  * 100) + this.format.format(after)
                )
        );
    }

    protected String getNameOfEntity(Entity entity) {
        if (entity instanceof Player) {
            return entity.getName();
        } else {
            return entity.getType().name();
        }
    }
}
