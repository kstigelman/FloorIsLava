package stiggles.floorislava;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import stiggles.floorislava.Cuboids.Cuboid;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static World world;
    private static Cuboid playArea;
    private static Cuboid bedrock;

    //Default world height is decreased for the purpose of this minigame. This will be checked in BuildListener
    public static final int MAX_HEIGHT = 120;
    private static int taskId = -1;

    private static boolean hardcore = false;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        world = Bukkit.getWorlds().get(0);
        world.getWorldBorder().setSize(200);
        world.getWorldBorder().setCenter(0, 0);

        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        playArea = new Cuboid(world, 100, 0, 100, -100, 1, -100);
        bedrock = new Cuboid (world, 100, -1, 100, -100, -1, -100);
        for (Block block : bedrock)
            block.setType (Material.BEDROCK);

        Bukkit.getPluginManager().registerEvents(new BuildListener(), this);
        Bukkit.getPluginManager().registerEvents(new LogEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new MovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PvPListener(), this);

        Bukkit.getPluginManager().registerEvents(new AllMiscEvents(), this);

        Bukkit.getPluginCommand("fil").setExecutor(new GameCommand());

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, GameStartManager::everySecond, 0, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public static Main getInstance() {
        return instance;
    }
    public static World getWorld () {
        return world;
    }
    public static Cuboid getPlayArea () {
        return playArea;
    }
    public static void setPlayArea (Cuboid newArea) {
        playArea = newArea;
    }

    public static boolean toggleHardcore () {
        hardcore = !hardcore;
        return hardcore;
    }
    public static boolean isHardcore () {
        return hardcore;
    }
}
