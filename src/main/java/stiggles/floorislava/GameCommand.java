package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Represents an instance of an in-game command that an admin can use to shorten the time until the game begins.
 */
public class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp())
            return false;

        if (args.length == 0) {
            if (RoundManager.getRoundId() != -1)
                return false;

            GameStartManager.setCountdown(10);
            return true;
        }
        if (args.length == 1) {
            if (args[0].equals("hardcore")) {
                if (Main.toggleHardcore())
                    sender.sendMessage(ChatColor.BOLD + ChatColor.RED.toString() + "Hard mode active");
                else
                    sender.sendMessage(ChatColor.GREEN + "Normal mode active");
            }
        }
        return false;
    }
}
