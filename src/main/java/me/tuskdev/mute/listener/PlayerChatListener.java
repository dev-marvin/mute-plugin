package me.tuskdev.mute.listener;

import me.tuskdev.mute.cache.MuteCache;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.model.Mute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final MuteCache muteCache;
    private final MuteController muteController;

    public PlayerChatListener(MuteCache muteCache, MuteController muteController) {
        this.muteCache = muteCache;
        this.muteController = muteController;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Mute mute = muteCache.get(event.getPlayer().getUniqueId());
        if (mute != null && !mute.hasExpired()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou are muted!");
        }

        else if (mute != null) {
            muteCache.invalidate(mute.getTarget());
            muteController.delete(mute.getTarget());
        }
    }

}
