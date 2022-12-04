package me.tuskdev.mute.model;

import java.util.UUID;

public class Mute {

    private final UUID target, muter;
    private final long time;
    private final String reason;

    public Mute(UUID target, UUID muter, long time, String reason) {
        this.target = target;
        this.muter = muter;
        this.time = time;
        this.reason = reason;
    }

    public UUID getTarget() {
        return target;
    }

    public UUID getMuter() {
        return muter;
    }

    public long getTime() {
        return time;
    }

    public boolean hasExpired() {
        if (time == -1) return false;

        return System.currentTimeMillis() > time;
    }

    public String getReason() {
        return reason;
    }

}
