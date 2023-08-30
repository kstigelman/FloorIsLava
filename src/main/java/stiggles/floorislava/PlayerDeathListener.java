package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent e) {
        PlayerManager.removePlayer(e.getEntity());
        e.getEntity().setGameMode(GameMode.SPECTATOR);
        e.getEntity().sendTitle(ChatColor.BOLD + ChatColor.RED.toString() + "YOU DIED!", null, 0, 80, 20);

        if (PlayerManager.getPlayerCount() <= 1) {
            RoundManager.end();
        }
    }
}
