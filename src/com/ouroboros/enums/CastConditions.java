package com.ouroboros.enums;

import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public enum CastConditions 
{
	RIGHT_CLICK_AIR,
	RIGHT_CLICK_BLOCK,
	LEFT_CLICK_AIR,
	LEFT_CLICK_BLOCK,
	SHIFT_RIGHT_CLICK_AIR,
	SHIFT_RIGHT_CLICK_BLOCK,
	SHIFT_LEFT_CLICK_AIR,
	SHIFT_LEFT_CLICK_BLOCK,
	PASSIVE
	;
	
	public String getKey()
	{
		String key = "";
		String[] splitKey = this.name().toLowerCase().split("_");
		for (String s : splitKey)
		{
			char[] chars = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			key += new String(chars);
			key += " ";
		}
		key = key.substring(0, key.length() - 1);
		return PrintUtils.ColorParser("&r&f&l"+key);
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
			case PASSIVE -> true;
		};
	}
	
}
