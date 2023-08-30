package stiggles.floorislava;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {
    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent e) {
        //Grace period
        if (RoundManager.getRoundId() > 0)
            return;
        if (e.getEntity() instanceof Player) {
            if (e.getDamager() instanceof Player) {
                e.setCancelled(true);
            }
            if (((Player) e.getEntity()).getHealth() == 0) {
                e.setCancelled(true);
                ((Player) e.getEntity()).setHealth(1);
            }

        }
    }
}