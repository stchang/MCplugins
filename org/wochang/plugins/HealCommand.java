package org.wochang.plugins;

import java.util.Arrays;

// needed by all cmds
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;

// needed by this cmd
import org.bukkit.potion.PotionEffectType;

// gift cmd: requires 1 argument: target player
public class HealCommand implements CommandExecutor {
    
    // This method is called, when somebody uses our command
    // @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	Bukkit.getLogger().info("heal cmd: " + label);
	Bukkit.getLogger().info("args: " + Arrays.toString(args));
        if (!(sender instanceof Player)) {
	    Bukkit.getLogger().info("heal cmd: sender is not a Player");
	    return false;
	}

	Player healer = (Player) sender;
	Player playerToHeal = (Player) sender;

	if (args.length == 1) {
	    playerToHeal = Bukkit.getPlayer(args[0]);
	    if (playerToHeal == null) {
		Bukkit.getLogger().info("heal: could not find player " + args[0]);
		return false;
	    }
	}	    
	
	playerToHeal.setHealth(20.0);
	playerToHeal.setFoodLevel(20);
	playerToHeal.setFireTicks(0);
	playerToHeal.removePotionEffect(PotionEffectType.POISON);
	playerToHeal.removePotionEffect(PotionEffectType.SLOW);
	playerToHeal.removePotionEffect(PotionEffectType.WEAKNESS);
	playerToHeal.removePotionEffect(PotionEffectType.BLINDNESS);
	playerToHeal.removePotionEffect(PotionEffectType.CONFUSION);
	
	String success_msg =
	    healer.getDisplayName()
	    + " healed " + playerToHeal.getDisplayName();
	Bukkit.getLogger().info(success_msg);
	Bukkit.getServer().broadcastMessage(success_msg);

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
