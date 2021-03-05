package org.wochang.plugins;

import java.util.Arrays;
import java.util.ArrayList;

// needed by all cmds
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by this cmd
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

// Usage: mkmob mobtype [nummobs=1] [location]
// TODO: implement location

// NOTE: mkmob and nomob cmds must have same obj as cmd executor in main plugin class
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

	Player player = (Player) sender;

	if (label.equals("mkmob")) {
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
	
        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
