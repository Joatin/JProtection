package com.hotmail.joatin37.jprotection;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.hotmail.joatin37.jprotection.eventhandlers.BlockDamageEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.BlockPlaceEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.HangingBreakEventByEntityHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.HangingPlaceEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.PlayerDeathEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.PlayerInteractEntityEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.PlayerInteractEventHandler;
import com.hotmail.joatin37.jprotection.eventhandlers.VehicleDestroyEventHandler;
import com.hotmail.joatin37.jprotection.util.BukkitMetrics;

public class JProtection extends JavaPlugin implements Listener{
	
	private int totalamountoflocks=0;
	private int totalamountofplayers=0;
	
	private HashMap<String, PlayerHandle>playerhandle;
	public Economy econ = null;
	
	private ServerCommandHandler servercommand;
	private PlayerCommandHandler playercommand;
	
	public EntityLock entitylock;
	public BlockLock blocklock;
	
	private BlockDamageEventHandler blockdamage;
	private BlockPlaceEventHandler blockplace;
	private HangingBreakEventByEntityHandler hangingbreak;
	private HangingPlaceEventHandler hangingplace;
	private PlayerInteractEntityEventHandler playerinteractentity;
	private PlayerInteractEventHandler playerinteract;
	private VehicleDestroyEventHandler vehicledestroy;
	private PlayerDeathEventHandler playerdeath;
	
	private FileConfiguration playerConfig = null;
	private File playerConfigFile = null;
	
	private HashMap<String, Boolean> getPreparedBlockAutoMap(){
		HashMap<String, Boolean>map=new HashMap<String, Boolean>();
		Material[] material = Material.values();
		for(int i = 0; i<material.length; i++){
			if(material[i].isBlock()){
				map.put(Integer.toString(material[i].getId()), false);
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		List<String>list = getConfig().getStringList("autoprotect.blocks");
		while(iterator.hasNext()){
			String s;
			if(list.contains(s=(String)iterator.next())){
				map.put(s, true);
			}
		}
		
		return map;
	}
	private HashMap<String, Boolean> getPreparedBlockProtectionMap(){
		HashMap<String, Boolean>map=new HashMap<String, Boolean>();
		Material[] material = Material.values();
		for(int i = 0; i<material.length; i++){
			if(material[i].isBlock()){
				map.put(Integer.toString(material[i].getId()), true);
			}
		}
		Iterator<String> iterator = map.keySet().iterator();
		List<String>list = getConfig().getStringList("protect.blocks");
		while(iterator.hasNext()){
			String s;
			if(list.contains(s=(String)iterator.next())){
				map.put(s, false);
			}
		}
		return map;
	}
	private HashMap<String, Boolean> getPreparedEntityAutoMap(){
		HashMap<String, Boolean>map=new HashMap<String, Boolean>();
		EntityType[] entitytype = EntityType.values();
		for(int i = 0; i<entitytype.length; i++){
			map.put(Short.toString(entitytype[i].getTypeId()), false);
		}
		Iterator<String> iterator = map.keySet().iterator();
		List<String>list = getConfig().getStringList("autoprotect.entities");
		while(iterator.hasNext()){
			String s;
			if(list.contains(s=(String)iterator.next())){
				map.put(s, true);
			}
		}
		return map;
	}
	
	private HashMap<String, Boolean> getPreparedEntityProtectionMap(){
		HashMap<String, Boolean>map=new HashMap<String, Boolean>();
		EntityType[] entitytype = EntityType.values();
		for(int i = 0; i<entitytype.length; i++){
			map.put(Short.toString(entitytype[i].getTypeId()), true);
		}
		Iterator<String> iterator = map.keySet().iterator();
		List<String>list = getConfig().getStringList("protect.entities");
		while(iterator.hasNext()){
			String s;
			if(list.contains(s=(String)iterator.next())){
				map.put(s, false);
			}
		}
		
		return map;
	}

	public void reloadPlayerConfig() {
	    if (playerConfigFile == null) {
	    playerConfigFile = new File(getDataFolder(), "playersaves.sav");
	    }
	    playerConfig = YamlConfiguration.loadConfiguration(playerConfigFile);
	    
	    
	    
	    /*Base maps*/
	    HashMap<String, Boolean>blockauto=getPreparedBlockAutoMap();
	    HashMap<String, Boolean>blockprotection=getPreparedBlockProtectionMap();
	    HashMap<String, Boolean>entityauto=getPreparedEntityAutoMap();
	    HashMap<String, Boolean>entityprotection=getPreparedEntityProtectionMap();
	    
	    List<String>names=playerConfig.getStringList("names");
	    if(names==null)return;
	    playerhandle=new HashMap<String, PlayerHandle>(names.size());
	    Iterator<String> iterator = names.listIterator();
	    while(iterator.hasNext()){
	    	String playername = (String) iterator.next();	 
	    	HashMap<String, Boolean>bauto=new HashMap<String, Boolean>();
	    	bauto.putAll(blockauto);
		    HashMap<String, Boolean>bprotection=new HashMap<String, Boolean>();
		    bprotection.putAll(blockprotection);
		    HashMap<String, Boolean>eauto=new HashMap<String, Boolean>();
		    eauto.putAll(entityauto);
		    HashMap<String, Boolean>eprotection=new HashMap<String, Boolean>();
		    eprotection.putAll(entityprotection);
	    	try{
		    Iterator<?> ibauto = playerConfig.getConfigurationSection(playername+".blockautolock").getValues(false).entrySet().iterator();
		    Iterator<?> ibprotection = playerConfig.getConfigurationSection(playername+".blockprotection").getValues(false).entrySet().iterator();
		    Iterator<?> ieauto = playerConfig.getConfigurationSection(playername+".entityautolock").getValues(false).entrySet().iterator();
		    Iterator<?> ieprotection = playerConfig.getConfigurationSection(playername+".entityprotection").getValues(false).entrySet().iterator();
	    	
		    while(ibauto.hasNext()){
		    	Entry<String, Boolean>e=(Entry<String, Boolean>) ibauto.next();
		    	bauto.put(e.getKey(), e.getValue());
		    }
		    while(ibprotection.hasNext()){
		    	Entry<String, Boolean>e=(Entry<String, Boolean>) ibprotection.next();
		    	bprotection.put(e.getKey(), e.getValue());
		    }
		    while(ieauto.hasNext()){
		    	Entry<String, Boolean>e=(Entry<String, Boolean>) ieauto.next();
		    	eauto.put(e.getKey(), e.getValue());
		    }
		    while(ieprotection.hasNext()){
		    	Entry<String, Boolean>e=(Entry<String, Boolean>) ieprotection.next();
		    	eprotection.put(e.getKey(), e.getValue());
		    }
	    	
	    	
	    	
	    	playerhandle.put(playername, new PlayerHandle(
	    	playername,
	    	bauto,
	    	bprotection,
	    	eauto,
	    	eprotection,
	    	playerConfig.getStringList(playername+".friends"),
	    	playerConfig.getBoolean(playername+".lockondeath", true)	
	    	));
	    	this.addTotalamountofplayers();
	    }catch(Exception e){}
	    }
	}
	
	public FileConfiguration getPlayerConfig() {
	    if (playerConfig == null) {
	        this.reloadPlayerConfig();
	    }
	    return playerConfig;
	}
	
	public void savePlayerConfig() {
		if (playerConfig == null || playerConfigFile == null) {
	    return;
	    }
		Iterator<PlayerHandle> iterator = playerhandle.values().iterator();
		Vector<String> list = new Vector<String>(100, 50);
		while(iterator.hasNext()){
			list.add(((PlayerHandle) iterator.next()).save(playerConfig));
		}
		playerConfig.set("names", list);	    
	    try {
	        getPlayerConfig().save(playerConfigFile);
	    } catch (IOException ex) {
	        this.getLogger().log(Level.SEVERE, "Could not save config to " + playerConfigFile, ex);
	    }
	}
	
	/**
	 * The entry point for this plugin
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void onEnable(){
		saveDefaultConfig();
		/*Sets up vault and notifies the user.*/
		if(!setupEconomy()){
			getLogger().info("§eCouldn't find Vault, running without economy!");
		}
		
		/*Loads the players settings*/
		reloadPlayerConfig();
		
		/*The lock classes*/
		entitylock = new EntityLock(this);
		blocklock = new BlockLock(this);
		
		/*The Command classes*/
		servercommand = new ServerCommandHandler(this);
		playercommand = new PlayerCommandHandler(this);
		
		/*The event handler classes*/
		blockdamage = new BlockDamageEventHandler(this);
		blockplace  = new BlockPlaceEventHandler(this);
		hangingbreak = new HangingBreakEventByEntityHandler(this);
		hangingplace = new HangingPlaceEventHandler(this);
		playerinteractentity = new PlayerInteractEntityEventHandler(this);
		playerinteract = new PlayerInteractEventHandler(this);
		vehicledestroy = new VehicleDestroyEventHandler(this);
		playerdeath = new PlayerDeathEventHandler(this);
		
		/*Makes the plugin listen to events*/
		getServer().getPluginManager().registerEvents(this, this);
		try {
		    BukkitMetrics metrics = new BukkitMetrics(this);
		    metrics.start();
		} catch (IOException e) {
		    // Failed to submit the stats :-(
		}

	}
	/**
	 * The last function to be called
	 * 
	 * @since 1.0.0
	 */
	@Override
	public void onDisable(){
		save();
	}
	
	private void save(){
		savePlayerConfig();
		entitylock.save();
		blocklock.save();
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public PlayerHandle getPlayerHandle(String playername){
		if(playerhandle.get(playername)==null){
			playerhandle.put(playername, createPlayerHandleWithDeafults(playername));
			this.addTotalamountofplayers();
		}
		return playerhandle.get(playername);
	}
	
	/**
	 * Return a new PlayerHandle with default settings specified in the config
	 * 
	 * @param name
	 * @return PlayerHandle
	 * 
	 * @since 1.0.0
	 */
	private PlayerHandle createPlayerHandleWithDeafults(String name){
		return new PlayerHandle(name, this.getPreparedBlockAutoMap(), this.getPreparedBlockProtectionMap(), this.getPreparedEntityAutoMap(), this.getPreparedEntityProtectionMap(), new Vector<String>(), this.getConfig().getBoolean("autoprotect.lockondeath", false));
	}
	
	/**
	 * the command listener.
	 * 
	 * @since 1.0.0
	 */	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if (sender instanceof Player) {
    			playercommand.onCommand((Player)sender, cmd, label, args);
    		}else{
    			servercommand.onCommand(sender, cmd, label, args);
    	}
		return true;
		
	}
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onHangingPlaceEvent(HangingPlaceEvent event){
		hangingplace.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event){
		hangingbreak.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		blockplace.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event){
		blockdamage.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event){
		playerinteractentity.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		playerinteract.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onVehicleDestroyEvent(VehicleDestroyEvent event){
		vehicledestroy.Handle(event);
	}
	
	/**
	 * Passes the event to the correct handler.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		playerdeath.Handle(event);
	}
	
	public int getTotalamountoflocks() {
		return totalamountoflocks;
	}
	public void addTotalamountoflocks() {
		this.totalamountoflocks++;
	}
	
	public void removeTotalamountoflocks() {
		this.totalamountoflocks--;
	}
	public int getTotalamountofplayers() {
		return totalamountofplayers;
	}
	public void addTotalamountofplayers() {
		this.totalamountofplayers++;
	}
	
	public void removeTotalamountofplayers() {
		this.totalamountofplayers--;
	}
}
