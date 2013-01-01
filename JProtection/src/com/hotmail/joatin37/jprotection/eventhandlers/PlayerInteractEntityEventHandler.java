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
			case "toggle":
				break;
			default: 
				String[] s = jprotect.getPlayerHandle(event.getPlayer().getName()).getCurrentcommand().split(":");
				if(s[0].equals("lock")){
					jprotect.entitylock.lock(event.getRightClicked(), event.getPlayer(), s[1]);
				}
				if(s[0].equals("toggle")){
					
				}
				if(s[0].equals("add")){
					jprotect.entitylock.addFriend(event.getRightClicked(), event.getPlayer(), s[1]);
				}
			}
			event.setCancelled(true);
			if(!jprotect.getPlayerHandle(event.getPlayer().getName()).isPersist()){
				jprotect.getPlayerHandle(event.getPlayer().getName()).setCurrentcommand(null);
			}
		}
	}

}
