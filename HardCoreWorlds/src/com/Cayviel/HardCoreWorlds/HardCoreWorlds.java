package com.Cayviel.HardCoreWorlds;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class HardCoreWorlds extends JavaPlugin{
	public static Logger log = Logger.getLogger("Minecraft");
	public Listener vh = new VirtualHunger(this);
	public Listener newp = new playerL(this);
	public Listener CreatureDamage = new EntListen(this);
	public static File datafolder;
	public static boolean usePerm = false;
	public static PermissionHandler permissionHandler;
	public static PermissibleBase pb;
	public static boolean OpCommands = true;
	public static Server server;
	
	public void onEnable(){
		server = getServer();
		datafolder  = getDataFolder();
		FileSetup.initfiles(datafolder);
    	Config.initconfig();
    	BanManager.init();
    	MobDifficulties.init();
    	/*
    	String priorityS = Config.getPriority();
    	EventPriority priority = EventPriority.valueOf(priorityS.toUpperCase());
    	*/
    	OpCommands = Config.getOC();
    	
    	PluginManager pm = getServer().getPluginManager();
    	/*if (!isPriority(priorityS)){//if priority isn't recognized
    		priority = EventPriority.NORMAL; 
    		log.info("[HardCoreWorlds]: Priority string in Config.yml not recognized, using default value");
    	}*/
    	pm.registerEvents(newp, this);
    	pm.registerEvents(vh, this);
    	pm.registerEvents(CreatureDamage, this);
    		
		// set up our permissions
		if (pm.getPlugin("Permissions")!=null){
			setupPermissions();
			usePerm = true;
		}
		log.info("[HardCoreWorlds]: Enabled!");
	}
    
	public void onDisable(){
		
	}
	
	public boolean onCommand (CommandSender sender, Command command, String commandLabel, String[] args){
		boolean success =  Commands.ParseCommand(sender,command,commandLabel,args, this);
		return success;
	}
	
	private void setupPermissions() {
		// if our permissions handler isn't null we shall return
		if (permissionHandler != null) return;

		// grab an instance of the permissions plugin
		Plugin permissionsPlugin = getServer().getPluginManager().getPlugin("Permissions");
		permissionHandler = ((Permissions) permissionsPlugin).getHandler();
		//log.info("[HardCoreWorlds]: Found and will use plugin " + ((Permissions) permissionsPlugin).getDescription().getFullName());
	}

	public static boolean getPerm(String permission, OfflinePlayer player, boolean def){
		if (usePerm){
			return permissionHandler.has(player.getName(), "hcw."+permission, BanManager.getubWN());
		}
		return def;
	}
	
	public static boolean getPerm(String permission, Player player, boolean def){
		if (usePerm){
			return permissionHandler.has(player, "hcw."+permission);
		}
		return def;
	}
	
	/*public static boolean isPriority(String string){
		   for(EventPriority p : EventPriority.values()) {
			      if(p.toString().equalsIgnoreCase(string)) {
			         return true;
			      }
			   }
			   return false;
	}*/
	
}
