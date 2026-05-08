package com.eol.echoes.abilities;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.echoes.abilities.instances.armament.Riptide;
import com.eol.echoes.abilities.instances.hammer.Bulwark;
import com.eol.echoes.abilities.instances.hatchet.HeavyChop;
import com.eol.echoes.abilities.instances.pickaxe.HeavySwing;
import com.eol.echoes.abilities.instances.polearm.Lunge;
import com.eol.echoes.abilities.instances.scythe.Cleave;
import com.eol.echoes.abilities.instances.special.LuminusRadiance;
import com.eol.echoes.abilities.instances.special.NidusPreservation;
import com.eol.echoes.abilities.instances.special.SpacialRend;
import com.eol.echoes.abilities.instances.sword.Flamelash;
import com.eol.echoes.abilities.instances.sword.GeminiSlash;
import com.eol.echoes.abilities.instances.sword.ImbueFire;

public class AbilityRegistry 
{
	public static final Map<String, EchoAbility> abilityRegistry = new HashMap<>();
    
	@SuppressWarnings("null")
	public static void abilityInit() 
    {
        List<Class<? extends EchoAbility>> itemClasses = Arrays.asList(
            ImbueFire.class, Flamelash.class, GeminiSlash.class, 
            Cleave.class, HeavySwing.class, Lunge.class, HeavyChop.class,
            Riptide.class, Bulwark.class,
            
            // Special
            LuminusRadiance.class,
            NidusPreservation.class,
            SpacialRend.class);
        
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
