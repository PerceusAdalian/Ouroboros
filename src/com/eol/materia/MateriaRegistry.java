package com.eol.materia;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eol.materia.instances.Materia;
import com.eol.materia.instances.catalysts.Catalyst_1;

public class MateriaRegistry 
{
	public static final Map<String, Materia> materiaRegistry = new HashMap<>();
    public static void init() 
    {
        List<Class<? extends Materia>> itemClasses = Arrays.asList(
        		Catalyst_1.class
        		
            );
        
        for (Class<? extends Materia> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends Materia> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Materia instance = constructor.newInstance();
                materiaRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
