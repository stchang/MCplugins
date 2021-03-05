package org.wochang.plugins;

import java.util.Arrays;

// needed by all cmds
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by this cmd
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

// gift cmd: requires 1 argument: target player
public class GiftCommand implements CommandExecutor {
    
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
	
	// gift cmd: requires 1 argument: target player
	if (args.length != 1) {
	    String wrong_args_errmsg = 
		"gift: wrong number of arguments, given " + Arrays.toString(args) ;
	    Bukkit.getServer().broadcastMessage(wrong_args_errmsg);
	    Bukkit.getLogger().info(wrong_args_errmsg);
	    return false;
	}
	
	Player gift_target = Bukkit.getPlayer(args[0]);
	
	// Create a new ItemStack (type: diamond)
	ItemStack diamond = new ItemStack(Material.DIAMOND);
	
	// // Create a new ItemStack (type: brick)
	// ItemStack bricks = new ItemStack(Material.BRICK, 20);
	// ItemStack bricks = new ItemStack(Material.BRICK);
	// // Set the amount of the ItemStack
	// bricks.setAmount(20);
	
	// Create a new ItemStack (type: brick)
	ItemStack pa = new ItemStack(Material.DIAMOND_PICKAXE);
	ItemStack sw = new ItemStack(Material.DIAMOND_SWORD);
	ItemStack ns = new ItemStack(Material.NETHER_STAR);
	ItemStack obs = new ItemStack(Material.OBSIDIAN,10);
	ItemStack gss = new ItemStack(Material.GLOWSTONE,20);
	ItemStack gds = new ItemStack(Material.GLOWSTONE_DUST,20);
	
	
	// Give the player our items (comma-seperated list of all ItemStack)
	gift_target.getInventory().addItem(diamond, pa, sw, ns, obs, gss, gds);
	
	String success_msg = player.getDisplayName() + " gave a gift to " + args[0];
	Bukkit.getLogger().info(success_msg);
	Bukkit.getServer().broadcastMessage(success_msg);

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
