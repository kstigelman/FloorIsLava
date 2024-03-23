package stiggles.floorislava;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Tracks the use cooldown for the Grappling Hook.
 *
 * @author Kyler Stigelman
 */
public class Cooldown {
    public static HashMap<UUID, Double> cooldown = new HashMap<>();

    public static void setupCooldown() {
        cooldown = new HashMap<>();
    }

    /**
     * Set the cooldown for a player's grappling hook.
     *
     * @param player The player to set the cooldown for
     * @param seconds The length of the cooldown.
     */
    public static void setCooldown(Player player, int seconds) {
        // Calculate the time in which the item may be used
        double delay = System.currentTimeMillis() + (seconds * 1000L);
        cooldown.put(player.getUniqueId(), delay);
    }

    /**
     * Check if a player's grappling hook is currently on cooldown.
     *
     * @param player The player to check
     * @return Whether or not their cooldown is still active
     */
    public static boolean checkCooldown(Player player) {
        return !cooldown.containsKey(player.getUniqueId()) || cooldown.get(player.getUniqueId()) <= System.currentTimeMillis();
    }
}
