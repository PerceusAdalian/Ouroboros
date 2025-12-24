package com.lol.spells.instances;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lol.spells.instances.inferno.Incendio;

public class SpellRegistry 
{
	public static final Map<String, Spell> spellRegistry = new HashMap<>();
    public static void init() 
    {
        List<Class<? extends Spell>> itemClasses = Arrays.asList(
            Incendio.class
            );
        
        for (Class<? extends Spell> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends Spell> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Spell instance = constructor.newInstance();
                spellRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
