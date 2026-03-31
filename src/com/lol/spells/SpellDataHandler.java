package com.lol.spells;

import org.bukkit.configuration.file.YamlConfiguration;

import com.lol.spells.instances.Spell;

public class SpellDataHandler 
{
	private final Spell spell;
	private final YamlConfiguration config;
	
	public SpellDataHandler(Spell spell, YamlConfiguration config)
	{
		this.spell = spell;
		this.config = config;
	}
	
	private String path(String s)
	{
		String pathAlpha = switch(spell.getElementType())
		{
			case INFERNO -> pathAlpha = "inferno.";
			case GLACIO -> pathAlpha = "glacio.";
			case GEO -> pathAlpha = "geo.";
			case AERO -> pathAlpha = "aero.";
			case MORTIO -> pathAlpha = "mortio.";
			case CELESTIO -> pathAlpha = "celestio.";
			case COSMO -> pathAlpha = "cosmo.";
			case HERESIO -> pathAlpha = "heresio.";
			case ARCANO -> pathAlpha = "arcano.";
			case ASTRAL -> pathAlpha = "astral.";
			case NULL -> pathAlpha = "nulltype.";
			
		};
		return "spells."+pathAlpha+spell.getInternalName()+s;
	}
	
	public boolean isRegistered()
	{
		return config.getBoolean(path(".registered"), false);
	}
	
	public SpellDataHandler setRegistered(boolean value)
	{
		config.set(path(".registered"), value);
		return this;
	}
	
	public Spell getInstance()
	{
		return spell;
	}
}
