SPIGOTAPI=../../Spigot/Spigot-API/target/spigot-api-1.16.5-R0.1-SNAPSHOT-shaded.jar

TestPlugin:
	javac -Xlint --class-path .:$(SPIGOTAPI) org/wochang/plugins/TestPlugin.java
	jar cf TestPlugin.jar org/wochang/plugins/*.class plugin.yml
	cp TestPlugin.jar ..

clean:
	rm -rf *.class
	rm -rf org/wochang/plugins/*.class
	rm -rf TestPlugin.jar
