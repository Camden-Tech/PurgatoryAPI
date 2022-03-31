package me.BaddCamden.PurgatoryAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.BaddCamden.PurgatoryAPI.events.PlayerEnterPurgatoryEvent;
import me.BaddCamden.PurgatoryAPI.events.PlayerExitPurgatoryEvent;

public class APIUtilities {
	public static Main main;
	public FileConfiguration database;
	public File datafile;
	public APIUtilities() {
		database = main.getDataBase();
		datafile = main.getDataFile();
	}
	public void sendToSpecificPurgatory(Player p, @Nullable String world, boolean adventureMode) {
		if(world == null && database.getConfigurationSection("Purgatory.Worlds").getKeys(false).size() == 1) {
			p.teleport(Bukkit.getWorld(database.getString("Purgatory.Worlds")).getSpawnLocation());
			if(adventureMode) p.setGameMode(GameMode.ADVENTURE);
			return;
		}
		
		for(String w : database.getConfigurationSection("Purgatory.Worlds").getKeys(false)) {
			if(w.equalsIgnoreCase(world)) {
				p.teleport(Bukkit.getWorld(w).getSpawnLocation());
				if(adventureMode) p.setGameMode(GameMode.ADVENTURE);
				break;
			} 
		}
	}
	
	public void exitPurgatory(Player p) {
		PlayerExitPurgatoryEvent event = new PlayerExitPurgatoryEvent(p, p.getBedSpawnLocation().getWorld(), p.getWorld());
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if(p.getBedSpawnLocation() != null && p.getBedSpawnLocation().getWorld().equals(getMainWorld())) {
				p.teleport(p.getBedSpawnLocation());	
			} else {
				p.teleport(getMainWorld().getSpawnLocation());
			}
			
		}
	}
	
	public void setTag(String w, String s, String value) {
		database.set("Purgatory.Worlds."+w+".tags."+s, value);
		main.saveDataBase();
	}
	
	public String getTag(String w, String s) {
		return database.getString("Purgatory.Worlds."+w+".tags."+s);
	}
	
	public List<String> getWorlds(){
		List<String> s = new ArrayList<String>();
		s.addAll(database.getConfigurationSection("Purgatory.Worlds").getKeys(false));
		return s;
	}
	
	public World getMainWorld() {
		return Bukkit.getWorld(database.getString("Purgatory.mainworld"));
	}
	
	
	
	public void sendPlayerToPurgatory(Player p) {

		World lowestWorld = null;
		for(World w : Bukkit.getWorlds()) {
			if(database.getConfigurationSection("Purgatory.Worlds").contains(w.getName())) {
				if(!database.getBoolean("Purgatory.Worlds."+w.getName()+".viable")) {
					continue;
				}
				if(database.getInt("Purgatory.Worlds."+w.getName()+".maxplayers") <= w.getPlayers().size()) {
					continue;
				}
				if(lowestWorld == null || w.getPlayers().size() < lowestWorld.getPlayers().size()) {
					lowestWorld = w;
					continue;
				}
				
			}
		}
		PlayerEnterPurgatoryEvent event = new PlayerEnterPurgatoryEvent(p, lowestWorld, true);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if(lowestWorld != null) {
				p.teleport(lowestWorld.getSpawnLocation());
				p.setGameMode(GameMode.ADVENTURE);
			}
		}

	}
	public void sendPlayerToPurgatory(Player p, boolean adventureMode) {
		World lowestWorld = null;
		for(World w : Bukkit.getWorlds()) {
			if(database.getConfigurationSection("Purgatory.Worlds").contains(w.getName())) {
				if(!database.getBoolean("Purgatory.Worlds."+w.getName()+".viable")) {
					continue;
				}
				if(database.getInt("Purgatory.Worlds."+w.getName()+".maxplayers") != -1 && database.getInt("Purgatory.Worlds."+w.getName()+".maxplayers") <= w.getPlayers().size()) {
					continue;
				}
				if(lowestWorld == null || w.getPlayers().size() < lowestWorld.getPlayers().size()) {
					lowestWorld = w;
					continue;
				}
				
			}
		}
		PlayerEnterPurgatoryEvent event = new PlayerEnterPurgatoryEvent(p, lowestWorld, true);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			if(lowestWorld != null) {
				p.sendMessage(ChatColor.BLACK+""+ChatColor.BOLD+"You've been sent to Purgatory");
				p.teleport(lowestWorld.getSpawnLocation());
				if(adventureMode) p.setGameMode(GameMode.ADVENTURE);
			}
		}
	}
	
	/*public void playerVisibility(boolean visible, @Nullable String world) {
		if(world == null && database.getConfigurationSection("Purgatory.Worlds").getKeys(false).size() == 1) {
			database.set("Purgatory.Worlds."+world+".visible", visible);
			main.saveDataBase();
			return;
		}
		for(String w : database.getConfigurationSection("Purgatory.Worlds").getKeys(false)) {
			if(w.equalsIgnoreCase(world)) {
				database.set("Purgatory.Worlds."+world+".visible", visible);
				main.saveDataBase();
				break;
			} 
		}
	}*/
	
	
}
