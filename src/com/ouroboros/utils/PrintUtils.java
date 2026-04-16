package com.ouroboros.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityMaterialClass;
import com.ouroboros.enums.AbilityType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.enums.StatType;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class PrintUtils
{
	public static String parseHex(String msg)
	{
	    Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
	    Matcher matcher = hexPattern.matcher(msg);
	    StringBuffer buffer = new StringBuffer();

	    while (matcher.find())
	    {
	        String hex = matcher.group(1);
	        StringBuilder replacement = new StringBuilder("§x");
	        for (char c : hex.toCharArray())
	            replacement.append('§').append(c);
	        matcher.appendReplacement(buffer, replacement.toString());
	    }
	    matcher.appendTail(buffer);
	    return buffer.toString();
	}

	public static String ColorParser(String msg) 
	{
	    return ChatColor.translateAlternateColorCodes('&', parseHex(msg));
	}
	
	public static String color(ObsColors color)
	{
	    return "&#" + color.getColor().replace("#", "");
	}
	
	public static String coloredMessage(ObsColors color, String msg)
	{
	    return ColorParser(color(color) + msg);
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
	
	public static void PrintToActionBarLegacy(Player player, String msg) 
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorParser(msg)));
	}
	
	public static void PrintToActionBar(Player player, String msg)
	{
	    String parsed = ColorParser(msg);
	    BaseComponent[] components = buildComponents(parsed);
	    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
	}

	private static BaseComponent[] buildComponents(String msg)
	{
	    List<BaseComponent> components = new ArrayList<>();
	    Pattern pattern = Pattern.compile("§x(§[0-9a-fA-F]){6}|§[0-9a-fA-Fk-orK-OR]");
	    Matcher matcher = pattern.matcher(msg);

	    int lastEnd = 0;
	    net.md_5.bungee.api.ChatColor currentColor = net.md_5.bungee.api.ChatColor.WHITE;

	    while (matcher.find())
	    {
	        if (matcher.start() > lastEnd)
	        {
	            String segment = msg.substring(lastEnd, matcher.start());
	            if (!segment.isEmpty())
	            {
	                TextComponent component = new TextComponent(segment);
	                component.setColor(currentColor);
	                components.add(component);
	            }
	        }

	        String code = matcher.group();
	        if (code.startsWith("§x"))
	        {
	            // Extract only the 6 hex digit characters from §x§R§R§G§G§B§B
	            StringBuilder hex = new StringBuilder("#");
	            for (int i = 0; i < code.length() - 1; i++)
	                if (code.charAt(i) == '§' && code.charAt(i + 1) != 'x')
	                    hex.append(code.charAt(i + 1));
	            currentColor = net.md_5.bungee.api.ChatColor.of(hex.toString());
	        }
	        else
	        {
	            char c = code.charAt(1);
	            // Ignore reset and formatting codes rather than setting color to null
	            if (Character.toString(c).matches("[0-9a-fA-F]"))
	                currentColor = net.md_5.bungee.api.ChatColor.getByChar(c);
	        }

	        lastEnd = matcher.end();
	    }

	    if (lastEnd < msg.length())
	    {
	        String tail = msg.substring(lastEnd);
	        if (!tail.isEmpty())
	        {
	            TextComponent component = new TextComponent(tail);
	            component.setColor(currentColor);
	            components.add(component);
	        }
	    }

	    return components.toArray(new BaseComponent[0]);
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
		return ColorParser("&r&f&lElement Type&r&f: {&r&f"+getElementTypeColor(type)+type.getType()+"&r&f}");
	}
	
	public static String assignElementType(ElementType type) 
	{
		return ColorParser("&r&f&lDamage Type&r&f: {&r&f"+getElementTypeColor(type)+type.getType()+"&r&f}");
	}
	
	public static String getElementTypeColor(SpellementType spellementType)
	{
	    return switch (spellementType)
	    {
	        case INFERNO  -> color(ObsColors.INFERNO);
	        case AERO     -> color(ObsColors.AERO);
	        case CELESTIO -> color(ObsColors.CELESTIO);
	        case COSMO    -> color(ObsColors.COSMO);
	        case GEO      -> color(ObsColors.GEO);
	        case GLACIO   -> color(ObsColors.GLACIO);
	        case HERESIO  -> color(ObsColors.HERESIO);
	        case MORTIO   -> color(ObsColors.MORTIO);
	        case ARCANO   -> color(ObsColors.ARCANO);
	        case ASTRAL   -> color(ObsColors.ASTRAL);
	        default       -> color(ObsColors.NULL);
	    };
	}
	
	public static String getElementTypeColor(ElementType elementType)
	{
	    return switch (elementType)
	    {
	        case INFERNO  -> color(ObsColors.INFERNO);
	        case AERO     -> color(ObsColors.AERO);
	        case CELESTIO -> color(ObsColors.CELESTIO);
	        case COSMO    -> color(ObsColors.COSMO);
	        case GEO      -> color(ObsColors.GEO);
	        case GLACIO   -> color(ObsColors.GLACIO);
	        case HERESIO  -> color(ObsColors.HERESIO);
	        case MORTIO   -> color(ObsColors.MORTIO);
	        case ARCANO   -> color(ObsColors.ARCANO);
	        default       -> color(ObsColors.NULL);
	    };
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
	
	public static String assignPVPCompatible()
	{
		return ColorParser("&r&7{ &c⚔&l PVP &r&c⚔ &7}");
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
	
	public static String formatEnumName(String string) 
	{
		if (string.contains(".")) 
		{
			string = string.substring(string.lastIndexOf(".") + 1);
		} 
		else if (string.contains(":")) 
		{
			string = string.substring(string.lastIndexOf(":") + 1);
		}
		
		String[] words = string.split("_");
		StringBuilder result = new StringBuilder();
		
		for (String word : words) 
		{
			if (result.length() > 0) result.append(" ");
			result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase());
		}
		
		return result.toString();
	}
	
	public static String rarityToString(Rarity rarity)
	{
		char color = getRarityColor(rarity);
		return ColorParser("&"+color+("✦".repeat(rarity.getRarity())));
	}
	
	public static String assignRarity(Rarity rarity)
	{
		char color = getRarityColor(rarity);
		return ColorParser("&r&f&lRarity&r&f: &"+color+("✦".repeat(rarity.getRarity())));
	}
	
	public static String assignRarity(Rarity rarity, boolean forBook)
	{
	    char color = getRarityColor(rarity);
	    String prefix = forBook ? "§" : "&";
	    String textColor = forBook ? "0" : "f";
	    return prefix + "r" + prefix + textColor + prefix + "nRarity" + prefix + "r" + prefix + textColor + ": " + prefix + color + ("✦".repeat(rarity.getRarity()));
	}
	
	public static String toBookSafe(String msg)
	{
	    Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
	    Matcher matcher = hexPattern.matcher(msg);
	    StringBuffer buffer = new StringBuffer();

	    while (matcher.find())
	    {
	        // Convert to nearest ChatColor
	        Color color = Color.decode("#" + matcher.group(1));
	        ChatColor nearest = nearestLegacyColor(color);
	        matcher.appendReplacement(buffer, nearest.toString());
	    }
	    matcher.appendTail(buffer);
	    return buffer.toString().replace("&", "§");
	}

	private static final Map<ChatColor, Color> LEGACY_COLORS = new EnumMap<>(ChatColor.class);
	static
	{
	    LEGACY_COLORS.put(ChatColor.BLACK,        new java.awt.Color(0,   0,   0  ));
	    LEGACY_COLORS.put(ChatColor.DARK_BLUE,    new java.awt.Color(0,   0,   170));
	    LEGACY_COLORS.put(ChatColor.DARK_GREEN,   new java.awt.Color(0,   170, 0  ));
	    LEGACY_COLORS.put(ChatColor.DARK_AQUA,    new java.awt.Color(0,   170, 170));
	    LEGACY_COLORS.put(ChatColor.DARK_RED,     new java.awt.Color(170, 0,   0  ));
	    LEGACY_COLORS.put(ChatColor.DARK_PURPLE,  new java.awt.Color(170, 0,   170));
	    LEGACY_COLORS.put(ChatColor.GOLD,         new java.awt.Color(255, 170, 0  ));
	    LEGACY_COLORS.put(ChatColor.GRAY,         new java.awt.Color(170, 170, 170));
	    LEGACY_COLORS.put(ChatColor.DARK_GRAY,    new java.awt.Color(85,  85,  85 ));
	    LEGACY_COLORS.put(ChatColor.BLUE,         new java.awt.Color(85,  85,  255));
	    LEGACY_COLORS.put(ChatColor.GREEN,        new java.awt.Color(85,  255, 85 ));
	    LEGACY_COLORS.put(ChatColor.AQUA,         new java.awt.Color(85,  255, 255));
	    LEGACY_COLORS.put(ChatColor.RED,          new java.awt.Color(255, 85,  85 ));
	    LEGACY_COLORS.put(ChatColor.LIGHT_PURPLE, new java.awt.Color(255, 85,  255));
	    LEGACY_COLORS.put(ChatColor.YELLOW,       new java.awt.Color(255, 255, 85 ));
	    LEGACY_COLORS.put(ChatColor.WHITE,        new java.awt.Color(255, 255, 255));
	}

	private static ChatColor nearestLegacyColor(java.awt.Color color)
	{
	    ChatColor nearest = ChatColor.WHITE;
	    double minDist = Double.MAX_VALUE;
	    for (Map.Entry<ChatColor, java.awt.Color> entry : LEGACY_COLORS.entrySet())
	    {
	        java.awt.Color c = entry.getValue();
	        double dist = Math.pow(c.getRed()   - color.getRed(),   2)
	                    + Math.pow(c.getGreen() - color.getGreen(), 2)
	                    + Math.pow(c.getBlue()  - color.getBlue(),  2);
	        if (dist < minDist) { minDist = dist; nearest = entry.getKey(); }
	    }
	    return nearest;
	}
	
	public static String assignObfuscatedRarity()
	{
		return ColorParser("&r&f&lRarity&r&f: &7&oUnrefined");
	}
	
	public static String assignMateriaType(MateriaType type)
	{
		return ColorParser("&r&bType&f: &7{&r&f"+formatEnumName(type.getKey())+"&r&7}");
	}
	
	public static String assignMateriaState(MateriaState state)
	{
		return ColorParser("&r&bState&f: &7{&r&f"+state.getState()+"&r&7}");
	}
	
	public static String assignMateriaComponent(MateriaComponent component)
	{
		return ColorParser("&r&bComponent&f: &7{&r&f"+component.getLabel()+"&r&7}");
	}
	
	public static String getRarityAsNumeralValue(Rarity rarity)
	{
		String numeral = switch (rarity)
		{
			case NONE -> "0";
			case ONE -> "I";
			case TWO -> "II";
			case THREE -> "III";
			case FOUR -> "IV";
			case FIVE -> "V";
			case SIX -> "VI";
			case SEVEN -> "VII";
			default -> "I";
		};
				
		return "&" + getRarityColor(rarity) + numeral;
	}
	
	public static char getRarityColor(Rarity rarity)
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
			case NONE -> color = '7';
			default -> color = '7';
		};
		
		return color;
	}
	
	public static String toTitleCase(String input)
	{
		return Arrays.stream(input.split("_"))
					 .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
					 .collect(Collectors.joining(" "));
	}
	
	public static String assignAstralVariant(String name, boolean asDaybound)
	{
		return color(ObsColors.ASTRAL) + "&l" + name + (asDaybound ? " &r&eDaybound&r&f:":" &r&9Nightbound&r&f:");
	}
}
