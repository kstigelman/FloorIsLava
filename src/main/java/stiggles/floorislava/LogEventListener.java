package stiggles.floorislava;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LogEventListener implements Listener {
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        if (RoundManager.getRoundId() == -1) {
            e.getPlayer().setGameMode(GameMode.ADVENTURE);

            if (Bukkit.getOnlinePlayers().size() > 1 && GameStartManager.getCountdown() == -1)
                GameStartManager.startCountdown();
        }
        else {
            e.getPlayer().setGameMode(GameMode.SPECTATOR);
            e.setJoinMessage(ChatColor.GRAY + e.getPlayer().getName() + " is watching the game");
        }
    }
}
