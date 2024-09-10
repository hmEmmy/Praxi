package me.emmy.practice.util;

import me.emmy.practice.Practice;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BaseEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        Practice.getInstance().getServer().getPluginManager().callEvent(this);
    }

}
