SPIGOTAPI=../../Spigot/Spigot-API/target/spigot-api-1.16.5-R0.1-SNAPSHOT-shaded.jar

TestPlugin:
	javac --class-path .:$(SPIGOTAPI) org/wochang/plugins/MyCommand.java
	javac --class-path .:$(SPIGOTAPI) org/wochang/plugins/TestPlugin.java
	jar cf TestPlugin.jar org/wochang/plugins/TestPlugin.class org/wochang/plugins/MyCommand.class plugin.yml
	cp TestPlugin.jar ..

clean:
	rm -rf TestPlugin.jar