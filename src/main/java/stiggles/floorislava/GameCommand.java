package stiggles.floorislava;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp())
            return false;

        if (args.length == 0)
            if (RoundManager.getRoundId() == -1)
                GameStartManager.setCountdown(5);

        //if (!gameManager.join(player))
        //    player.sendMessage(ChatColor.RED + "[Error] Game is currently in progress.");

        return false;
    }
}
