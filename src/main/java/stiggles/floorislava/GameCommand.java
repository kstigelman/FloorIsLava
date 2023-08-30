package stiggles.floorislava;

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

        if (args.length != 0)
            return false;

        if (RoundManager.getRoundId() != -1)
            return false;

        GameStartManager.setCountdown(10);
        return true;
    }
}