package stiggles.floorislava;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static World world;
    private static stiggles.floorislava.Cuboid playArea;
    private static stiggles.floorislava.Cuboid bedrock;

    public Main () {

    }



    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        world = Bukkit.getWorlds().get(0);
        world.getWorldBorder().setSize(200);
        world.getWorldBorder().setCenter(0, 0);

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        playArea = new stiggles.floorislava.Cuboid(world, 100, 0, 100, -100, 1, -100);
        bedrock = new stiggles.floorislava.Cuboid (world, 100, -1, 100, -100, -1, -100);
        for (Block block : bedrock)
            block.setType (Material.BEDROCK);

        Bukkit.getPluginManager().registerEvents(new BuildListener(), this);
        Bukkit.getPluginManager().registerEvents(new LogEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PvPListener(), this);

        Bukkit.getPluginCommand("fil").setExecutor(new GameCommand());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, GameStartManager::everySecond, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }

    public static Main getInstance() {
        return instance;
    }

    public static World getWorld () {
        return world;
    }
    public static stiggles.floorislava.Cuboid getPlayArea () {
        return playArea;
    }
    public static void setPlayArea (stiggles.floorislava.Cuboid newArea) {
        playArea = newArea;
    }
}
