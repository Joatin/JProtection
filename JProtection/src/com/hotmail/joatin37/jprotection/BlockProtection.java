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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BlockProtection {

	private String owner;
	private Location loc;
	
	private String password;
	private boolean protect;
	
	private List<String>allowedusers;
	
	public BlockProtection(Block block, PlayerHandle handle) {
		owner=handle.getName();
		loc = block.getLocation();
		protect=handle.getBlockProtection(block.getTypeId());
		allowedusers=handle.getFrienList();
	}

	public BlockProtection(String locn, FileConfiguration config, JProtection jprotect) {
		owner=config.getString(locn+".owner");
		String[] s = locn.split(";");
		loc=new Location(jprotect.getServer().getWorld(s[0]) , Double.parseDouble(s[1]), Double.parseDouble(s[2]), Double.parseDouble(s[3]));
		password = config.getString(locn+".password");
		protect=config.getBoolean(locn+".protect");
		allowedusers=config.getStringList(locn+".allowedusers");
	}
	
	public boolean allowsInteraction(String player){
		if(owner.equals(player)||!protect||allowedusers.contains(player)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isProtected(){
		return protect;
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
		String s = loc.getWorld().getName()+";"+loc.getX()+";"+loc.getY()+";"+loc.getZ();
		config.set(s+".owner", owner);
		config.set(s+".password", password);
		config.set(s+".protect", protect);
		config.set(s+".allowedusers", allowedusers);
		
		return s;
	}
	public Location getLocation(){
		return loc;
	}

	public String getOwner() {
		return owner;
	}

	public List<String> getAllowedUsers() {
		return allowedusers;
	}


}
