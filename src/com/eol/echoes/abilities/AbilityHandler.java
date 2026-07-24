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
	    	case OFFENSIVE -> pathAlpha = "offensive.";
	    	case DEFENSIVE -> pathAlpha = "defensive.";
	    	case SUPPORT -> pathAlpha = "support.";
	    	case UTILITY -> pathAlpha = "utility.";
	    	case CONTROL -> pathAlpha = "control.";
	    	case BUFF -> pathAlpha = "buff.";
	    	case DEBUFF -> pathAlpha = "debuff.";
	    	case ULTIMATE -> pathAlpha = "ultimate.";
	    	case SIGNATURE -> pathAlpha = "offensive.";
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
