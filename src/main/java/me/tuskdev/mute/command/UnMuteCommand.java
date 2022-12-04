package me.tuskdev.mute.command;

import me.saiintbrisson.bukkit.command.command.BukkitContext;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.tuskdev.mute.cache.MuteCache;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.model.Mute;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class UnMuteCommand {

    private final MuteCache muteCache;
    private final MuteController muteController;

    public UnMuteCommand(MuteCache muteCache, MuteController muteController) {
        this.muteCache = muteCache;
        this.muteController = muteController;
    }

    @Command(
            name = "unmute",
            permission = "unmute.use",
            usage = "/unmute <player>"
    )
    public void handleCommand(BukkitContext bukkitContext) {
        if (bukkitContext.argsCount() <= 0) {
            bukkitContext.sendMessage("§cUse /unmute <player>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(bukkitContext.getArg(0));
        if (!offlinePlayer.hasPlayedBefore()) {
            bukkitContext.sendMessage("§cPlayer not found.");
            return;
        }

        Mute mute = muteCache.get(offlinePlayer.getUniqueId());
        if (mute == null || mute.hasExpired()) {
            bukkitContext.sendMessage("§cThis player is not muted.");
            return;
        }

        muteCache.invalidate(mute.getTarget());
        muteController.delete(mute.getTarget());

        bukkitContext.sendMessage("§aPlayer unmuted.");
    }

}
