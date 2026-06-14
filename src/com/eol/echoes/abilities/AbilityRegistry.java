package com.eol.echoes.abilities;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eol.echoes.abilities.instances.armament.Riptide;
import com.eol.echoes.abilities.instances.bow.Launch;
import com.eol.echoes.abilities.instances.crossbow.QuickLoad;
import com.eol.echoes.abilities.instances.hammer.Bulwark;
import com.eol.echoes.abilities.instances.hatchet.HeavyChop;
import com.eol.echoes.abilities.instances.pickaxe.HeavySwing;
import com.eol.echoes.abilities.instances.polearm.Lunge;
import com.eol.echoes.abilities.instances.scythe.Cleave;
import com.eol.echoes.abilities.instances.special.Annihilate;
import com.eol.echoes.abilities.instances.special.ArcaneOrder;
import com.eol.echoes.abilities.instances.special.BjornsGlaciate;
import com.eol.echoes.abilities.instances.special.BloodFolliedBlade;
import com.eol.echoes.abilities.instances.special.KelligirAeroMastery;
import com.eol.echoes.abilities.instances.special.LanceFlammes;
import com.eol.echoes.abilities.instances.special.LuminasRadiance;
import com.eol.echoes.abilities.instances.special.LuminusRadiance;
import com.eol.echoes.abilities.instances.special.MarkedForDeath;
import com.eol.echoes.abilities.instances.special.NidusPreservation;
import com.eol.echoes.abilities.instances.special.PhotonCannon;
import com.eol.echoes.abilities.instances.special.PlaguesPrimer;
import com.eol.echoes.abilities.instances.special.SpatialDistortion;
import com.eol.echoes.abilities.instances.special.SpatialRend;
import com.eol.echoes.abilities.instances.sword.Flamelash;
import com.eol.echoes.abilities.instances.sword.GeminiSlash;
import com.eol.echoes.abilities.instances.sword.ImbueFire;

public class AbilityRegistry 
{
	public static final Map<String, EchoAbility> abilityRegistry = new HashMap<>();
    
	public static void abilityInit() 
    {
        List<Class<? extends EchoAbility>> itemClasses = Arrays.asList(
            ImbueFire.class, Flamelash.class, GeminiSlash.class, 
            Cleave.class, HeavySwing.class, Lunge.class, HeavyChop.class,
            Riptide.class, Bulwark.class, Launch.class, QuickLoad.class,
            
            // Special
            LuminusRadiance.class,
            NidusPreservation.class,
            SpatialRend.class,
            BjornsGlaciate.class,
            BloodFolliedBlade.class,
            Annihilate.class,
            KelligirAeroMastery.class,
            PlaguesPrimer.class,
            LuminasRadiance.class,
            MarkedForDeath.class,
            SpatialDistortion.class,
            ArcaneOrder.class,
            PhotonCannon.class,
            LanceFlammes.class);
        
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
