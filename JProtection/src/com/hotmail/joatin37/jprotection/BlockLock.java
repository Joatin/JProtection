package com.hotmail.joatin37.jprotection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.Level;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class BlockLock {
	
	private JProtection jprotect;
	private HashMap<Location, BlockProtection> locks;
	
	private FileConfiguration blockConfig = null;
	private File blockConfigFile = null;

	public BlockLock(JProtection jProtection) {
		jprotect = jProtection;
		load();
	}
	
	public boolean isOwner(Block block, String player){
		return locks.get(block.getLocation()).getOwner().equals(player);
	}
	
	public boolean hasLock(Block block){
		return locks.get(block.getLocation())!=null;
	}
	
	public void unlock(Block block, Player player){
		if(locks.get(block.getLocation()).getOwner().equals(player.getName())||player.hasPermission("jprotection.admin.lockmaster")){
			jprotect.getPlayerHandle(locks.get(block.getLocation()).getOwner()).removeAmountlocks();
			if(block.getTypeId()==54){
				jprotect.getPlayerHandle(locks.get(block.getLocation()).getOwner()).removeAmountchests();
			}
			locks.remove(block.getLocation());
			player.sendMessage(jprotect.getConfig().getString("messages.yousuccesfullyromevedthislock", "§eYou succesfully romeved this lock"));
		}else{
			player.sendMessage(jprotect.getConfig().getString("messages.youarenottheownerofthislock", "§4You are not the owner of this lock!"));
		}
	}
	
	public void lock(Block block, Player player){
		if(jprotect.getConfig().getStringList("protection.blocks").contains(Integer.toString(block.getTypeId()))){
			if(locks.get(block.getLocation())==null){
				if(jprotect.econ!=null&&jprotect.getConfig().getDouble("price.blocks."+block.getTypeId(), 0d)!=0d){						
					EconomyResponse response = jprotect.econ.withdrawPlayer(player.getName(), jprotect.getConfig().getDouble("price.blocks."+block.getTypeId()));
					if(!response.transactionSuccess()){
						player.sendMessage(jprotect.getConfig().getString("messages.failedtolockthisblock", "§4Failed to lock this block!"));
						player.sendMessage(response.errorMessage);
					}else{
						locks.put(block.getLocation(), new BlockProtection(block ,jprotect.getPlayerHandle(player.getName())));
						jprotect.getPlayerHandle(player.getName()).addAmountlocks();
						jprotect.addTotalamountoflocks();
						player.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisblock", "§2Successfully locked this block!"));
						player.sendMessage(jprotect.getConfig().getString("messages.amountwaswithdrawnfromyouracount", "§e[amount] was withdrawn from your acount").replace("[amount]", ""+jprotect.getConfig().getDouble("price.blocks."+block.getTypeId())));
					}
				}else{
					locks.put(block.getLocation(), new BlockProtection(block ,jprotect.getPlayerHandle(player.getName())));
					if(block.getTypeId()==54){
						jprotect.getPlayerHandle(player.getName()).addAmountchests();
					}
					jprotect.addTotalamountoflocks();
					jprotect.getPlayerHandle(player.getName()).addAmountlocks();
					player.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisblock", "§2Successfully locked this block!"));
				}
			}else{
				player.sendMessage(jprotect.getConfig().getString("messages.thisblockisalreadylocked", "§4This block is already locked!"));
			}
		}else{
			player.sendMessage(jprotect.getConfig().getString("messages.thisblocktypecantbelocked", "§4This blocktype can't be locked"));
		}
	}
	
	public void lock(Block block, Player commander, String player){
		if(commander.hasPermission("jprotection.admin.lockmaster")){
			if(jprotect.getConfig().getStringList("protection.blocks").contains(Integer.toString(block.getTypeId()))){
				locks.put(block.getLocation(), new BlockProtection(block, jprotect.getPlayerHandle(player)));
				if(block.getTypeId()==54){
					jprotect.getPlayerHandle(player).addAmountchests();
				}
				jprotect.getPlayerHandle(player).addAmountlocks();
				jprotect.addTotalamountoflocks();
				commander.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisblocktoplayer", "§2Successfully locked this block to [player]!").replace("[player]", player));
			}else{
				commander.sendMessage(jprotect.getConfig().getString("messages.thisblocktypecantbelocked", "§4This blocktype can't be locked"));
			}
		}else{
			commander.sendMessage(jprotect.getConfig().getString("messages.youdonthavemasterlockpermission", "§eYou don't have permission to create locks this way"));
		}
	}
	
	public void load() {
	    if (blockConfigFile == null) {
	    	blockConfigFile = new File(jprotect.getDataFolder(), "blocksaves.sav");
	    }
	    blockConfig = YamlConfiguration.loadConfiguration(blockConfigFile);
	    if(blockConfig.getStringList("locs")==null)return;
	    locks=new HashMap<Location, BlockProtection>();
	    Iterator<String> iterator = blockConfig.getStringList("locs").listIterator();
	    while(iterator.hasNext()){
	    	BlockProtection b = new BlockProtection(iterator.next(), blockConfig, jprotect);
	    	locks.put(b.getLocation(), b);
	    }
	}
	
	public void save() {
		if (blockConfig == null || blockConfigFile == null) {
	    return;
	    }    
		Vector<String>list=new Vector<String>(100,100);
		Iterator<BlockProtection> iterator = locks.values().iterator();
		while(iterator.hasNext()){
			list.add(iterator.next().save(blockConfig));
		}
		blockConfig.set("locs", list);
	    try {
	    	blockConfig.save(blockConfigFile);
	    	jprotect.getLogger().info("Succesfully saved blocksaves.sav");
	    } catch (IOException ex) {
	        jprotect.getLogger().log(Level.SEVERE, "Could not save config to " + blockConfigFile, ex);
	    }
	}

	public boolean allowsInteraction(Block block, Player player) {
		if(locks.get(block.getLocation())!=null){
		return locks.get(block.getLocation()).allowsInteraction(player.getName());
		}else{
			return true;
		}
	}
}
