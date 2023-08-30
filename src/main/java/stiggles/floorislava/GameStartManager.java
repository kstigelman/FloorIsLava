package stiggles.floorislava;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.HashMap;



public class GameStartManager {
    public static final int MAX_COUNTDOWN_TIME = 120;
    public static final int SHORTENED_COUNTDOWN = 10;

    private static int countdown = MAX_COUNTDOWN_TIME;
    private static boolean full = false;

    public static void everySecond () {
        if (Bukkit.getOnlinePlayers().size() <= 1) {
            Bukkit.getConsoleSender().sendMessage("Less than 1");
            cancelCountdown();
            return;
        }
        if (countdown == -1)
            return;


        if (countdown == 0) {
            if (RoundManager.getRoundId() == -1) {
                Bukkit.getConsoleSender().sendMessage("Start!");
                RoundManager.start();
                return;
            }
        }
        if (countdown % 30 == 0 || countdown <= 10) {
            PlayerManager.sendPlayersMessage(ChatColor.GRAY + "Game begins in " + countdown);
            PlayerManager.sendPlayersSound(Sound.UI_BUTTON_CLICK, 1);
        }

        if (countdown > -1)
            --countdown;

        Bukkit.getConsoleSender().sendMessage("Time: " + countdown);
    }


    public static void setCountdown (int time) {
        countdown = time;
    }
    public static int getCountdown () {
        return countdown;
    }
    public static void startCountdown () {
        countdown = 12;
    }
    public static void cancelCountdown () {
        countdown = -1;
    }

}
