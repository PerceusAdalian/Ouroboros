package com.lol.spells;

import org.bukkit.configuration.file.YamlConfiguration;

import com.lol.spells.instances.Spell;

public class SpellHandler 
{
	private final Spell spell;
	private final YamlConfiguration config;
	
	public SpellHandler(Spell spell, YamlConfiguration config)
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
		};
		return "spells."+pathAlpha+spell.getInternalName()+s;
	}
	
	public boolean isRegistered()
	{
		return config.getBoolean(path(".registered"), false);
	}
	
	public SpellHandler setRegistered(boolean value)
	{
		config.set(path(".registered"), value);
		return this;
	}
	
	public Spell getInstance()
	{
		return spell;
	}
}
