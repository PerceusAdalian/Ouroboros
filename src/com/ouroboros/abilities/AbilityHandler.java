package com.ouroboros.abilities;

import org.bukkit.configuration.file.YamlConfiguration;

import com.ouroboros.abilities.instances.AbstractOBSAbility;

public class AbilityHandler 
{
	
    private final AbstractOBSAbility ability;
    private final YamlConfiguration config;
    public AbilityHandler(AbstractOBSAbility ability, YamlConfiguration config) 
    {
        this.ability = ability;
        this.config = config;
    }

    private String path(String s) 
    {
    	String pathAlpha = switch(ability.getAbilityType()) 
    	{
	    	case COMBAT -> pathAlpha = "combat_ability.";
	    	case PERK -> pathAlpha = "perk.";
	    	case UTILITY -> pathAlpha = "utility.";
	    	case SPECIALABILITY -> pathAlpha = "special_ability.";
    	};
        return "abilities."+pathAlpha+ability.getInternalName()+s;
    }

    public boolean isRegistered() 
    {
        return config.getBoolean(path(".registered"), false);
    }

    public AbilityHandler setRegistered(boolean value) 
    {
        config.set(path(".registered"), value);
        return this;
    }
    
    public boolean isActive() 
    {
    	return config.getBoolean(path(".active"));
    }
    
    public AbilityHandler setActive(boolean value) 
    {
		config.set(path(".active"), value);
		return this;
    }
    
    public AbstractOBSAbility getAbility() 
    {
        return ability;
    }

}
