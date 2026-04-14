package com.eol.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Material;

import com.ouroboros.utils.PrintUtils;

public enum MateriaType 
{
	WOOD("wooden_chunk",         Material.OAK_BUTTON),
	STONE("stone_chunk",         Material.STONE_BUTTON),
	IRON("iron_slag",           Material.IRON_NUGGET),
	COPPER("copper_nugget",       Material.COPPER_NUGGET),
	GOLD("gold_nugget",         Material.GOLD_NUGGET),
	DIAMOND("diamond",     Material.DIAMOND),
	NETHERITE("netherite_scrap", Material.NETHERITE_SCRAP),

	STRING("string",       Material.STRING),
	PELT("pelt",           Material.RABBIT_HIDE),
	LEATHER("leather",     Material.LEATHER),

	CATALYST("catalyst",   Material.NETHER_STAR),

	CELESTIO("celestio",   Material.END_CRYSTAL),
	MORTIO("mortio",       Material.WITHER_SKELETON_SKULL),
	INFERNO("inferno",     Material.BLAZE_ROD),
	GLACIO("glacio",       Material.NAUTILUS_SHELL),
	GEO("geo",             Material.RESIN_CLUMP),
	AERO("aero",           Material.AMETHYST_SHARD),
	COSMO("cosmo",         Material.ECHO_SHARD),
	HERESIO("heresio",     Material.TOTEM_OF_UNDYING);

	private final String materiaCategory;
	private final Material material;

	MateriaType(String materiaCategory, Material material)
	{
		this.materiaCategory = materiaCategory;
		this.material = material;
	}

	public String getName()
	{
		return materiaCategory;
	}

	public Material getMaterial()
	{
		return material;
	}

	public String getKey()
	{
		return this.name();
	}
	
	public static MateriaType fromString(String name)
	{
		try 
		{
			return MateriaType.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException e) 
		{
			return null;
		}
	}

	public static MateriaType fromMaterial(Material material)
	{
	    for (MateriaType type : values())
	    {
	        if (type.getMaterial() == material)
	            return type;
	    }
	    return null;
	}
	
	public static List<String> getAllKeys()
	{
		return Arrays.stream(MateriaType.values())
				.filter(m -> m != CATALYST && m != CELESTIO && m != MORTIO && m != INFERNO && m != GLACIO && m != GEO && m != AERO && m != COSMO && m != HERESIO)
				.map(MateriaType::getKey)
				.collect(Collectors.toList());
	}

	public String getLabel()
	{
		return PrintUtils.formatEnumName(getName());
	}
	
	public static final Set<Material> exemptMaterials = Set.of(
			Material.NETHER_STAR, Material.ECHO_SHARD, Material.END_CRYSTAL,
			Material.WITHER_SKELETON_SKULL, Material.TOTEM_OF_UNDYING, Material.AMETHYST_SHARD, 
			Material.RESIN_CLUMP, Material.NAUTILUS_SHELL, Material.BLAZE_ROD);
}