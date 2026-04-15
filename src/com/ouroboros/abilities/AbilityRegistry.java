package com.ouroboros.abilities;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ouroboros.abilities.instances.ObsAbility;
import com.ouroboros.abilities.instances.combat.Flamelash;
import com.ouroboros.abilities.instances.combat.GeminiSlash;
import com.ouroboros.abilities.instances.combat.ImbueFire;
import com.ouroboros.abilities.instances.combat.ReapAndSew;
import com.ouroboros.abilities.instances.perks.RejuvenateWounds;
import com.ouroboros.abilities.instances.special.Prometheus;
import com.ouroboros.abilities.instances.utility.Geomorph;

public class AbilityRegistry 
{
	public static final Map<String, ObsAbility> abilityRegistry = new HashMap<>();
    
	public static void abilityInit() 
    {
        List<Class<? extends ObsAbility>> itemClasses = Arrays.asList(
            ImbueFire.class, Flamelash.class, GeminiSlash.class, 
            ReapAndSew.class,RejuvenateWounds.class,Geomorph.class,
            Prometheus.class);
        
        for (Class<? extends ObsAbility> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends ObsAbility> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                ObsAbility instance = constructor.newInstance();
                abilityRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
