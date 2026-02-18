package com.lol.spells.instances;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lol.spells.instances.aero.Charge;
import com.lol.spells.instances.aero.Diffindo;
import com.lol.spells.instances.aero.GalvanicNeedle;
import com.lol.spells.instances.aero.Levioso;
import com.lol.spells.instances.aero.Smite;
import com.lol.spells.instances.aero.Thunderbolt;
import com.lol.spells.instances.aero.Thunderstorm;
import com.lol.spells.instances.celestio.AssertOrder;
import com.lol.spells.instances.celestio.Cure;
import com.lol.spells.instances.celestio.Diagnosis;
import com.lol.spells.instances.celestio.Lumos;
import com.lol.spells.instances.celestio.Protego;
import com.lol.spells.instances.celestio.Revelio;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.spells.instances.geo.Expelliarmus;
import com.lol.spells.instances.glacio.Glacius;
import com.lol.spells.instances.heresio.Hex;
import com.lol.spells.instances.inferno.Bombarda;
import com.lol.spells.instances.inferno.Incendio;
import com.lol.spells.instances.mortio.Haunt;

public class SpellRegistry 
{
	public static final Map<String, Spell> spellRegistry = new HashMap<>();
    public static void init() 
    {
        List<Class<? extends Spell>> itemClasses = Arrays.asList(
        		
        		//Inferno
        		Incendio.class,
        		Bombarda.class,
        		
        		//Glacio
        		Glacius.class,
        		
        		
        		//Aero
        		Diffindo.class,
        		Levioso.class,
        		Smite.class,
        		Thunderbolt.class,
        		Charge.class,
        		Thunderstorm.class,
        		GalvanicNeedle.class,
        		
        		//Geo
        		Expelliarmus.class,
        		
        		//Celestio
        		Revelio.class,
        		Protego.class,
        		AssertOrder.class,
        		Lumos.class,
        		Diagnosis.class,
        		Cure.class,
        		
        		//Mortio
        		Haunt.class,
        		
        		//Cosmo
        		ArrestoMomentum.class,
        		
        		//Heresio
        		Hex.class
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
