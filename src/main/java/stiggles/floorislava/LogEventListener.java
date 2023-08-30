package stiggles.floorislava;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        if (RoundManager.getRoundId() == -1) {
            if (e.getPlayer().getGameMode().equals(GameMode.SPECTATOR))
                e.getPlayer().setGameMode(GameMode.SURVIVAL);

            if (Bukkit.getOnlinePlayers().size() > 1 && GameStartManager.getCountdown() == -1)
                GameStartManager.startCountdown();
        }
    }
}