package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Listener checks for build-related events such as blocks place and blocks broken and checks that the locations are
 *   valid for the game's play area.
 */
public class BuildListener implements Listener {
    @EventHandler
    public void onBlockPlace (BlockPlaceEvent e) {
        if (e.getBlock().getLocation().getBlockY() > Main.MAX_HEIGHT) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "Build height limited to " + Main.MAX_HEIGHT);
        }

        if (RoundManager.getRoundId () == -1)
            if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                e.setCancelled(true);
    }
    @EventHandler
    public void onBlockBreak (BlockBreakEvent e) {
        if (e.getBlock().getLocation().getBlockY() > Main.MAX_HEIGHT) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "Build height limited to " + Main.MAX_HEIGHT);
        }

        if (RoundManager.getRoundId () == -1)
            if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
                e.setCancelled(true);
    }

    // Player interact event handles water bucket placement, which is a blacklisted item in this game
    @EventHandler
    public void onPlayerInteract (PlayerInteractEvent e) {
        if (e.getItem() == null)
            return;

        if (e.getItem().getType().equals(Material.WATER_BUCKET)) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.GRAY + "Water is not allowed in this game!");
        }
    }
}
