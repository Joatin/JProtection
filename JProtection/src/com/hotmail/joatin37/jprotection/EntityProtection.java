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

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityProtection {

	private String owner;
	
	private UUID uuid;
	
	private String password;
	
	private boolean protect;
	
	private List<String> allowedusers;
	
	public EntityProtection (String uuid, FileConfiguration config){
		this.owner=config.getString(uuid+".owner");
		String[] s = uuid.split(";"); 
		this.uuid= new UUID(Long.parseLong(s[0]), Long.parseLong(s[1]));
		this.protect=config.getBoolean(uuid+".protect");
		this.allowedusers=config.getStringList(uuid+".allowedusers");
		if(allowedusers==null)allowedusers=new Vector<String>();
		this.password=config.getString(uuid+".password");
	}
	
	public boolean addFriend(Player player, String friend){
		if(player.getName().equals(owner)){
			if(!allowedusers.contains(friend)){
				allowedusers.add(friend);
			}
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isProtected(){
		return protect;
	}
	
	public List<String> getAllowedUsers(){
		return allowedusers;
	}
	
	public String getPassword(Player player){
		if(password!=null){
		if(player.getName().equals(owner)||player.hasPermission("jprotection.admin.lockmaster")){
			return password;
		}else{
			return "*****";
		}
		}else{
			return "None";
		}
	}
	
	public String save(FileConfiguration config){
		String s = uuid.getMostSignificantBits()+";"+uuid.getLeastSignificantBits();
		config.set(s+".owner", owner);
		config.set(s+".protect", protect);
		config.set(s+".allowedusers", allowedusers);
		config.set(s+".password", password);
		
		return s;
	}
	
	public EntityProtection(Entity entity, PlayerHandle player){
		owner=player.getName();
		uuid=entity.getUniqueId();
		protect = player.getEntityProtection(entity.getType().getTypeId());
		allowedusers = player.getFrienList();
	}
	public UUID getuuid(){
		return uuid;
	}
	
	public boolean hasPassword(){
		return password!=null;
	}
	
	public boolean setPassword(Player player, String pass){
		if(player.getName().equals(owner)){
			password=pass;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean enterPassword(Player player, String pass){
		if(password.equals(pass)){
			addAllowedUser(player.getName());
			return true;
		}else{
			return false;
		}
	}
	
	public String getOwner(){
		return owner;
	}
	public boolean allowInteraction(){
		return protect;
	}
	public void setAllowInteraction(Boolean bool){
		protect=bool;
	}
	public boolean isAllowedUser(String name){
		if(allowedusers.contains(name)||!protect||owner.equals(name)){
			return true;
		}else{
			return false;
		}
		
	}
	
	public void addAllowedUser(String name){
		if(!allowedusers.contains(name)){
			allowedusers.add(name);
		}
	}
	
	public void removeAllowedUser(String name){
		allowedusers.remove(name);
	}
}
