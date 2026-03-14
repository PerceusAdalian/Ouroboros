package com.ouroboros.objects;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ouroboros.objects.instances.AeroEssence;
import com.ouroboros.objects.instances.CelestioEssence;
import com.ouroboros.objects.instances.CosmoEssence;
import com.ouroboros.objects.instances.GeoEssence;
import com.ouroboros.objects.instances.GlacioEssence;
import com.ouroboros.objects.instances.HeresioEssence;
import com.ouroboros.objects.instances.InfernoEssence;
import com.ouroboros.objects.instances.LuminiteCore;
import com.ouroboros.objects.instances.LuminiteDust;
import com.ouroboros.objects.instances.LuminiteFragment;
import com.ouroboros.objects.instances.LuminiteIngot;
import com.ouroboros.objects.instances.LuminiteShard;
import com.ouroboros.objects.instances.MortioEssence;
import com.ouroboros.objects.instances.ObsStatVoucher;
import com.ouroboros.objects.instances.RemembranceOfHope;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.objects.instances.TrainingWand;

public class ObjectRegistry 
{
	public static final Map<String, AbstractObsObject> itemRegistry = new HashMap<>();

    public static void itemInit() 
    {
        List<Class<? extends AbstractObsObject>> itemClasses = Arrays.asList(
            //Money Items:
    		LuminiteDust.class,
    		LuminiteShard.class,
    		LuminiteFragment.class,
    		LuminiteIngot.class,
    		LuminiteCore.class,
    		
        	ObsStatVoucher.class,
        	
        	RemembranceOfHope.class,
        	
        	TearOfLumina.class,
        	TrainingWand.class,
        	
        	CelestioEssence.class,
        	MortioEssence.class,
        	InfernoEssence.class,
        	GlacioEssence.class,
        	AeroEssence.class,
        	GeoEssence.class,
        	CosmoEssence.class,
        	HeresioEssence.class);
        
        for (Class<? extends AbstractObsObject> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends AbstractObsObject> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                AbstractObsObject instance = constructor.newInstance();
                itemRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
