package stiggles.floorislava;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the players playing the minigame.
 *
 * @author Kyler Stigelman
 */
public class PlayerManager {
    private static HashSet<Player> players = new HashSet<>();

    /**
     * Sends a message to all online players.
     *
     * @param msg The message to be sent
     */
    public static void sendOnlinePlayersMessage (String msg) {
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(msg);
    }

    /**
     * Plays a sound for all online players.
     *
     * @param sound The sound to be played
     * @param pitch The pitch of the sound to be played
     */
    public static void sendOnlinePlayersSound (Sound sound, float pitch) {
        for (Player p : players)
            p.playSound(p.getLocation(), sound, 1, pitch);
    }

    /**
     * Send a message only to players currently alive in the game.
     *
     * @param msg The message to be sent.
     */
    public static void sendPlayersMessage(String msg) {
        for (Player p : players)
            p.sendMessage(msg);
    }

    /**
     * Play a sound for only the players currently alive in the game.
     *
     * @param sound The sound to be played
     * @param pitch The pitch of the sound
     */
    public static void sendPlayersSound (Sound sound, float pitch) {
        for (Player p : players)
            p.playSound(p.getLocation(), sound, 1, pitch);
    }

    /**
     * Get the number of players still alive.
     *
     * @return The number of players
     */
    public static int getPlayerCount () {
        return players.size();
    }

    /**
     * Get the players alive in the game.
     *
     * @return The players
     */
    public static Set<Player> getPlayers () {
        return players;
    }

    /**
     * Add a new player to the game.
     *
     * @param player The player to be added
     */
    public static void addPlayer (Player player) {
        players.add (player);
    }

    /**
     * Remove a player from the game.
     *
     * @param player The player to be removed.
     */
    public static void removePlayer (Player player) {
        players.remove (player);
    }
}
