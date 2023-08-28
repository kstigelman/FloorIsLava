package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BuildListener implements Listener {
    private final int MAX_BUILD_LIMIT = 120;

    @EventHandler
    public void onBlockPlace (BlockPlaceEvent e) {
        if (e.getBlock().getLocation().getBlockY() > MAX_BUILD_LIMIT) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "Build height limited to " + MAX_BUILD_LIMIT);
        }

        if (RoundManager.getRoundId () == -1)
            if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        if (e.getBlock().getLocation().getBlockY() > MAX_BUILD_LIMIT) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "Build height limited to " + MAX_BUILD_LIMIT);
        }

        if (RoundManager.getRoundId () == -1)
            if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                e.setCancelled(true);
    }
}
