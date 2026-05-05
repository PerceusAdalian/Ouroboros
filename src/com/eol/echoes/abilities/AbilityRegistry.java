package com.eol.echoes.abilities;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.echoes.abilities.instances.combat.Flamelash;
import com.eol.echoes.abilities.instances.combat.GeminiSlash;
import com.eol.echoes.abilities.instances.combat.ImbueFire;
import com.eol.echoes.abilities.instances.combat.ReapAndSew;

public class AbilityRegistry 
{
	public static final Map<String, EchoAbility> abilityRegistry = new HashMap<>();
    
	public static void abilityInit() 
    {
        List<Class<? extends EchoAbility>> itemClasses = Arrays.asList(
            ImbueFire.class, Flamelash.class, GeminiSlash.class, 
            ReapAndSew.class);
        
        for (Class<? extends EchoAbility> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends EchoAbility> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                EchoAbility instance = constructor.newInstance();
                abilityRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
