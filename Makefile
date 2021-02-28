TestPlugin:
	javac --class-path ../../Spigot/Spigot-API/target/spigot-api-1.16.4-R0.1-SNAPSHOT-shaded.jar org/wochang/plugins/TestPlugin.java
	jar cf TestPlugin.jar org/wochang/plugins/TestPlugin.class plugin.yml
	cp TestPlugin.jar ..

clean:
	rm -rf TestPlugin.class
	rm -rf TestPlugin.jar
