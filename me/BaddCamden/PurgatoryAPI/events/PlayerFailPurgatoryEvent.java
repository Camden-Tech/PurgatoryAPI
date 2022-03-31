package me.BaddCamden.PurgatoryAPI.events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class PlayerFailPurgatoryEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    Player player;
    World world;
    String failReason;
   
    
    public PlayerFailPurgatoryEvent(Player p, World w, String reason) {
    	player = p;
    	world = w;
    	failReason = reason;
    	//COMMON REASON FOR FAILING PURGATORY: DEATH
    	
    }
    
    public Player getPlayer() {
    	return player;
    }
    public World getWorld() {
    	return world;
    }
    public String getFailReason() {
    	return failReason;
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