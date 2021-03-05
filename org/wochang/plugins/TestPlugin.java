package org.wochang.plugins;
import org.bukkit.plugin.java.JavaPlugin;

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
