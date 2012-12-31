package com.hotmail.joatin37.jprotection.eventhandlers;

import org.bukkit.event.player.PlayerInteractEvent;

import com.hotmail.joatin37.jprotection.JProtection;

public class PlayerInteractEventHandler {
	
	private JProtection jprotect;
	
	public PlayerInteractEventHandler(JProtection jprotection){
		jprotect = jprotection;
	}

	public void Handle(PlayerInteractEvent event) {
		if(jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand()==null){
			if(!jprotect.blocklock.allowsInteraction(event.getClickedBlock(), event.getPlayer())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(jprotect.getConfig().getString("messages.youdonthavepermissiontointeractwiththisblock", "§4You don't have permision to interact with this block!"));
			}
		}else{
			switch(jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand()){
			
			case "lock": jprotect.blocklock.lock(event.getClickedBlock(), event.getPlayer());
				break;
			case "unlock": jprotect.blocklock.unlock(event.getClickedBlock(), event.getPlayer());
				break;
			case "info":jprotect.blocklock.getInfo(event.getClickedBlock(), event.getPlayer());
				break;
			default:
				String[] s = jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand().split(":");
				if(s[0].equals("lock")){
					jprotect.blocklock.lock(event.getClickedBlock(), event.getPlayer(), s[1]);
				}
				if(s[0].equals("add")){
					jprotect.blocklock.addFriend(event.getClickedBlock(), event.getPlayer(), s[1]);
				}
				break;
			
			}
			event.setCancelled(true);
			if(!jprotect.getPlayerHandle(event.getPlayer().getName()).isPersist()){
				jprotect.getPlayerHandle(event.getPlayer().getName()).setCurrentcommand(null);
			}
		}
		
	}

}
