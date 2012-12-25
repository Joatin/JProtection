package com.hotmail.joatin37.jprotection.eventhandlers;

import org.bukkit.entity.EntityType;
import org.bukkit.event.hanging.HangingPlaceEvent;

import com.hotmail.joatin37.jprotection.JProtection;

public class HangingPlaceEventHandler {
	
	private JProtection jprotect;
	
	public HangingPlaceEventHandler (JProtection jprotection){
		jprotect=jprotection;
	}

	/**
	 * If the player has autolock turned on this function will pass the entity to the lock class.
	 * 
	 * @param event
	 * 
	 * @since 1.0.0
	 */
	public void Handle(HangingPlaceEvent event){
		if(jprotect.getPlayerHandle(event.getPlayer().getName()).getEntityAutoLock(event.getEntity().getType().getTypeId())){
			jprotect.entitylock.lock(event.getEntity(), event.getPlayer());
		}
	}
}
