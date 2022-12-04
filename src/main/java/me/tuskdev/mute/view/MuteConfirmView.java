package me.tuskdev.mute.view;

import me.tuskdev.mute.cache.MuteCache;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.inventory.OpenViewContext;
import me.tuskdev.mute.inventory.View;
import me.tuskdev.mute.inventory.ViewContext;
import me.tuskdev.mute.model.Mute;
import me.tuskdev.mute.util.ItemBuilder;
import me.tuskdev.mute.util.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

public class MuteConfirmView extends View {

    private final MuteCache muteCache;
    private final MuteController muteController;

    public MuteConfirmView(MuteCache muteCache, MuteController muteController) {
        super(3, "Confirm mute");

        setCancelOnClick(true);

        slot(14, new ItemBuilder(Material.WOOL, 1, (byte) 14).setName("§cCancel").toItemStack()).closeOnClick().onClick(handler -> handler.getPlayer().playSound(handler.getPlayer().getLocation(), "random.click", 1, 1));

        this.muteCache = muteCache;
        this.muteController = muteController;
    }

    @Override
    protected void onOpen(OpenViewContext context) {
        OfflinePlayer target = context.get("target");
        context.setInventoryTitle("Confirm mute of " + target.getName());
    }

    @Override
    protected void onRender(ViewContext context) {
        OfflinePlayer target = context.get("target");
        String reason = context.get("reason");
        long duration = context.get("duration");

        slot(12, new ItemBuilder(Material.WOOL, 1, (byte) 5).setName("§aConfirm").setLore("", "  §7Target: §f" + target.getName(), "  §7Time: §f" + (duration == -1 ? "Permanent" : TimeParser.format(duration)), "  §7Reason: §f" + reason, "").toItemStack()).closeOnClick().onClick(handler -> {
            Mute newMute = new Mute(target.getUniqueId(), context.getPlayer().getUniqueId(), System.currentTimeMillis() + duration, reason);
            muteCache.put(newMute);
            muteController.insert(newMute);

            handler.getPlayer().playSound(handler.getPlayer().getLocation(), "random.click", 1, 1);
            context.getPlayer().sendMessage("§aPlayer muted.");
        });
    }
}
