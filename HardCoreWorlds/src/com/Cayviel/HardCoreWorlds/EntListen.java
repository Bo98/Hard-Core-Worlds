package com.Cayviel.HardCoreWorlds;


//import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class EntListen implements Listener{

	private static HardCoreWorlds hcw;
	EntListen(HardCoreWorlds HCW){
		hcw= HCW;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent damageE){
		if(!(damageE instanceof EntityDamageByEntityEvent)) return;	//continue only if entity damages entity
		if (!(damageE.getEntity() instanceof Player)) return;		//continue only if player is hurt
		Player player = (Player)damageE.getEntity();
		if (! Config.getHc(player.getWorld(), player)) return; //continue only if hardcore is true
		EntityDamageByEntityEvent damage =  (EntityDamageByEntityEvent) damageE;
		if(!(damage.getDamager() instanceof Creature)) return; //continue only if a creature is doing the attacking
		int sd = MobDifficulties.getDamage(damage.getDamager(), damage.getEntity().getWorld().getName()); //get Modified damage
		if (sd > 0){
			damageE.setDamage(sd);	
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent dead){

		if (!(dead.getEntity() instanceof Player) && !(dead.getEntity() instanceof OfflinePlayer)){return;} //if not instance of player return
		Player player;
		if (dead.getEntity() instanceof Player){
			player = (Player)dead.getEntity();			//entity is a player
		}else{
			player = ((OfflinePlayer)dead.getEntity()).getPlayer(); //player is offline
		}
		World world = player.getWorld();
		if (BanManager.getSHc(player)){ //server life management
			int sLives = BanManager.getSLives(player.getName())-1;
			BanManager.setSLives(player, sLives, hcw);
			player.sendMessage(sLives + " live(s) remaining on Server");
		}

		if (! Config.getHc(world,player)) return;		//if world is not hardcore, and player isnt hardcore in this world, return
		if (world.getName().equals(BanManager.BannedList.getString("Unbannable World"))) return; // if this is the Unbannable world, return

		int plives = BanManager.getPlayerLives(player.getName(), world.getName());
		if (plives <= 1){
			BanManager.ban(player, world, hcw); //Ban the player
			BanManager.banMessage(player, world);
			playerL.safetyWcheck(); //ensure Unbannable World exists
		}

		BanManager.setPlayerLives(player.getName(), world.getName(),plives-1);
		player.sendMessage(BanManager.getPlayerLives(player.getName(), player.getWorld().getName()) + " live(s) remaining in world " + world.getName());
		//Location spawn = BanManager.Ereturnworld.getSpawnLocation();
		//player.teleport(spawn); //teleport player there.

	}
}
