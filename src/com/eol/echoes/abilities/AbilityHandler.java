package com.eol.echoes.abilities;

import org.bukkit.configuration.file.YamlConfiguration;

public class AbilityHandler 
{
    private final EchoAbility ability;
    private final YamlConfiguration config;
   
    public AbilityHandler(EchoAbility ability, YamlConfiguration config) 
    {
        this.ability = ability;
        this.config = config;
    }

    private String path(String s) 
    {
    	String pathAlpha = switch(ability.getAbilityType()) 
    	{
	    	case COMBAT -> pathAlpha = "combat_ability.";
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
    
    public EchoAbility getInstance() 
    {
        return ability;
    }
    
}
