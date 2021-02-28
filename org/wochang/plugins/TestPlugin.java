package org.wochang.plugins;
import java.util.Arrays;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.Location;

public class TestPlugin extends JavaPlugin {
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
	getLogger().info("onEnable has been invoked!");

        // // Register our command "gift" (set an instance of your command class as executor)
        // this.getCommand("gift").setExecutor(new CommandGift());
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
	getLogger().info("onDisable has been invoked!");
    }
// }

// public class CommandGift implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	getLogger().info("onCommand: " + label);
	getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    getLogger().info("onCommand: sender is not a Player");
	    return false;
	}

	ArrayList<Entity> summoned_mobs = new ArrayList<Entity>();
	
        if (sender instanceof Player) {
            Player player = (Player) sender;

	    // gift cmd: requires 1 argument: target player
	    if (label.equals("gift")) {

		if (args.length != 1) {
		    String wrong_args_errmsg = 
			"gift: wrong number of arguments, given " + Arrays.toString(args) ;
		    getServer().broadcastMessage(wrong_args_errmsg);
		    getLogger().info(wrong_args_errmsg);
		    return false;
		}
		
		Player gift_target = Bukkit.getPlayer(args[0]);

		// Create a new ItemStack (type: diamond)
		ItemStack diamond = new ItemStack(Material.DIAMOND);
		
		// Create a new ItemStack (type: brick)
		ItemStack bricks = new ItemStack(Material.BRICK, 20);
		// ItemStack bricks = new ItemStack(Material.BRICK);
		
		// // Set the amount of the ItemStack
		// bricks.setAmount(20);
		
		// Give the player our items (comma-seperated list of all ItemStack)
		gift_target.getInventory().addItem(bricks, diamond);

		String success_msg = player.getDisplayName() + " gave a gift to " + args[0];
		getLogger().info(success_msg);
		getServer().broadcastMessage(success_msg);
	    } else if (label.equals("gohome")) {
		Location spawn_loc = player.getBedSpawnLocation();
		player.teleport(spawn_loc);
	    } else if (label.equals("mkmobs")) {
		// up to 5 args:
		// 1) mob name
		// 2) # of mobs
		// 3-5) x y z
		World w = player.getWorld();
		Location loc = player.getLocation();
		int num_mobs = 1;
		if (args[1] != null) {
		    num_mobs = Integer.parseInt(args[1]);
		}
		if (args.length == 5) {
		    loc = new Location(w,
				       Double.valueOf(args[2]),
				       Double.valueOf(args[3]),
				       Double.valueOf(args[4]));
		}

		//I.e. for spawning a Zombie, you can find all the entity types here: link
		//		Zombie z=(Zombie)w.spawnEntity(loc,EntityType.Zombie);
		for (int i = 0; i < num_mobs; i++) { 
		    summoned_mobs.add(w.spawnEntity(loc,EntityType.ZOMBIE));
		}
		//You need to cast the spawned entity to the given types class, i.e. Zombie
		//If you call this method, you will get the spawned entity,
		//so you will be able to save it, modify it or access it later.
		String success_msg =
		    player.getDisplayName() + " made " + Integer.toString(num_mobs) + " mobs";
		getLogger().info(success_msg);
		getServer().broadcastMessage(success_msg);
	    } else if (label.equals("clearmobs")) {
		for (Entity e: summoned_mobs) {
		    e.remove();
		}
		summoned_mobs = new ArrayList<Entity>();
		String success_msg =
		    player.getDisplayName() + " cleared summoned mobs";
		getLogger().info(success_msg);
		getServer().broadcastMessage(success_msg);
	    }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
