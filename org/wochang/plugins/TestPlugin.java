package org.wochang.plugins;
import java.util.Arrays;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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

public class TestPlugin extends JavaPlugin {

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
	getLogger().info("onEnable has been invoked!");
	getLogger().info("Hi Stella!");
	getLogger().info("Hi Evan!");
	getLogger().info("Hi Henry!");

	MyCommand mkmobcmd = new MyCommand();
	
        // Register commands
        this.getCommand("gift").setExecutor(new MyCommand());
        this.getCommand("gohome").setExecutor(new MyCommand());
        this.getCommand("mkmob").setExecutor(mkmobcmd);
        this.getCommand("nomob").setExecutor(mkmobcmd);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
	getLogger().info("onDisable has been invoked!");
    }
}
