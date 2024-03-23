package stiggles.floorislava;

import org.bukkit.*;

/**
 * Runs all processes relating to the pre-game period. Begins and updates a countdown to the game starting.
 *   Once the countdown reaches 0, control is given to RoundManager.
 */
public class GameStartManager {
    public static final int MAX_COUNTDOWN_TIME = 120;
    public static final int SHORTENED_COUNTDOWN = 10;

    private static int countdown = MAX_COUNTDOWN_TIME;
    private static boolean full = false;

    public static void everySecond () {
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            cancelCountdown();
            return;
        }
        if (countdown == -1)
            return;

        if (countdown == 0) {
            if (RoundManager.getRoundId() == -1) {
                RoundManager.start();
                return;
            }
        }
        if (countdown % 30 == 0 || countdown <= 10) {
            PlayerManager.sendOnlinePlayersMessage(ChatColor.GRAY + "Game begins in " + countdown);
            PlayerManager.sendOnlinePlayersSound(Sound.UI_BUTTON_CLICK, 1);
        }

        if (countdown > -1)
            --countdown;
    }

    public static void setCountdown (int time) {
        countdown = time;
    }
    public static int getCountdown () {
        return countdown;
    }
    public static void startCountdown () {
        countdown = 120;
    }
    public static void cancelCountdown () {
        countdown = -1;
    }

}
