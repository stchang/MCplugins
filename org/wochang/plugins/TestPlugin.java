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

        // Register commands
        this.getCommand("gift").setExecutor(new GiftCommand());
        this.getCommand("heal").setExecutor(new HealCommand());
        this.getCommand("gohome").setExecutor(new GoHomeCommand());

	MkMobCommand mkmobcmd = new MkMobCommand();
	
        this.getCommand("mkmob").setExecutor(mkmobcmd);
        this.getCommand("nomob").setExecutor(mkmobcmd);

	HenMode henMode = new HenMode(this);

        this.getCommand("henmode").setExecutor(henMode);
	
	// Register Listeners
	getServer().getPluginManager().registerEvents(henMode, this);

    }
    // Fired when plugin is disabled
    @Override
    public void onDisable() {
	getLogger().info("TestPlugin disabled!");
    }
}
