package org.wochang.plugins;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
	getLogger().info("TestPlugin enabled!");
	getLogger().info("Hi Stella!");
	getLogger().info("Hi Evan!");
	getLogger().info("Hi Henry!");

	MkMobCommand mkmobcmd = new MkMobCommand();
	
        // Register commands
        this.getCommand("gift").setExecutor(new GiftCommand());
        this.getCommand("gohome").setExecutor(new GoHomeCommand());
        this.getCommand("mkmob").setExecutor(mkmobcmd);
        this.getCommand("nomob").setExecutor(mkmobcmd);
    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
	getLogger().info("TestPlugin disabled!");
    }
}
