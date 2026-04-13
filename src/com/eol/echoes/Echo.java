package com.eol.echoes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public class Echo 
{
	private static final Map<String, Echo> echo_registry = new HashMap<>();
	
	private static final NamespacedKey echoKey = new NamespacedKey(Ouroboros.instance, "echo_identifier");
	
	private String name;
	private String internalName;
	private String[] description;
	
	private Material mType;
	private EchoForm form;
	private EchoMaterial echoMaterial;
	private Rarity rarity;
	private ElementType eType;
	
	private double attack;
	private double attackRating;
	private double critRate;
	private double critModifier;
	
	private int[] modifier_slots;
	
	public Echo(String name, String internalName, Material mType, Rarity rarity, @Nullable ElementType eType, double attack, double attackRating, double critRate, double critModifier)
	{
		this.name = name;
		this.internalName = internalName;
		this.mType = mType;
		this.rarity = rarity;
		this.eType = eType;
		this.attack = attack;
		this.attackRating = attackRating;
		this.critRate = critRate;
		this.critModifier = critModifier;
	}
	
	public Echo(EchoForm form, EchoMaterial echoMaterial, Rarity rarity, @Nullable ElementType eType, double attack, double attackRating, double critRate, double critModifier)
	{
		this.form = form;
		this.echoMaterial = echoMaterial;
		this.rarity = rarity;
		this.eType = eType;
		this.attack = attack;
		this.attackRating = attackRating;
		this.critRate = critRate;
		this.critModifier = critModifier;
	}

	protected static NamespacedKey getEchokey() 
	{
		return echoKey;
	}

	protected String getName() 
	{
		return name;
	}

	protected String getInternalName() 
	{
		return internalName;
	}
	
	public String getInternalNameAsID()
	{
		int hash = internalName.hashCode() & 0xFFFF;
		return Integer.toHexString(hash).toUpperCase();
	}
	
	public List<String> getLore()
	{
		List<String> lore = new ArrayList<>();
		for (String line : description)
			lore.add(PrintUtils.ColorParser(line));
		return lore;
	}

	protected Material getmType() 
	{
		return mType;
	}

	protected EchoForm getForm() 
	{
		return form;
	}

	protected EchoMaterial getEchoMaterial() 
	{
		return echoMaterial;
	}

	protected Rarity getRarity() 
	{
		return rarity;
	}

	protected ElementType geteType() 
	{
		return eType;
	}

	protected double getAttack() 
	{
		return attack;
	}

	protected double getAttackRating() 
	{
		return attackRating;
	}

	protected double getCritRate() 
	{
		return critRate;
	}

	protected double getCritModifier() 
	{
		return critModifier;
	}

	protected int[] getModifier_slots() 
	{
		return modifier_slots;
	}
	
	public static void register(Echo echo)
	{
		echo_registry.put(echo.getInternalName(), echo);
	}
	
	public static Echo get(String internalName)
	{
		return echo_registry.getOrDefault(internalName, null);
	}
	
	public static ItemStack build()
	{
		return null; // @ TODO
	}
}
