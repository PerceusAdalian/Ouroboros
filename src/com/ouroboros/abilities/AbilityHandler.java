package com.ouroboros.abilities;

import org.bukkit.configuration.file.YamlConfiguration;

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
        return "abilities." + ability.getInternalName() + "." + s;
    }

    public boolean isRegistered() 
    {
        return config.getBoolean(path("registered"), false);
    }

    public AbilityHandler setRegistered(boolean value) 
    {
        config.set(path("registered"), value);
        return this;
    }

    public boolean isActive() {
        return config.getBoolean(path("active"), false);
    }

    public AbilityHandler setActive(boolean value) 
    {
        config.set(path("active"), value);
        return this;
    }

    public AbstractOBSAbility getAbility() 
    {
        return ability;
    }

}
