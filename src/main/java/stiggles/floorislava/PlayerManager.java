package stiggles.floorislava;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PlayerManager {
    public static final int MAX_PLAYER_COUNT = 8;

    private static HashSet<Player> players = new HashSet<>();
    private static boolean full = false;


    public static void sendOnlinePlayersMessage (String msg) {
        for (Player p : Bukkit.getOnlinePlayers())
            p.sendMessage(msg);
    }
    public static void sendOnlinePlayersSound (Sound sound, float pitch) {
        for (Player p : players)
            p.playSound(p.getLocation(), sound, 1, pitch);
    }
    public static void sendPlayersMessage(String msg) {
        for (Player p : players)
            p.sendMessage(msg);
    }
    public static void sendPlayersSound (Sound sound, float pitch) {
        for (Player p : players)
            p.playSound(p.getLocation(), sound, 1, pitch);
    }
    public static int getPlayerCount () {
        return players.size();
    }
    public static Set<Player> getPlayers () {
        return players;
    }

    public static void addPlayer (Player player) {
        players.add (player);
    }
    public static void removePlayer (Player player) {
        players.remove (player);
    }

    /*
    public static boolean join (Player player) {
        if (full || GameStartManager.getCountdown() <= 0)
            return false;

        players.put (player, true);
        player.setGameMode(GameMode.SURVIVAL);

        if (players.size() == MAX_PLAYER_COUNT) {
            full = true;
            if (GameStartManager.getCountdown() > GameStartManager.SHORTENED_COUNTDOWN)
                GameStartManager.setCountdown(11);
        }

        return true;
    }
    public static boolean leave (Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        players.remove (player);

        if (GameStartManager.getCountdown() < GameStartManager.SHORTENED_COUNTDOWN && players.size() <= 0.75 * MAX_PLAYER_COUNT)
            GameStartManager.setCountdown(GameStartManager.MAX_COUNTDOWN_TIME);

        return true;
    }
*/
    public static boolean isFull () {
        return full;
    }
}