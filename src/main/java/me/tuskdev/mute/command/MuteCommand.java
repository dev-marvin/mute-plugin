package me.tuskdev.mute.command;

import com.google.common.collect.ImmutableMap;
import me.saiintbrisson.minecraft.command.annotation.Command;
import me.saiintbrisson.minecraft.command.command.Context;
import me.saiintbrisson.minecraft.command.target.CommandTarget;
import me.tuskdev.mute.cache.MuteCache;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.model.Mute;
import me.tuskdev.mute.util.TimeParser;
import me.tuskdev.mute.view.MuteConfirmView;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class MuteCommand {

    private final MuteCache muteCache;
    private final MuteController muteController;
    private final MuteConfirmView muteConfirmView;

    public MuteCommand(MuteCache muteCache, MuteController muteController, MuteConfirmView muteConfirmView) {
        this.muteCache = muteCache;
        this.muteController = muteController;
        this.muteConfirmView = muteConfirmView;
    }

    @Command(
            name = "mute",
            aliases = { "tempmute" },
            permission = "mute.use",
            usage = "/mute <player> <time> <reason>",
            target = CommandTarget.PLAYER
    )
    public void handleCommand(Context<Player> context) {
        if (context.argsCount() <= 1) {
            context.sendMessage("§cUse /mute <player> <time> <reason>");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(context.getArg(0));
        if (!offlinePlayer.hasPlayedBefore()) {
            context.sendMessage("§cPlayer not found.");
            return;
        }

        if (offlinePlayer.getName().equals(context.getSender().getName())) {
            context.sendMessage("§cYou can't mute yourself.");
            return;
        }

        Mute mute = muteCache.get(offlinePlayer.getUniqueId());
        if (mute != null && !mute.hasExpired()) {
            context.sendMessage("§cThis player is already muted.");
            return;
        }

        // INVALID AND DELETE EXPIRED MUTE
        else if (mute != null) {
            muteCache.invalidate(mute.getTarget());
            muteController.delete(mute.getTarget());
        }

        long time = (context.getArg(1).contains("-") ? TimeParser.convert(context.getArg(1)) : -1);
        if (context.argsCount() >= 2 && context.getArg(1).contains("-") && time == -1) {
            context.sendMessage("§cInvalid time.");
            return;
        }

        String reason = arrayToString(context.getArgs());
        if (reason.length() > 255) {
            context.sendMessage("§cReason too long.");
            return;
        }

        muteConfirmView.open(context.getSender(), ImmutableMap.of("target", offlinePlayer, "duration", time, "reason", reason));
    }

    String arrayToString(String[] array) {
        StringBuilder builder = new StringBuilder();

        for (int i = 2; i < array.length; i++)
            builder.append(array[i]).append(" ");

        return builder.toString().trim();
    }

}
