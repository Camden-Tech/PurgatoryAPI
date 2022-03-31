package me.BaddCamden.PurgatoryAPI;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin{
	public File datafile;
	public FileConfiguration database;
	public FileConfiguration config;
	@Override
	public void onEnable() {
		APIUtilities.main = this;
		datafile = new File(this.getDataFolder()+"/database.yml");
		database = YamlConfiguration.loadConfiguration(datafile);
  	    if (!datafile.exists()) {
	        datafile.getParentFile().mkdirs();
	        try {
	                datafile.createNewFile();
	        } catch (IOException ex) {
	                ex.printStackTrace();
	        }
	        loadDatabase();
	    }
		PluginManager pm = getServer().getPluginManager();
		RegisterEvents listener = new RegisterEvents(this);
		pm.registerEvents(listener, this );
		
		this.getCommand("Purgatory").setExecutor(listener);
		config = this.getConfig();
	}
	@Override
	public void onDisable() {
		
	}
    public void loadDatabase() {
        try {
            database.load(datafile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
	public FileConfiguration getDataBase() {
		return database;
	}
	
	public File getDataFile() {
		return datafile;
	}
	
	 public void saveDataBase() {
		 try {
			 database.save(datafile);
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	 }
}
