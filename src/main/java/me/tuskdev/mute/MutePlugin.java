package me.tuskdev.mute;

import me.saiintbrisson.bukkit.command.BukkitFrame;
import me.tuskdev.mute.cache.MuteCache;
import me.tuskdev.mute.command.MuteCommand;
import me.tuskdev.mute.command.UnMuteCommand;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.inventory.ViewFrame;
import me.tuskdev.mute.listener.PlayerChatListener;
import me.tuskdev.mute.view.MuteConfirmView;
import org.bukkit.plugin.java.JavaPlugin;

public class MutePlugin extends JavaPlugin {

    private PooledConnection pooledConnection;

    @Override
    public void onLoad() {
        getDataFolder().mkdir();
        saveResource("database.properties", false);

        pooledConnection = new PooledConnection(getDataFolder().getPath(), "database.properties");
    }

    @Override
    public void onEnable() {
        MuteController muteController = new MuteController(pooledConnection);
        MuteCache muteCache = new MuteCache(muteController);

        ViewFrame viewFrame = new ViewFrame(this);
        viewFrame.register(new MuteConfirmView(muteCache, muteController));

        BukkitFrame bukkitFrame = new BukkitFrame(this);
        bukkitFrame.registerCommands(
                new MuteCommand(muteCache, muteController, viewFrame.getView(MuteConfirmView.class)),
                new UnMuteCommand(muteCache, muteController)
        );

        getServer().getPluginManager().registerEvents(new PlayerChatListener(muteCache, muteController), this);
    }

    @Override
    public void onDisable() {
        pooledConnection.close();
    }
}
