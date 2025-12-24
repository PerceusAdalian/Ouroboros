package com.ouroboros.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityMaterialClass;
import com.ouroboros.enums.AbilityType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.enums.StatType;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PrintUtils
{
	public static String ColorParser(String msg) 
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static void Print(String msg) 
	{
		Bukkit.getServer().getConsoleSender().sendMessage(ColorParser(msg));
	}
	
	public static void Print(Player player, String msg) 
	{
		player.getPlayer().sendMessage(ColorParser(msg));	
	}
	
	public static void Print(Player player, String...msg) 
	{
		for (String line : msg) 
			player.getPlayer().sendMessage(ColorParser(line));
	}
	public static String setCost(int cost) 
	{
		return ColorParser("&r&fCost: " + cost + "&e₪&f");
	}
	public static void PrintToActionBar(Player player, String msg) 
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorParser(msg)));
	}
	
	public static void OBSFormatPrint(Player player, String msg, @Nullable String callbackMsg) 
	{
		Print(player, "&f|&eΩ&r&f| "+msg+" &r&f/&e$&f/"+(callbackMsg!=null?callbackMsg:"")+"/");
	}
	
	public static void OBSFormatPrint(Player player, String msg) 
	{
		Print(player, "&f|&eΩ&r&f| "+msg+" &r&f/&e$&f//");
	}
	
	public static void OBSFormatError(Player player, String msg) 
	{
		Print(player, "&f|&cΩ&r&f| "+msg+" &r&f/&c!&f//");
	}
	
	public static void OBSFormatError(Player player, String msg, @Nullable String callbackMsg) 
	{
		Print(player, "&f|&cΩ&r&f| "+msg+" &r&f/&c!&f/"+(callbackMsg!=null?callbackMsg:"")+"/");
	}
	
	public static void OBSFormatDebug(Player player, String msg) 
	{
		Print(player.getPlayer(), "&f|&bΩ&r&f| "+msg+ " &r&f/&b?&f//");
	}
	
	public static void OBSFormatDebug(Player player, String msg, @Nullable String callbackMsg) 
	{
		Print(player.getPlayer(), "&f|&bΩ&r&f| "+msg+ " &r&f/&b?&f/"+(callbackMsg!=null?callbackMsg:"")+"/");
	}
	
	public static void OBSConsolePrint(String msg)
	{
		Print("&f|&eΩ&r&f| "+msg+" &r&f/&e$&f//");
	}
	
	public static void OBSConsolePrint(String...msg) 
	{
		Print("&f|&eΩ&r&f| Multiline Message: \n");
		for (String line : msg)
			Bukkit.getConsoleSender().sendMessage(ColorParser(line));
		Print("&r&f\nEND -- &aOK&f /&e$&f//");
	}
	
	public static void OBSConsoleError(String msg) 
	{
		Print("&f|&cΩ&r&f| "+msg+" &r&f/&c!&f//");
	}
	
	public static void OBSConsoleDebug(String msg) 
	{
		Print("&f|&bΩ&r&f| "+msg+ " &r&f/&b?&f//");
	}
	
	public static String printNextLevelXP(UUID uuid, StatType sType) 
	{
		int level = PlayerData.getPlayer(uuid).getStat(sType, true);
		if (level == 100) 
		{
			return ColorParser("&r&7<&b&oPRESTIGE READY&r&7>");
		}
		return ColorParser(""+PlayerData.getNextLevelXP(uuid, sType));
	}
	
	public static String printStatType(StatType sType) 
	{
		char[] chars = sType.getKey().toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}
	
	public static String assignAbilityType(AbilityType type1, AbilityType type2) 
	{
		return ColorParser("&r&f&lAbility Type&r&f: {"+ type1.getAbilityType() + "&r&f | " + type2.getAbilityType() + "&r&f}");
	}
	
	public static String assignAbilityType(AbilityType type) 
	{
		return ColorParser("&r&f&lAbility Type&r&f: {"+type.getAbilityType()+"&r&f}");
	}
	
	public static String assignCastCondition(CastConditions condition)
	{
		return ColorParser("&r&f&lCast Condition&r&f: {"+condition.getKey()+"&r&f}");
	}
	
	public static String assignAbilityCategory(AbilityMaterialClass category)
	{
		boolean isAnyCategory = category.equals(AbilityMaterialClass.ANY) ? true : false;
		String printStr = isAnyCategory ? "&r&f&lCast Category&r&f: &b&oAny&r&f" : "&r&f&lCast Category: "+category.getKey()+" only.";
		return ColorParser(printStr);
	}
	
	public static String assignElementType(SpellementType type) 
	{
		return ColorParser("&r&f&lElement Type&r&f: {"+type.getType()+"&r&f}");
	}
	
	public static String assignElementType(ElementType type) 
	{
		return ColorParser("&r&f&lDamage Type&r&f: {"+type.getType()+"&r&f}");
	}
	
	public static char getElementTypeColor(SpellementType spellementType)
	{
		char ch = switch(spellementType)
		{
			case INFERNO -> ch = 'c';
			case AERO -> ch = 'd';
			case CELESTIO -> ch = 'e';
			case COSMO -> ch = '3';
			case GEO -> ch = '6';
			case GLACIO -> ch = 'b';
			case HERESIO -> ch = '2';
			case MORTIO -> ch = '4';
			default -> ch = '7';
		};
		return ch;
	}
	
	public static String assignAuthor(SpellementType spellementType)
	{
		String author = switch(spellementType)
		{
			case INFERNO -> author = ColorParser("&c&lAighil&r&f, the Gentle");
			case AERO -> author = ColorParser("&d&lSeth&r&f, the Looming Storm");
			case CELESTIO -> author = ColorParser("&e&lLumina&r&f, High Priestess");
			case COSMO -> author = ColorParser("&3&koo &r&3&lE&ko&r&3&lrr &r&3&ko &r&3&lor&r&3&ko");
			case GEO -> author = ColorParser("&6&lHaephestus&r&f, Emanator of &6&lNidus");
			case GLACIO -> author = ColorParser("&b&lBjorn&r&f, Ice Giant");
			case HERESIO -> author = ColorParser("&2&lSnape&r&f, the 'Mortal Alchemist'");
			case MORTIO -> author = ColorParser("&4&lGeneral Falric&r&f, Death's Righthand Man");
			default -> author = "&7&oUknown";
		};
		return author;
	}
	
	public static String assignSpellType(SpellType type)
	{
		return ColorParser("&r&f&lSpell Type&r&f: {"+type.getSpellType()+"&r&f}");
	}
	
	public static String assignSpellType(SpellType type1, SpellType type2)
	{
		return ColorParser("&r&f&lSpell Type&r&f: {"+ type1.getSpellType() + "&r&f | " + type2.getSpellType() + "&r&f}");
	}
	
	public static String getFancyEntityName(EntityType eType)
	{
		String inherentName = eType.toString().toLowerCase();
		String[] splitName = inherentName.split("_");
		inherentName = "";
		for (String s : splitName) 
		{
			char[] chars = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			inherentName += new String(chars);
			inherentName += " ";
		}
		inherentName = inherentName.substring(0, inherentName.length() - 1);
		return inherentName;
	}
	
	public static String assignRarity(Rarity rarity)
	{
		char color = switch (rarity) 
		{
			case ONE -> color = '7';
			case TWO -> color = '6';
			case THREE -> color = 'b';
			case FOUR -> color = 'd';
			case FIVE -> color = 'e';
			case SIX -> color = 'c';
			case SEVEN -> color = '3';
			case NONE -> throw new UnsupportedOperationException("Unimplemented case: " + rarity);
			default -> throw new IllegalArgumentException("Unexpected value: " + rarity);	
		};
		
		return ColorParser("&r&f&nRarity&r&f: &"+color+("✦".repeat(rarity.getRarity())));
	}
}
