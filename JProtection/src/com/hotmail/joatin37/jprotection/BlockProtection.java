package com.hotmail.joatin37.jprotection;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

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


}
