package me.BaddCamden.PurgatoryAPI.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerExitPurgatoryEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    Player player;
    World currentWorld;
    World respawnWorld;
    
    public PlayerExitPurgatoryEvent(Player p, World rw, World cw) {
    	player = p;
    	currentWorld = cw;
    	respawnWorld = rw;
    }
    public Player getPlayer() {
    	return player;
    }
    public World getCurrentWorld() {
    	return currentWorld;
    }
    public World respawnWorld() {
    	return respawnWorld;
    }
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}