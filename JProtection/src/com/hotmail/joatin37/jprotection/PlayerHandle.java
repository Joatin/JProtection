package com.hotmail.joatin37.jprotection;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.configuration.file.FileConfiguration;

public class PlayerHandle {
	
	public enum ProtectionLv{
		PRIVATE, PUBLIC;
	}

	private String playername;
	
	private HashMap<String, Boolean>blockautolock;
	private HashMap<String, Boolean>blockprotection;
	private HashMap<String, Boolean>entityautolock;
	private HashMap<String, Boolean>entityprotection;
	private List <String>friends;
	private boolean lockondeath;
	
	private boolean persist;
	private String currentcommand;
	private int amountchests;
	private int amountlocks;

	
	
	public PlayerHandle(String playername,
	 						HashMap<String, Boolean>blockautolock,
	 						HashMap<String, Boolean>blockprotection,
	 						HashMap<String, Boolean>entityautolock,
	 						HashMap<String, Boolean>entityprotection,
	 						List<String>friends,
	 						boolean lockondeath
			){
		this.playername=playername;
		this.blockautolock=blockautolock;
		this.blockprotection=blockprotection;
		this.entityautolock=entityautolock;
		this.entityprotection=entityprotection;
		this.lockondeath=lockondeath;
		
		this.friends=friends;
	}
	
	public String save(FileConfiguration config){
		config.createSection(playername+".blockautolock", blockautolock);
		config.createSection(playername+".blockprotection", blockprotection);
		config.createSection(playername+".entityautolock", entityautolock);
		config.createSection(playername+".entityprotection", entityprotection);
		config.set(playername+".friends", friends);		
		config.set(playername+".lockondeath", lockondeath);
		return playername;
	}
	
	public String getName(){
		return playername;
	}
	
	public int getAmountFriends(){
		return friends.size();
	}
	
	public List<String> getFrienList(){
		return friends;
	}
	
	public void addFriend(String name){
		if(!friends.contains(name)){
			friends.add(name);
		}
	}
	
	public boolean getBlockAutoLock(int id){
		return blockautolock.get(Integer.toString(id));
	}
	
	public void setBlockAutoLock(int id, boolean bool){
		blockautolock.put(Integer.toString(id), bool);
	}
	
	public boolean getBlockProtection(int id){
		return blockprotection.get(Integer.toString(id));
	}
	
	public void setBlockProtection(int id, boolean bool){
		blockprotection.put(Integer.toString(id), bool);
	}
	
	public boolean getEntityAutoLock(int id){
		return entityautolock.get(Integer.toString(id));
	}
	
	public void setEntityAutoLock(int id, boolean bool){
		entityautolock.put(Integer.toString(id), bool);
	}
	
	public boolean getEntityProtection(int id){
		return entityprotection.get(Integer.toString(id));
	}
	
	public void setEntityProtection(int id, boolean bool){
		entityprotection.put(Integer.toString(id), bool);
	}

	public boolean isPersist() {
		return persist;
	}

	public void setPersist(boolean persist) {
		this.persist = persist;
	}

	public String getCurrentcommand() {
		return currentcommand;
	}

	public void setCurrentcommand(String currentcommand) {
		this.currentcommand = currentcommand;
	}

	public int getAmountlocks() {
		return amountlocks;
	}

	public void addAmountlocks() {
		this.amountlocks++;
	}
	
	public void removeAmountlocks() {
		this.amountlocks--;
	}

	public int getAmountchests() {
		return amountchests;
	}

	public void addAmountchests() {
		this.amountchests++;;
	}
	
	public void removeAmountchests() {
		this.amountchests--;;
	}
}
