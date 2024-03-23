package stiggles.floorislava;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import stiggles.floorislava.Cuboids.Cuboid;

import java.util.concurrent.TimeUnit;

/**
 * Handles logic for the game phases/rounds. There is a five minute grace period, then lava begins to rise.
 *
 * @author Kyler Stigelman
 */
public class RoundManager {
    private static int roundId = -1;

    private static int timer = -1;
    private static int targetTime = 0;

    private static int currentLevel = -1;
    private static int nextLevel = 0;
    private static int taskId = -1;

    private static BossBar levelBossBar;

    public static void everySecond () {
        Bukkit.getConsoleSender().sendMessage("RoundID: " + roundId + " time: " + timer);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().getBlockY() < RoundManager.getCurrentLevel() - 3) {
                p.damage(1.f);
                p.sendMessage(ChatColor.RED + "The heat is unbearable! Get above the lava!");
            }
        }

        if (roundId == 0) {
            if (timer == 0) {
                ++roundId;
                PlayerManager.sendPlayersMessage(ChatColor.RED + "The grace period has ended!");
                PlayerManager.sendPlayersSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1);
                setNextLevel(60, 120);
            }
            else if (timer % 60 == 0) {
                PlayerManager.sendPlayersMessage(ChatColor.GREEN + "There are " + timer / 60 + " minutes remaining in the grace period.");
            }
        }
        if (roundId == 1) {
            if (timer == 0 || currentLevel == nextLevel) {
                ++roundId;
                Main.getWorld().getWorldBorder().setSize(1, TimeUnit.MINUTES, 10);
                setNextLevel(80, 120);
                return;
            }
            if (timer % (targetTime / nextLevel) == 0)
                nextLevel();
        }
        if (roundId == 2) {
            if (timer == 0 || currentLevel == nextLevel) {
                ++roundId;
                setNextLevel(196, 360);
                return;
            }
            if (timer % (targetTime / nextLevel) == 0)
                nextLevel();
        }
        if (roundId > 2) {
            if (currentLevel == nextLevel)
                return;
            if (timer % (targetTime / nextLevel) == 0)
                nextLevel();
        }
        if (timer > -1)
            --timer;
        if (roundId >= 2) {
            if (timer % 5 == 0) {
                for (Player p : PlayerManager.getPlayers())
                    p.getInventory().addItem(new ItemStack(Material.DIRT));
            }
            if (timer % 10 == 0) {
                for (Player p : PlayerManager.getPlayers())
                    p.getInventory().addItem(new ItemStack(Material.COBBLESTONE));
            }

            if (timer % 15 == 0) {
                for (Player p : PlayerManager.getPlayers())
                    p.getInventory().addItem(new ItemStack(Material.ARROW));
            }
        }
    }

    /**
     * Begin the game! Update world settings and give items to players.
     */
    public static void start () {
        roundId = 0;
        timer = 300;

        // Change world gamemodes
        Main.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        Main.getWorld().setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        Main.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        Main.getWorld().setGameRule(GameRule.DO_INSOMNIA, false);

        GrapplingHook gh = new GrapplingHook();

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GREEN + "The game has begun. Good luck...");
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 0);

            if (!player.getGameMode().equals(GameMode.ADVENTURE))
                return;

            player.setGameMode(GameMode.SURVIVAL);
            PlayerManager.addPlayer (player);


            player.getInventory().clear();
            player.setFoodLevel(20);
            player.setSaturation(10.0F);
            player.getWorld().setTime(0);

            if (!Main.isHardcore()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, timer * 20, 4, false, false));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, timer * 20, 4, false, false));

                player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));

                player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
                player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
                player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));

                player.getInventory().addItem(gh.getHook());
                player.getInventory().addItem(new ItemStack(Material.APPLE, 4));

                player.getInventory().addItem(new ItemStack(Material.BOW));
                player.getInventory().addItem(new ItemStack(Material.ARROW, 4));
            }
        }
        showLevelBossBar();

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), RoundManager::everySecond, 0, 20);

    }

    /**
     * The game has ended. Show according endgame titles to players.
     */
    public static void end () {
        hideLevelBossBar();
        cancelTask();

        if (PlayerManager.getPlayerCount() == 0) {
            //Tie!
            for (Player player : Bukkit.getOnlinePlayers())
                player.sendTitle(ChatColor.BOLD + ChatColor.WHITE.toString() + "TIE!", null, 0, 80, 20);
            return;
        }
        //1 Winner otherwise
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerManager.getPlayers().contains(player))
                player.sendTitle(ChatColor.GREEN + ChatColor.BOLD.toString() + "YOU WIN!", null, 0, 80, 20);
            else
                player.sendTitle(ChatColor.RED + ChatColor.BOLD.toString() + "YOU LOST!", ChatColor.GRAY + "Better luck next time...", 0, 80, 20);
        }

    }

    /**
     * Advance the lava to the next level.
     */
    public static void nextLevel () {
        if (currentLevel > nextLevel)
            return;

        ++currentLevel;
        if (currentLevel % 10 == 0)
            PlayerManager.sendPlayersMessage(ChatColor.RED + "The lava is currently at y=" + currentLevel);

        updateLevelBossBar();

        Main.setPlayArea(Main.getPlayArea().shift(Cuboid.CuboidDirection.Up, 1));

        // Fill the new bottom layer with lava
        for (Block b : Main.getPlayArea())
            if (b.getType().equals(Material.AIR))
                b.setType(Material.LAVA);
    }

    /**
     * Sets the next target level for lava.
     *
     * @param newLevel The next checkpoint for the lava level
     * @param time The time it takes to reach that level
     */
    public static void setNextLevel (int newLevel, int time) {
        nextLevel = newLevel;
        timer = time;
        targetTime = time;
    }

    /**
     * Cancel the game timer.
     */
    public static void cancelTimer () {
        timer = -1;
    }

    /**
     * Retrieves the current round of the game.
     *
     * @return The round ID
     */
    public static int getRoundId () {
        return roundId;
    }

    /**
     * Displays the Boss Bar for all players which shows the current lava level.
     */
    public static void showLevelBossBar () {
        if (levelBossBar == null)
            levelBossBar = Bukkit.createBossBar( "Current Lava Level:", BarColor.RED, BarStyle.SOLID);

        levelBossBar.removeAll();

        for (Player p : Bukkit.getOnlinePlayers())
            levelBossBar.addPlayer(p);

        updateLevelBossBar();
    }

    /**
     * Update the boss bar when the lava rises.
     */
    public static void updateLevelBossBar () {
        levelBossBar.setVisible(true);
        levelBossBar.setTitle("Current Lava Level: " + currentLevel);
        levelBossBar.setProgress((float) (currentLevel + 1) / (Main.MAX_HEIGHT - 4));
    }

    /**
     * Hides the boss bar display from players.
     */
    public static void hideLevelBossBar () {
        if (levelBossBar == null)
            levelBossBar = Bukkit.createBossBar( "Current Lava Level:", BarColor.RED, BarStyle.SOLID);

        levelBossBar.setVisible(false);
    }

    /**
     * Get the current lava level.
     *
     * @return The current level
     */
    public static int getCurrentLevel () {
        return currentLevel;
    }

    /**
     * Cancel the main logic loop task, which runs every second.
     */
    public static void cancelTask () {
        Bukkit.getScheduler().cancelTask(taskId);
        taskId = -1;
    }

}
