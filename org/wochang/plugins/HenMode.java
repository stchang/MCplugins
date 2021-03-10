package org.wochang.plugins;

import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

// needed by all cmds
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by tabcompleter
import org.bukkit.command.TabCompleter;

// needed by listeners
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class HenMode implements Listener, CommandExecutor, TabCompleter {

    private HashSet<Player> henModePlayers = new HashSet<Player>();
    
     @EventHandler
     public void onPlayerJoin(PlayerJoinEvent event) {
         event.setJoinMessage("Welcome to CreativeChangWorld, " + event.getPlayer().getName() + "!");
     }

     @EventHandler
     public void onBlockPlace(BlockPlaceEvent event) {
	 Bukkit.getLogger().info(event.getPlayer().getName()
				 + " put a "
				 + event.getBlockPlaced().getType()
				 + " block on a "
				 + event.getBlockAgainst().getType()
				 + " block, replacing a "
				 + event.getBlockReplacedState().getType()
				 + " block, while holding a "
				 + event.getItemInHand().getType()
				 + " block");

	 Player p = event.getPlayer();

	 if (henModePlayers.contains(p)) {
	     Bukkit.getLogger().info("henmode player detected");
	 } else {
	     Bukkit.getLogger().info("not henmode player");
	 }
	 
	 Bukkit.getLogger().info("henmode player trying to place: " +
				 event.getBlockPlaced().getType().toString());
	 if (henModePlayers.contains(p) &&
	     //	     event.getBlockPlaced().getType().toString().equals("TNT")) {
	     event.getBlockPlaced().getType().equals(Material.TNT)) {
	     Bukkit.getLogger().info(event.getPlayer().getName()
				     + " tried to place a TNT!");
	     Bukkit.getLogger().info("changing it to a wood!");
	     event.getBlockPlaced().setType(Material.ALLIUM);
	 }
     }

        // This method is called, when somebody uses our command
    // @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Bukkit.getLogger().info("HenMode onCommand: " + label);
	Bukkit.getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    Bukkit.getLogger().info("HenMode onCommand: sender is not a Player");
	    return false;
	}

	Player player = (Player) sender;
	Player henModePlayer = (Player) sender;
	
	// gift cmd: requires 1 argument: target player
	if (!((args.length == 1) || (args.length == 2))) {
	    String wrong_args_errmsg = 
		"henmode: wrong number of arguments, given " + Arrays.toString(args);
	    Bukkit.getServer().broadcastMessage(wrong_args_errmsg);
	    Bukkit.getLogger().info(wrong_args_errmsg);
	    return false;
	}

	if (args.length == 2) {
	    // TODO: check that player exists
	    henModePlayer = Bukkit.getPlayer(args[1]);
	    if (henModePlayer == null) { // player doesnt exist
		String no_player_err = "henmode: player doesn't exist: " + args[1];
		Bukkit.getServer().broadcastMessage(no_player_err);
		Bukkit.getLogger().info(no_player_err);
		return false;
	    }
	}
	
	if (args[0].equals("on")) {
	    henModePlayers.add(henModePlayer);
	} else if (args[0].equals("off")) {
	    henModePlayers.remove(henModePlayer);
	} else {
	    String wrong_args_errmsg = 
		"henmode: argument must be on/off, given: " + args[0];
	    Bukkit.getServer().broadcastMessage(wrong_args_errmsg);
	    Bukkit.getLogger().info(wrong_args_errmsg);
	    return false;
	}

	for (Player p: henModePlayers) {
	    Bukkit.getLogger().info("henMode Player: " + p.getDisplayName());
	}
	String success_msg =
	    player.getDisplayName() + " set " +
	    henModePlayer.getDisplayName() + "'s HenMode to " + args[0];
	Bukkit.getLogger().info(success_msg);
	Bukkit.getServer().broadcastMessage(success_msg);
	return true;
    }

    // This method is called, when somebody uses our command
    // @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	// Bukkit.getLogger().info("onTabComplete: " + label);
	// Bukkit.getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    Bukkit.getLogger().info("henMode onTabComplete: sender is not a Player");
	    return null;
	}

	Player player = (Player) sender;

	ArrayList<String> possibleArgs = new ArrayList<String>();

	if (label.equals("henmode")) {
	    if (args.length == 1) {
		if ("on".startsWith(args[0].toLowerCase())) {
		    possibleArgs.add("on");
		}
		if ("off".startsWith(args[0].toLowerCase())) {
		    possibleArgs.add("off");
		}
	    } else if (args.length == 2) {
		for (Player p : Bukkit.getOnlinePlayers()) {
		    if (p.getDisplayName().toLowerCase().startsWith(args[1].toLowerCase())) {
			possibleArgs.add(p.getDisplayName());
		    }
		}
	    }
	    
	    Collections.sort(possibleArgs);
	}

	return possibleArgs;
    }
}
