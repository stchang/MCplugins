package org.wochang.plugins;

import java.util.Arrays;

// needed by all cmds
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by this cmd
import org.bukkit.World;
import org.bukkit.Location;

// no args
public class GoHomeCommand implements CommandExecutor {
    
    // This method is called, when somebody uses our command
    // @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Bukkit.getLogger().info("onCommand: " + label);
	Bukkit.getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    Bukkit.getLogger().info("onCommand: sender is not a Player");
	    return false;
	}

	Player player = (Player) sender;

	Location spawn_loc = player.getBedSpawnLocation();
	if (spawn_loc != null) {
	    player.teleport(spawn_loc);
	    String success_msg = player.getDisplayName() + " went home";
	    Bukkit.getLogger().info(success_msg);
	    Bukkit.getServer().broadcastMessage(success_msg);
	} else {
	    String fail_msg =
		player.getDisplayName() + " could not go home: no spawn point set";
	    Bukkit.getLogger().info(fail_msg);
	    Bukkit.getServer().broadcastMessage(fail_msg);
	}

	return true;
    }
}
