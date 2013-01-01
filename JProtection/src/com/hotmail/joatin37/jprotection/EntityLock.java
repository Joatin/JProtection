/*
 * Copyright 2013 Joatin Granlund. All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this list of
 *    conditions and the following disclaimer.
 *
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list
 *    of conditions and the following disclaimer in the documentation and/or other materials
 *    provided with the distribution.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.hotmail.joatin37.jprotection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.logging.Level;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;

public class EntityLock {
	
	private HashMap<UUID, EntityProtection> locks;
	private JProtection jprotect;
	
	private FileConfiguration entityConfig = null;
	private File entityConfigFile = null;

	public EntityLock (JProtection jprotection){
		jprotect=jprotection;
		load();
	}
	
	public void getInfo(Entity entity, Player player){
		EntityProtection lock = locks.get(entity.getUniqueId());
		if(lock==null){
			player.sendMessage(jprotect.getConfig().getString("messages.entityinfonolock", "§eThis [entity] doesn't have any protection").replace("[entity]", entity.getType().getName()));
			return;
		}
		Iterator<String> it = lock.getAllowedUsers().iterator();
		String s="";
		while(it.hasNext()){
			s=s+it.next()+", ";
		}
		player.sendMessage(ChatPaginator.wordWrap(jprotect.getConfig().getString("messages.entityinfo", "§2Owner: §4[owner]\n§2Protected: [protected]\n§2Password: §4[password]\n§2Friends: §e[friendslist]").replace("[owner]", lock.getOwner()).replace("[protected]", ""+lock.isProtected()).replace("[password]", lock.getPassword(player)).replace("[friendslist]", s), 119));
		
	}
	public void toggle(Entity entity, Player player){
		
	}
	
	public boolean isLocked(Entity entity){
		return locks.get(entity.getUniqueId())!=null;
	}
	
	public boolean isOwner(Entity entity, String player){
		return locks.get(entity.getUniqueId()).getOwner().equals(player);
	}
	
	public boolean allowsInteraction(Entity entity, Player player){
		if(locks.get(entity.getUniqueId())!=null){
		return locks.get(entity.getUniqueId()).isAllowedUser(player.getName());
		}else{
			return true;
		}
	}
	public void removelock(Entity entity){
		locks.remove(entity.getUniqueId());
	}
	
	public void addFriend(Entity entity, Player player, String friend){
		if(locks.get(entity.getUniqueId())!=null){
			if(locks.get(entity.getUniqueId()).addFriend(player, friend)){
				player.sendMessage(jprotect.getConfig().getString("messages.yousuccesfullyaddedfriend", "§eYou succesfully added "+friend+" to this looks friendlist"));
			}else{
				player.sendMessage(jprotect.getConfig().getString("messages.youcantaddthisfriend", "§4You cant add this friend to this lock"));
			}
		}
			
	}
	
	public void unlock(Entity entity, Player player){
		if(locks.get(entity.getUniqueId()).getOwner().equals(player.getName())||player.hasPermission("jprotection.admin.lockmaster")){
			jprotect.getPlayerHandle(locks.get(entity.getUniqueId()).getOwner()).removeAmountlocks();
			locks.remove(entity.getUniqueId());
			player.sendMessage(jprotect.getConfig().getString("messages.yousuccesfullyromevedthislock", "§eYou succesfully romeved this lock"));
		}else{
			player.sendMessage(jprotect.getConfig().getString("messages.youarenottheownerofthislock", "§4You are not the owner of this lock!"));
		}
	}
	
	public void lock(Entity entity, Player commander, String player){
		if(commander.hasPermission("jprotection.admin.lockmaster")){
			if(jprotect.getConfig().getStringList("protection.entities").contains(Integer.toString(entity.getType().getTypeId()))){
				locks.put(entity.getUniqueId(), new EntityProtection(entity, jprotect.getPlayerHandle(player)));
				jprotect.getPlayerHandle(player).addAmountlocks();
				jprotect.addTotalamountoflocks();
				commander.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisentitytoplayer", "§2Successfully locked this [entity] to [player]!").replace("[entity]", entity.getType().getName()).replace("[player]", player));
			}else{
				commander.sendMessage(jprotect.getConfig().getString("messages.thisentitytypecantbelocked", "§4This entity type ([entity]) can't be locked!").replace("[entity]", entity.getType().getName()));
			}
		}else{
			commander.sendMessage(jprotect.getConfig().getString("messages.youdonthavemasterlockpermission", "§eYou don't have permission to create locks this way"));
		}
		}
	
	
	public void lock(Entity entity, Player player){
		if(jprotect.getConfig().getStringList("protection.entities").contains(Integer.toString(entity.getType().getTypeId()))){
			if(player.hasPermission("jprotection.locker.entitylocker")){
				if(locks.get(entity.getUniqueId())==null){
					if(jprotect.econ!=null&&jprotect.getConfig().getDouble("price.entities."+entity.getType().getTypeId(), 0d)!=0d){						
							EconomyResponse response = jprotect.econ.withdrawPlayer(player.getName(), jprotect.getConfig().getDouble("price.entities."+entity.getType().getTypeId()));
							if(!response.transactionSuccess()){
								player.sendMessage(jprotect.getConfig().getString("messages.failedtolockthisentity", "§4Failed to lock this [entity]!").replace("[entity]", entity.getType().getName()));
								player.sendMessage(response.errorMessage);
							}else{
								locks.put(entity.getUniqueId(), new EntityProtection(entity,jprotect.getPlayerHandle(player.getName())));
								jprotect.getPlayerHandle(player.getName()).addAmountlocks();
								jprotect.addTotalamountoflocks();
								player.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisentity", "§2Successfully locked this [entity]!").replace("[entity]", entity.getType().getName()));
								player.sendMessage(jprotect.getConfig().getString("messages.amountwaswithdrawnfromyouracount", "§e[amount] was withdrawn from your acount").replace("[amount]", ""+jprotect.getConfig().getDouble("price.blocks."+entity.getType().getTypeId())));
							}
					}else{
						locks.put(entity.getUniqueId(), new EntityProtection(entity, jprotect.getPlayerHandle(player.getName())));
						jprotect.addTotalamountoflocks();
						jprotect.getPlayerHandle(player.getName()).addAmountlocks();
						player.sendMessage(jprotect.getConfig().getString("messages.successfullylockedthisentity", "§2Successfully locked this [entity]!").replace("[entity]", entity.getType().getName()));
					}
				}else{
					player.sendMessage(jprotect.getConfig().getString("messages.thisentityisalreadylocked", "§4This [entity] is already locked!").replace("[entity]", entity.getType().getName()));
				}
			}else{
				player.sendMessage(jprotect.getConfig().getString("messages.youdonthavepermissiontomakelocks", "§eYou don't have permission to create locks"));
			}
		}else{
			player.sendMessage(jprotect.getConfig().getString("messages.thisentitytypecantbelocked", "§4This entity type ([entity]) can't be locked!").replace("[entity]", entity.getType().getName()));
		}
	}
	
	
	public void load() {
	    if (entityConfigFile == null) {
	    entityConfigFile = new File(jprotect.getDataFolder(), "entitysaves.sav");
	    }
	    entityConfig = YamlConfiguration.loadConfiguration(entityConfigFile);
	    if(entityConfig.getStringList("uuids")==null)return;
	    locks=new HashMap<UUID, EntityProtection>();
	    Iterator<String> iterator = entityConfig.getStringList("uuids").listIterator();
	    while(iterator.hasNext()){
	    	EntityProtection e = new EntityProtection(iterator.next(), entityConfig);
	    	locks.put(e.getuuid(), e);
	    }
	}
	
	public void save() {
		if (entityConfig == null || entityConfigFile == null) {
	    return;
	    }    
		Vector<String>list=new Vector<String>(100,100);
		Iterator<EntityProtection> iterator = locks.values().iterator();
		while(iterator.hasNext()){
			list.add(iterator.next().save(entityConfig));
		}
		entityConfig.set("uuids", list);
	    try {
	    	entityConfig.save(entityConfigFile);
	    	jprotect.getLogger().info("Succesfully saved entitysaves.sav");
	    } catch (IOException ex) {
	        jprotect.getLogger().log(Level.SEVERE, "Could not save config to " + entityConfigFile, ex);
	    }
	}
	
}
