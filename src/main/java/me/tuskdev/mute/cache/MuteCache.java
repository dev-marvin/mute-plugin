package me.tuskdev.mute.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import me.tuskdev.mute.controller.MuteController;
import me.tuskdev.mute.model.Mute;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MuteCache {

    // CHANGE VALUE TO "Optional<Mute>" TO ALLOW NULL VALUES
    private final LoadingCache<UUID, Optional<Mute>> CACHE;

    public MuteCache(MuteController muteController) {
        CACHE = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build(CacheLoader.from(key -> Optional.ofNullable(muteController.select(key))));
    }

    public Mute get(UUID uuid) {
        try {
            return CACHE.get(uuid).orElse(null);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void invalidate(UUID uuid) {
        CACHE.invalidate(uuid);
    }

    public void put(Mute mute) {
        CACHE.put(mute.getTarget(), Optional.of(mute));
    }

}
