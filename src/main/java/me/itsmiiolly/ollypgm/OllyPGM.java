package me.itsmiiolly.ollypgm;

import org.bukkit.plugin.java.JavaPlugin;

public class OllyPGM extends JavaPlugin {
	@Override
	public void onEnable() {
		System.out.println("OllyPGM -> Enabled");
	}

	@Override
	public void onDisable() {
		System.out.println("OllyPGM -> Disabled");
	}
}
