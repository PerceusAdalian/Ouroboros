package com.eol.enums;

import org.bukkit.Material;

public enum MateriaType 
{
	WOOD("wooden",         Material.OAK_BUTTON),
	STONE("stone",         Material.STONE_BUTTON),
	IRON("iron",           Material.IRON_NUGGET),
	COPPER("copper",       Material.COPPER_NUGGET),
	GOLD("golden",         Material.GOLD_NUGGET),
	DIAMOND("diamond",     Material.DIAMOND),
	NETHERITE("netherite", Material.NETHERITE_SCRAP),

	STRING("string",       Material.STRING),
	LEATHER("leather",     Material.LEATHER),

	CATALYST("catalyst",   Material.NETHER_STAR),

	CELESTIO("celestio",   Material.GHAST_TEAR),
	MORTIO("mortio",       Material.BONE),
	INFERNO("inferno",     Material.BLAZE_POWDER),
	GLACIO("glacio",       Material.PRISMARINE_CRYSTALS),
	GEO("geo",             Material.BRICK),
	AERO("aero",           Material.AMETHYST_SHARD),
	COSMO("cosmo",         Material.ECHO_SHARD),
	HERESIO("heresio",     Material.ENDER_EYE);

	private final String materiaCategory;
	private final Material material;

	MateriaType(String materiaCategory, Material material)
	{
		this.materiaCategory = materiaCategory;
		this.material = material;
	}

	public String getMateriaCategory()
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
}