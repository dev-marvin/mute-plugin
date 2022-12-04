package me.tuskdev.mute.inventory;

import org.bukkit.plugin.Plugin;

public interface ViewProvider {

	Plugin getHolder();

	ViewFrame getFrame();

}
