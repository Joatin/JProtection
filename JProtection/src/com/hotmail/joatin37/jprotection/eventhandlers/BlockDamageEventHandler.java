package com.hotmail.joatin37.jprotection.eventhandlers;

import org.bukkit.event.block.BlockDamageEvent;

import com.hotmail.joatin37.jprotection.JProtection;

public class BlockDamageEventHandler {
	
	private JProtection jprotect;

	public BlockDamageEventHandler(JProtection jProtection) {
		jprotect=jProtection;
	}

	public void Handle(BlockDamageEvent event) {
		if(jprotect.getConfig().getStringList("protection.blocks").contains(Integer.toString(event.getBlock().getTypeId()))){
			if(jprotect.blocklock.hasLock(event.getBlock())){
				if(!jprotect.blocklock.isOwner(event.getBlock(), event.getPlayer().getName())){
					event.getPlayer().sendMessage(jprotect.getConfig().getString("messages.youarenotallowedtodamagethisblock", "§4You are not allowed to damage this block!"));
				}
			}
		}
		
	}

}
