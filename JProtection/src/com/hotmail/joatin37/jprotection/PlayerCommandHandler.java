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

import java.util.Iterator;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class PlayerCommandHandler {
	
	private JProtection jprotection;

	PlayerCommandHandler(JProtection jp){
		jprotection = jp;
	}
	
	public void onCommand(Player player, Command cmd, String label, String[] args){
		switch(args[0].toLowerCase()){
		case "lock":if(args.length==1){
			if(jprotection.getPlayerHandle(player.getName()).getAmountlocks()>jprotection.getConfig().getInt("maxamountlocks")){
				player.sendMessage(jprotection.getConfig().getString("messages.youhaveexededmaxamount", "§4You have alread exeded the max amount"));
			}else{
				jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]);
				player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttolock", "§2Type the entity or block you wan't to lock"));
			}
		}else{
			if(args.length==2){
				jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]+":"+args[1]);
				player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttolocktoplayer", "§2Type the entity or block you wan't to lock to player [player]").replace("[player]", args[1]));
				if(jprotection.getServer().getPlayer(args[1])==null){
					player.sendMessage(jprotection.getConfig().getString("messages.notethisplayerisnotonline", "§4Note! This player is not online"));
				}
			}else{
				player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
			}
		}
			
			break;
		case "unlock":if(args.length==1){
			jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]);
			player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttounlock", "§2Type the entity or block you wan't to unlock"));
		}else{
			player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
		}
			break;
		case "toggle":if(args.length==1){
			player.sendMessage(jprotection.getConfig().getString("messages.tofewarguments", "§eYou have entered to few arguments, type §2/jprotection help§e, for help"));
		}else{
			if(args.length==2){
				if(args[1].equals("protected")){
				jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]+":"+args[1]);
				player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttotoggle", "§2Type the entity or block you wan't to toggle"));
				}else{
					player.sendMessage(jprotection.getConfig().getString("messages.unknowwcommand", "§eYou have entered a unknown command"));
				}
				}else{
				player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
			}
		}
			break;
		case "plugininfo":if(args.length==1){
			displayPluginInfo(player);
		}else{
			player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
		}
			break;
		case "me":if(args.length==1){
			displayMyInfo(player);
		}else{
			if(args.length==2){
				switch (args[1]){
				case "info": displayMyInfo(player);
					break;
				case "help": //TODO
					break;
				case "toggle": //TODO
					break;
				case "isauto": //TODO
					break;
				case "isprotect": //TODO
					break;
				
				default:
					player.sendMessage(jprotection.getConfig().getString("messages.unknowwcommand", "§eYou have entered a unknown command"));
					break;
				}
			}
		}
			break;
		case "add":if(args.length==1){
			player.sendMessage(jprotection.getConfig().getString("messages.tofewarguments", "§eYou have entered to few arguments, type §2/jprotection help§e, for help"));	
		}else{
			if(args.length==2){
				jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]+":"+args[1]);
				player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttoaddyourfriendto", "§2Type the entity or block you wan't to add your friend [friend] to").replace("[friend]", args[1]));
				if(jprotection.getServer().getPlayer(args[1])==null){
					player.sendMessage(jprotection.getConfig().getString("messages.notethisplayerisnotonline", "§4Note! This player is not online"));
				}
			}else{
				player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
			}
		}
			break;
		case "info":if(args.length==1){
			jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]);
			player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttogetinfoon", "§2Type the entity or block you wan't to get info on"));
		}else{
			player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
		}
			break;
		case "persist":
			if(jprotection.getPlayerHandle(player.getName()).isPersist()){
				jprotection.getPlayerHandle(player.getName()).setPersist(false);
				player.sendMessage(jprotection.getConfig().getString("messages.persistoff", "§eToggled persist mode §4OFF"));
			}else{
				jprotection.getPlayerHandle(player.getName()).setPersist(true);
				player.sendMessage(jprotection.getConfig().getString("messages.persiston", "§eToggled persist mode §2ON"));
			}
			break;
		case "setpassword":if(args.length==1){
			player.sendMessage(jprotection.getConfig().getString("messages.tofewarguments", "§eYou have entered to few arguments, type §2/jprotection help§e, for help"));	
		}else{
			if(args.length==2){
				jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]+":"+args[1]);
				player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttoaddpasswordto", "§2Type the entity or block you wan't to add a password to"));
				if(jprotection.getServer().getPlayer(args[1])==null){
					player.sendMessage(jprotection.getConfig().getString("messages.notethisplayerisnotonline", "§4Note! This player is not online"));
				}
			}else{
				player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
			}
		}
			break;
		case "removepassword":if(args.length==1){
			jprotection.getPlayerHandle(player.getName()).setCurrentcommand(args[0]);
			player.sendMessage(jprotection.getConfig().getString("messages.typethethingyouwanttoremovethepasswordfrom", "§2Type the entity or block you wan't to remove the password from"));
		}else{
			player.sendMessage(jprotection.getConfig().getString("messages.tomanyarguments", "§eYou have entered to many arguments, type §2/jprotection help§e, for help"));
		}
			break;
		case "cancel": jprotection.getPlayerHandle(player.getName()).setCurrentcommand(null);
		player.sendMessage(jprotection.getConfig().getString("messages.cancelledcommand", "§eCancelled your current command"));
		break;
		}
	}
	
	private void displayPluginInfo(Player player){
		
	}
	
	private void displayMyInfo(Player player){
		PlayerHandle handle = jprotection.getPlayerHandle(player.getName());
		String s = jprotection.getConfig().getString("messages.myinfofriendcolor", "§e");
		Iterator<String> iterator = handle.getFrienList().iterator();
		player.sendMessage(jprotection.getConfig().getString("messages.myinfo", "§4------/////§eMY INFO§4/////------"));
		player.sendMessage(jprotection.getConfig().getString("messages.myinfolocks", "§eYou have §4[numlocks]§e locks, and §4[numchests]§e of them are chests.").replace("[numlocks]", Integer.toString(handle.getAmountlocks()).replace("[numchests]", Integer.toString(handle.getAmountchests()))));
		player.sendMessage(jprotection.getConfig().getString("messages.myinfofriends", "§eAnd you have §4[numfriends]§e friends").replace("[numfriends]", Integer.toString(handle.getAmountFriends())));
		while(iterator.hasNext()){
			player.sendMessage(s+iterator.next());
		}
	}
}
