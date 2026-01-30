package com.ouroboros.enums;

import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.utils.PlayerActions;

public enum CastConditions 
{
	RIGHT_CLICK_AIR("&d&oRight-Click Air"),
	RIGHT_CLICK_BLOCK("&d&oRight-Click Air"),
	LEFT_CLICK_AIR("&d&oLeft-Click Air"),
	LEFT_CLICK_BLOCK("&d&oLeft-Click Block"),
	SHIFT_RIGHT_CLICK_AIR("&d&oShift_Right-Click Air"),
	SHIFT_RIGHT_CLICK_BLOCK("&d&oShift_Right-Click Block"),
	SHIFT_LEFT_CLICK_AIR("&d&oShift_Left-Click Air"),
	SHIFT_LEFT_CLICK_BLOCK("&d&oShift_Left-Click Block"),
	MIXED("&d&oMixed"),
	PASSIVE("&ePassive Effect");
	
	private final String name;
	
	
	
	CastConditions(String name) 
	{
		this.name = name;
	}
	
	public String getKey()
	{
		return this.name;
	}
	
	public String getName()
	{
		return this.getName();
	}
	
	public static boolean isValidAction(PlayerInteractEvent e, CastConditions condition)
	{
		return switch(condition)
		{
			case LEFT_CLICK_AIR -> PlayerActions.leftClickAir(e);
			case LEFT_CLICK_BLOCK -> PlayerActions.leftClickBlock(e);
			case RIGHT_CLICK_AIR -> PlayerActions.rightClickAir(e);
			case RIGHT_CLICK_BLOCK -> PlayerActions.rightClickBlock(e);
			case SHIFT_LEFT_CLICK_AIR -> PlayerActions.shiftLeftClickAir(e);
			case SHIFT_LEFT_CLICK_BLOCK -> PlayerActions.shiftLeftClickBlock(e);
			case SHIFT_RIGHT_CLICK_AIR -> PlayerActions.shiftRightClickAir(e);
			case SHIFT_RIGHT_CLICK_BLOCK -> PlayerActions.shiftRightClickBlock(e);
			case MIXED -> PlayerActions.anyAction(e);
			case PASSIVE -> true;
		};
	}
	
}
