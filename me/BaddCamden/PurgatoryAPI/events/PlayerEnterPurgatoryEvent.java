package me.BaddCamden.PurgatoryAPI.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerEnterPurgatoryEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    Player player;
    World world;
    boolean adventureMode;
    
    public PlayerEnterPurgatoryEvent(Player p, World w, boolean adventure) {
    	player = p;
    	world = w;
    	adventureMode = adventure;
    }
    public Player getPlayer() {
    	return player;
    }
    public World getWorld() {
    	return world;
    }
    public boolean getAdventureMode() {
    	return adventureMode;
    }
    public void setAdventureMode(boolean adventure) {
    	adventureMode = adventure;
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
