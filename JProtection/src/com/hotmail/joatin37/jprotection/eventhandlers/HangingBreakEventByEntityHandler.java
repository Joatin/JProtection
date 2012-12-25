package com.hotmail.joatin37.jprotection.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;

import com.hotmail.joatin37.jprotection.JProtection;

public class HangingBreakEventByEntityHandler {
	
	private JProtection jprotect;

	public HangingBreakEventByEntityHandler(JProtection jProtection) {
		jprotect=jProtection;
	}

	public void Handle(HangingBreakByEntityEvent event) {
		if(event.getRemover() instanceof Player){
			if(jprotect.entitylock.isLocked(event.getEntity())){
				if(!jprotect.entitylock.isOwner(event.getEntity(), ((Player)event.getRemover()).getName())){
					event.setCancelled(true);
					((Player)event.getRemover()).sendMessage(jprotect.getConfig().getString("messages.youarenotallowedtobreakthisentity", "§4You are not allowed to break this [entity]").replace("[entity]", event.getEntity().getType().getName()));
				}
			}
		}
		
	}

}
