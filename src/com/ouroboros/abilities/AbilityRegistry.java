package com.ouroboros.abilities;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ouroboros.abilities.instances.ImbueFire;

public class AbilityRegistry 
{
	public static final Map<String, AbstractOBSAbility> abilityRegistry = new HashMap<>();

    public static void itemInit() 
    {
        List<Class<? extends AbstractOBSAbility>> itemClasses = Arrays.asList(
            ImbueFire.class);
        
        for (Class<? extends AbstractOBSAbility> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends AbstractOBSAbility> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                AbstractOBSAbility instance = constructor.newInstance();
                abilityRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
