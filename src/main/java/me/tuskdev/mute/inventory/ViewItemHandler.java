package me.tuskdev.mute.inventory;

@FunctionalInterface
public interface ViewItemHandler {

    void handle(ViewSlotContext context);

}
