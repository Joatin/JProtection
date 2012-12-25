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
