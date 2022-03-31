package me.BaddCamden.PurgatoryAPI;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.BaddCamden.PurgatoryAPI.events.PlayerEnterPurgatoryEvent;


public class RegisterEvents implements Listener, CommandExecutor, TabCompleter{
	Main mainPlugin;
	public FileConfiguration database;
	public File datafile;
	public boolean hasStarted = false;
	public APIUtilities utilities = new APIUtilities();
	
	public RegisterEvents(Main main) {
		mainPlugin = main;
		database = main.getDataBase();
		datafile = main.getDataFile();
        boolean foundW = false;
		for(String key : database.getKeys(false)) {
			
			if(key.equalsIgnoreCase("Purgatory")) {
				foundW = true;
			}
		}
		if(!foundW) {
			database.createSection("Purgatory");
			database.set("Purgatory.started", false);
			database.set("Purgatory.mainworld", "none");
			database.createSection("Purgatory.Worlds");
			mainPlugin.saveDataBase();
		}
	}
	public void createWorldDatabase(World w) {
		database.createSection("Purgatory.Worlds."+w.getName());
		database.set("Purgatory.Worlds."+w.getName()+".viable", true);
		database.createSection("Purgatory.Worlds."+w.getName()+".tags");
		database.set("Purgatory.Worlds."+w.getName()+".tags.exists", true);
		database.set("Purgatory.Worlds."+w.getName()+".maxplayers", -1);
		database.set("Purgatory.Worlds."+w.getName()+".visibility", true);
		mainPlugin.saveDataBase();
	}
	

	
	
	
	
	
	

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
        List<String> list = new ArrayList<String>();
        List<String> results = new ArrayList<String>();
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("Purgatory")) {
                if (args.length == 0) {
                    list.add("addworld");
                    list.add("removeworld");
                    list.add("worlds");
                    list.add("start");
                    list.add("stop");
                    list.add("setMainWorld");
                    
                    Collections.sort(list);
                    return list;
                } else if (args.length == 1) {

                    list.add("addworld");
                    list.add("worlds");
                    list.add("removeworld");
                    list.add("sendplayer");
                    list.add("start");
                    list.add("stop");
                    list.add("setMainWorld");
                    
                    for (String s : list){
                        if (s.toLowerCase().startsWith(args[0].toLowerCase())){
                        	results.add(s);
                        }
                    }
                    Collections.sort(results);
                    return results;
                } else if(args.length == 2) {
                	if(args[0].equalsIgnoreCase("addworld")) {
                		
                			List<String> worlds = new ArrayList<String>();
                			for(World w : Bukkit.getWorlds()) worlds.add(w.getName());
                			for(String wr : database.getConfigurationSection("Purgatory.Worlds").getKeys(false)) worlds.remove(wr);
                			list.addAll(worlds);
                			
                        
                	} else if(args[0].equalsIgnoreCase("removeworld")) {
                		list.addAll(database.getConfigurationSection("Purgatory.Worlds").getKeys(false));
                        
                	} else if(args[0].equalsIgnoreCase("sendplayer")) {
                		for(Player p : Bukkit.getOnlinePlayers()) {
                			list.add(p.getDisplayName());
                		}
                        
                	} else if(args[0].equalsIgnoreCase("worlds")) {
                		list.addAll(database.getConfigurationSection("Purgatory.Worlds").getKeys(false));
                        
                	}
                    for (String s : list){
                        if (s.toLowerCase().startsWith(args[1].toLowerCase())){
                            results.add(s);
                        }
                    }
                	Collections.sort(results);
                	return results;
                } else if(args.length == 3) {
                	if(args[0].equalsIgnoreCase("worlds")) {
                		list.add("viable");
                		list.add("maxplayers");
                	} else if(args[0].equalsIgnoreCase("sendplayer")) {
                		list.addAll(database.getConfigurationSection("Purgatory.Worlds").getKeys(false));
                	}
                } else if(args.length == 4) {
                	if(args[0].equalsIgnoreCase("worlds")) {
                		if(args[2].equalsIgnoreCase("viable")) {
                			list.add("true");
                			list.add("false");
                		} else if(args[2].equalsIgnoreCase("maxplayers")) {
                			list.add("none");
                		}
                	}
                }
            }
        }
        return list;
	}
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if(arg0.isOp()) {
			if(arg2.equalsIgnoreCase("Purgatory")) {
				if(arg3[0].equals("start")) {
					arg0.sendMessage(ChatColor.GREEN+"Purgatory has started!");
					database.set("Purgatory.started", true);
					mainPlugin.saveDataBase();
					
				} else if(arg3[0].equals("stop")) {
					arg0.sendMessage(ChatColor.GREEN+"Purgatory has stopped!");
					database.set("Purgatory.started", false);
					mainPlugin.saveDataBase();
					
				} else if(arg3[0].equals("worlds")) {
					if(arg3[1] != null && database.getConfigurationSection("Purgatory.Worlds").getKeys(false).contains(arg3[1])) {
						if(arg3[2] != null) {
							if(arg3[3] != null) {
								if (arg3[2].equalsIgnoreCase("viable")){
									if(arg3[3].equalsIgnoreCase("true")) {
										database.set("Purgatory.Worlds."+arg3[1]+".viable", true);
										mainPlugin.saveDataBase();
										arg0.sendMessage(ChatColor.GREEN+"Purgatory world "+arg3[1]+"'s viability has been set to true");
									} else if(arg3[3].equalsIgnoreCase("false")) {
										database.set("Purgatory.Worlds."+arg3[1]+".viable", false);
										mainPlugin.saveDataBase();
										arg0.sendMessage(ChatColor.GREEN+"Purgatory world "+arg3[1]+"'s viability has been set to false");
									}
								} else if (arg3[2].equalsIgnoreCase("maxplayers")){
									try {
										if( Integer.parseInt(arg3[3]) >= -1) database.set("Purgatory.Worlds."+arg3[1]+".maxplayers", Integer.parseInt(arg3[3]));
										mainPlugin.saveDataBase();
									} catch(NumberFormatException e) {
										if(arg3[3].equalsIgnoreCase("none")) {
											database.set("Purgatory.Worlds."+arg3[1]+".maxplayers", -1);
										    mainPlugin.saveDataBase();
										} else arg0.sendMessage(ChatColor.DARK_RED+"FORMAT EXCEPTION :: Use an integer!");
									}
								}
							} else {
								arg0.sendMessage(ChatColor.DARK_RED+"Missing argument! Needs another subcommand!");
							}

						} else {
							arg0.sendMessage(ChatColor.DARK_RED+"Missing argument! Put world name here!");
						}
					}

					
				} else if(arg3[0].equals("addworld")) {
					if(arg3[1] != null && Bukkit.getWorld(arg3[1]) != null) {
						createWorldDatabase(Bukkit.getWorld(arg3[1]));	
						arg0.sendMessage(ChatColor.GREEN+"Purgatory world "+arg3[1]+" has now been created!");
					} else if(arg3[1] == null) {
						arg0.sendMessage(ChatColor.DARK_RED+"Missing Argument! Must include world!");
						return false;
					}

					
				} else if(arg3[0].equals("removeworld")) {
					if(arg3[1] == null) {
						arg0.sendMessage(ChatColor.DARK_RED+"Missing Argument! Must include world!");
						
						return false;
					}
					arg0.sendMessage(ChatColor.GREEN+"Purgatory world "+arg3[1]+" has now been removed!");
					database.set("Purgatory.Worlds."+arg3[1], null);
					mainPlugin.saveDataBase();


					
				} else if(arg3[0].equals("sendplayer")) {
					if(arg3[1] != null && Bukkit.getPlayer(arg3[1]) != null && Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(arg3[1]))) {
						if(arg3[2] != null && database.getConfigurationSection("Purgatory.Worlds").getKeys(false).contains(arg3[2])) {
							
							utilities.sendToSpecificPurgatory(Bukkit.getPlayer(arg3[1]),arg3[2], true);
							
						} else { 
							arg0.sendMessage(ChatColor.DARK_RED+"Missing Arguments! Must include purgatory world! (World must also still exist)");
						}

					} else {
						arg0.sendMessage(ChatColor.DARK_RED+"Missing Arguments! Must include player!");
					}

					
				} else if(arg3[0].equalsIgnoreCase("setMainWorld")) {
					database.set("Purgatory.mainworld", ((Player)arg0).getWorld().getName());
					mainPlugin.saveDataBase();
					arg0.sendMessage(ChatColor.GREEN+"Main World Set!");
				}

				
			}
		}

		return false;
	}
	
}
