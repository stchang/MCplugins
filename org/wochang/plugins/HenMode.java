package org.wochang.plugins;

import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

// needed by cmds
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by tabcompleter
import org.bukkit.command.TabCompleter;

// needed by listeners
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

// needed by this class
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class HenMode implements Listener, CommandExecutor, TabCompleter {

    private HashSet<Player> henModePlayers = new HashSet<Player>();

    private JavaPlugin owningPlugin;

    public HenMode(JavaPlugin p) {
	owningPlugin = p;
    }

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

	// check for henmode player
	if (henModePlayers.contains(p)) {
	    Bukkit.getLogger().info("henmode player trying to place: " +
				    event.getBlockPlaced().getType().toString());

	    // henmode player cannot place tnt
	    if (event.getBlockPlaced().getType().equals(Material.TNT)) {
		Bukkit.getLogger().info(event.getPlayer().getName()
					+ " tried to place a TNT!");
		Bukkit.getLogger().info("changing it to a wood!");
		event.getBlockPlaced().setType(Material.ALLIUM);
	    }
	}

	// mark all placed blocks with their owner
	Block b = event.getBlockPlaced();
	b.setMetadata("owner", new FixedMetadataValue(owningPlugin,p));
    }
    
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
	Player breaker = event.getPlayer();
	
	Bukkit.getLogger().info(breaker.getDisplayName()
				+ " tried to break a "
				+ event.getBlock().getType()
				+ " block");

	Player owner = null;
	if (event.getBlock().hasMetadata("owner")) {
	    List<MetadataValue> vals = event.getBlock().getMetadata("owner");
	    if (!vals.isEmpty()) {
		owner = (Player) vals.get(0).value();
		Bukkit.getLogger().info("block placed by " + owner.getDisplayName());
	    } else {
		Bukkit.getLogger().info("shouldn't get here: couldnt find owner");
	    }
	} else {
	    Bukkit.getLogger().info("block is a world block");
	}	    

	// check for henmode player
	if (henModePlayers.contains(breaker)) {
	    Bukkit.getLogger().info("onBlockBreak: henmode player detected");

	    // henmode player cannot break other players blocks;
	    // ok to break generated world blocks (owner = null)
	    if ((owner != null) && !owner.equals(breaker)) {
		event.setCancelled(true);
		String break_err =
		    "HenMode player " + breaker.getDisplayName()
		    + " prevented from breaking "
		    + event.getBlock().getType() +
		    " block placed by " +
		    owner.getDisplayName();
		Bukkit.getServer().broadcastMessage(break_err);
		Bukkit.getLogger().info(break_err);
	    }
	}
    }
    
    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
	Bukkit.getLogger().info(event.getPlayer().getName()
				+ " tried to dump a "
				+ event.getBucket());
	
	Player p = event.getPlayer();
	if (henModePlayers.contains(p) &&
	    event.getBucket().equals(Material.LAVA_BUCKET)) {
	    event.setCancelled(true);
	    
	    p.getInventory().setItemInMainHand(new ItemStack(Material.RED_TULIP));
	    String lava_err = "Stopped HenMode player " + p.getDisplayName()
		+ " from dumping LAVA!";
	    Bukkit.getServer().broadcastMessage(lava_err);
	    Bukkit.getLogger().info(lava_err);
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
