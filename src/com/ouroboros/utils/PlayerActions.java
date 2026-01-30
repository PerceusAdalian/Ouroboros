package com.ouroboros.utils;

import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerActions 
{
	public static boolean rightClickAir(PlayerInteractEvent e) 
	{	
		return e.getAction().equals(Action.RIGHT_CLICK_AIR);
	}
	
	public static boolean leftClickAir(PlayerInteractEvent e) 
	{
		return e.getAction().equals(Action.LEFT_CLICK_AIR);
	}
	
	public static boolean rightClickBlock(PlayerInteractEvent e) 
	{
		return e.getAction().equals(Action.RIGHT_CLICK_BLOCK);
	}
	
	public static boolean leftClickBlock(PlayerInteractEvent e) 
	{
		return e.getAction().equals(Action.LEFT_CLICK_BLOCK);
	}
	
	public static boolean shiftRightClickAir(PlayerInteractEvent e) 
	{	
		return e.getPlayer().isSneaking() && e.getAction().equals(Action.RIGHT_CLICK_AIR);
	}
	
	public static boolean shiftLeftClickAir(PlayerInteractEvent e) 
	{
		return e.getPlayer().isSneaking() && e.getAction().equals(Action.LEFT_CLICK_AIR);
	}
	
	public static boolean shiftRightClickBlock(PlayerInteractEvent e) 
	{
		return e.getPlayer().isSneaking() && e.getAction().equals(Action.RIGHT_CLICK_BLOCK);
	}
	
	public static boolean shiftLeftClickBlock(PlayerInteractEvent e) 
	{
		return e.getPlayer().isSneaking() && e.getAction().equals(Action.LEFT_CLICK_BLOCK);
	}
	
	public static boolean anyAction(PlayerInteractEvent e)
	{
		return e.getAction() != null && e.getAction() != Action.PHYSICAL;
	}
}