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

import java.util.concurrent.TimeUnit;

public class RoundManager {

    private static int roundId = -1;
    private static int currentLevel = -1;
    private static int timer = -1;
    private static int nextLevel = 0;
    private static int nextTarget = 0;
    private static BossBar levelBossBar;

    private static final int MAX_HEIGHT = 120;

    private static int taskId = -1;

    public static void everySecond () {
        Bukkit.getConsoleSender().sendMessage("RoundID: " + roundId + " time: " + timer);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getLocation().getBlockY() < RoundManager.getCurrentLevel() - 1) {
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
                setNextLevel(80, 300);
                return;
            }
            if (timer % (timer / nextLevel) == 0)
                nextLevel();
        }
        if (roundId == 2) {
            if (timer % (timer / nextLevel) == 0)
                nextLevel();
            if (timer == 0) {
                ++roundId;
                setNextLevel(118, 300);
            }
        }
        if (timer > -1)
            --timer;

    }

    public static void start () {
        roundId = 0;
        timer = 300;

        Main.getWorld().setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        Main.getWorld().setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
        Main.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        Main.getWorld().setGameRule(GameRule.DO_INSOMNIA, false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GREEN + "The game has begun. Good luck...");
            player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 0);

            if (!player.getGameMode().equals(GameMode.SURVIVAL))
                return;

            PlayerManager.addPlayer (player);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, timer * 20, 4, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, timer * 20, 4, false, false));

            player.getInventory().clear();
            player.setFoodLevel(20);
            player.setSaturation(10.0F);
            player.getWorld().setTime(0);
            showLevelBossBar();

            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_LEGGINGS));
            player.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_BOOTS));

            player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
            player.getInventory().addItem(new ItemStack(Material.STONE_PICKAXE));
            player.getInventory().addItem(new ItemStack(Material.STONE_AXE));
            player.getInventory().addItem(new ItemStack(Material.STONE_SHOVEL));

            player.getInventory().addItem(new ItemStack(Material.APPLE, 4));

            player.getInventory().addItem(new ItemStack(Material.BOW));
            player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                everySecond();
            }
        }, 0, 20);

    }
    public static void end () {
        hideLevelBossBar();
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
    public static void nextLevel () {
        if (currentLevel > nextLevel)
            return;

        ++currentLevel;
        if (currentLevel % 10 == 0)
            PlayerManager.sendPlayersMessage(ChatColor.RED + "The lava is currently at y=" + currentLevel);

        Main.setPlayArea(Main.getPlayArea().shift(stiggles.floorislava.Cuboid.CuboidDirection.Up, 1));
        for (Block b : Main.getPlayArea())
            if (b.getType().equals(Material.AIR))
                b.setType(Material.LAVA);
    }

    public static void setNextLevel (int newLevel, int time) {
        nextLevel = newLevel;
        timer = time;

    }

    public static void cancelTimer () {
        timer = -1;
    }
    public static int getRoundId () {
        return roundId;
    }

    public static void showLevelBossBar () {
        if (levelBossBar == null)
            levelBossBar = Bukkit.createBossBar( "Current Lava Level:", BarColor.RED, BarStyle.SOLID);

        levelBossBar.removeAll();

        for (Player p : Bukkit.getOnlinePlayers())
            levelBossBar.addPlayer(p);

        levelBossBar.setVisible(true);
        levelBossBar.setTitle("Current Lava Level: " + currentLevel);
        levelBossBar.setProgress((float) (currentLevel + 1) / (MAX_HEIGHT - 4));
    }
    public static void hideLevelBossBar () {
        if (levelBossBar == null)
            levelBossBar = Bukkit.createBossBar( "Current Lava Level:", BarColor.RED, BarStyle.SOLID);

        levelBossBar.setVisible(false);
    }
    public static int getCurrentLevel () {
        return currentLevel;
    }
}
