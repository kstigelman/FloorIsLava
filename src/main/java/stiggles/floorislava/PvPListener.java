package stiggles.floorislava;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Listens for player damage event. This event should be cancelled during pre-game and during grace period.
 *
 * @author Kyler Stigelman
 */
public class PvPListener implements Listener {
    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent e) {
        if (RoundManager.getRoundId() > 0)
            return;
        //Grace period. Cancel damage event if damage is between two players.
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
