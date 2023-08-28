package stiggles.floorislava;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent e) {
        if (RoundManager.getRoundId() == -1) {
            if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
                Location from = e.getFrom();
                Location to = e.getTo();

                if (from.getX() != to.getX())
                    e.setCancelled(true);
                else if (from.getY() != to.getY())
                    e.setCancelled(true);
                else if (from.getZ() != to.getZ())
                    e.setCancelled(true);
            }
        }
    }
}
