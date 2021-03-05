package org.wochang.plugins;
import java.util.Arrays;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.Location;

public class MkMobCommand implements CommandExecutor {
    
    // track summoned mobs for "mkmob" and "nomob" cmds
    ArrayList<Entity> summoned_mobs = new ArrayList<Entity>();

    // This method is called, when somebody uses our command
    // @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Bukkit.getLogger().info("onCommand: " + label);
	Bukkit.getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    Bukkit.getLogger().info("onCommand: sender is not a Player");
	    return false;
	}

        if (sender instanceof Player) {
            Player player = (Player) sender;

	    // gift cmd: requires 1 argument: target player
	    if (label.equals("gift")) {

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
		
		// Create a new ItemStack (type: brick)
		//		ItemStack bricks = new ItemStack(Material.BRICK, 20);
		// ItemStack bricks = new ItemStack(Material.BRICK);
		
		// Create a new ItemStack (type: brick)
		ItemStack pa = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemStack sw = new ItemStack(Material.DIAMOND_SWORD);
		ItemStack ns = new ItemStack(Material.NETHER_STAR);
 		ItemStack obs = new ItemStack(Material.OBSIDIAN,10);
		ItemStack gss = new ItemStack(Material.GLOWSTONE,20);
		ItemStack gds = new ItemStack(Material.GLOWSTONE_DUST,20);
		
		// // Set the amount of the ItemStack
		// bricks.setAmount(20);
		
		// Give the player our items (comma-seperated list of all ItemStack)
		gift_target.getInventory().addItem(diamond, pa, sw, ns, obs, gss, gds);

		String success_msg = player.getDisplayName() + " gave a gift to " + args[0];
		Bukkit.getLogger().info(success_msg);
		Bukkit.getServer().broadcastMessage(success_msg);
	    } else if (label.equals("gohome")) {
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
	    } else if (label.equals("mkmob")) {
		// up to 5 args:
		// 1) mob name
		// 2) # of mobs
		// 3-5) x y z
		World w = player.getWorld();
		Location loc = player.getLocation();
		int num_mobs = 1;
		String mob_type_str = "ZOMBIE";
		EntityType mob_type = EntityType.ZOMBIE;

		if (args.length == 0) {
		    String fail_msg1 = "mkmob: didn't name a type of mob";
		    Bukkit.getLogger().info(fail_msg1);
		    Bukkit.getServer().broadcastMessage(fail_msg1);
		    return false;
		}

		mob_type_str = args[0].toUpperCase();
		try {
		    mob_type = EntityType.valueOf(mob_type_str);
		} catch (IllegalArgumentException exception) {
		    String fail_msg =
			player.getDisplayName() +
			" tried to make a mob that doesn't exist: " + args[0];
		    Bukkit.getLogger().info(fail_msg);
		    Bukkit.getServer().broadcastMessage(fail_msg);
		    return false;
		}
		
		if (args.length > 1) { // # mobs is 2nd arg
		    try {
			num_mobs = Integer.parseInt(args[1]);
			if (num_mobs >= 256) {
			    String fail_msg =
				player.getDisplayName() +
				" tried to make too many " + mob_type_str.toLowerCase() +
				"s: must be < 256";
			    Bukkit.getLogger().info(fail_msg);
			    Bukkit.getServer().broadcastMessage(fail_msg);
			    return false;
			}
		    } catch (NumberFormatException exception) {
			String fail_msg =
			    player.getDisplayName() +
			    " gave a bad # of mobs: must be a number < 256";
			Bukkit.getLogger().info(fail_msg);
			Bukkit.getServer().broadcastMessage(fail_msg);
			return false;
		    }
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
		    summoned_mobs.add(w.spawnEntity(loc, mob_type));
		}
		//You need to cast the spawned entity to the given types class, i.e. Zombie
		//If you call this method, you will get the spawned entity,
		//so you will be able to save it, modify it or access it later.
		String success_msg =
		    player.getDisplayName() +
		    " made " + num_mobs + " " + mob_type_str.toLowerCase() + "s";
		Bukkit.getLogger().info(success_msg);
		Bukkit.getServer().broadcastMessage(success_msg);
	    } else if (label.equals("nomob")) {
		int num_mobs = summoned_mobs.size();
		for (Entity e: summoned_mobs) {
		    e.remove();
		}
		String success_msg =
		    player.getDisplayName() + " cleared " + num_mobs + " mobs";
		Bukkit.getLogger().info(success_msg);
		Bukkit.getServer().broadcastMessage(success_msg);
		summoned_mobs = new ArrayList<Entity>();
	    }
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
