package gg.uhc.specinfo.listeners;

import gg.uhc.specinfo.log.MessageLogger;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;

public class DamageTakenListener implements Listener {

    protected static final String LOG_FORMAT = "Took %.2f damage from %s";

    protected final MessageLogger sendTo;

    public DamageTakenListener(MessageLogger sendTo) {
        this.sendTo = sendTo;
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

        sendTo.logMessage(damaged, String.format(LOG_FORMAT, event.getFinalDamage(), causeString));
    }

    protected String getNameOfEntity(Entity entity) {
        if (entity instanceof Player) {
            return entity.getName();
        } else {
            return entity.getType().name();
        }
    }
}
