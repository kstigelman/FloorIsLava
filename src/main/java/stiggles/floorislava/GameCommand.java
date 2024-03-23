package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Represents an instance of an in-game command that an admin can use to shorten the time until the game begins.
 *
 * @quthor Kyler Stigelman
 */
public class GameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // OP only command
        if (!sender.isOp())
            return false;

        if (args.length == 0) {
            // Command may only be used pre-game
            if (RoundManager.getRoundId() != -1)
                return false;

            // Shorten the cooldown to 10 seconds
            GameStartManager.setCountdown(10);
            return true;
        }
        if (args.length == 1) {
            // Can specify the hardcore keyword to toggle hard mode
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
