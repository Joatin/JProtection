package com.hotmail.joatin37.jprotection.eventhandlers;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.hotmail.joatin37.jprotection.JProtection;

public class PlayerInteractEntityEventHandler {
	
	private JProtection jprotect;

	public PlayerInteractEntityEventHandler(JProtection jProtection) {
		jprotect=jProtection;
	}

	public void Handle(PlayerInteractEntityEvent event) {
		if(jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand()==null){
			if(!jprotect.entitylock.allowsInteraction(event.getRightClicked(), event.getPlayer())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(jprotect.getConfig().getString("messages.youdonthavepermissiontointeractwiththisentity", "§4You don't have permision to interact with this [entity]!").replace("[entity]", event.getRightClicked().getType().getName()));
			}
		}else{
			switch(jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand()){
			case "lock": jprotect.entitylock.lock(event.getRightClicked(), event.getPlayer());
				break;
			case "info":
				jprotect.entitylock.getInfo(event.getRightClicked(), event.getPlayer());
				break;
			case "unlock":
				jprotect.entitylock.unlock(event.getRightClicked(), event.getPlayer());
				break;
			default: 
				String[] s = jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand().split(":");
				if(s[0].equals("lock")){
					jprotect.entitylock.lock(event.getRightClicked(), event.getPlayer(), s[1]);
				}
				if(s[0].equals("toggle")){
					
				}
			}
			event.setCancelled(true);
			if(!jprotect.getPlayerHandle(event.getPlayer().getName()).isPersist()){
				jprotect.getPlayerHandle(event.getPlayer().getName()).setCurrentcommand(null);
			}
		}
	}

}
