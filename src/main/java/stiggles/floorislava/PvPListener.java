package stiggles.floorislava;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvPListener implements Listener {

    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent e) {
        //Grace period
        if (RoundManager.getRoundId() != 0)
            return;
        if (e.getDamager() instanceof Player) {
            if (e.getEntity() instanceof Player) {
                e.setCancelled(true);
            }
        }
    }
}
