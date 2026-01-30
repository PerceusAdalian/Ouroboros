package com.ouroboros.mobs.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import com.ouroboros.enums.ElementType;

public class AffinityRegistry 
{
	private static final Map<EntityType, MobAffinity> AFFINITIES = new HashMap<>();
	
    static {
        
    	//Special/Elite/Boss mobs
    	
    	// Allay
        AFFINITIES.put(EntityType.ALLAY, new MobAffinity(
            EnumSet.of(ElementType.AERO,ElementType.PIERCE,ElementType.GEO),        // Resistance
            EnumSet.of(ElementType.MORTIO,ElementType.COSMO),   				   // Weakness
            EnumSet.of(ElementType.CELESTIO,ElementType.BLUNT,ElementType.ARCANO, // Immunity
            		ElementType.SLASH,ElementType.PIERCE,ElementType.PUNCTURE) 
        ));
        
        // Elder Guardian
        AFFINITIES.put(EntityType.ELDER_GUARDIAN, new MobAffinity(
            EnumSet.of(ElementType.BLUNT,ElementType.ARCANO,ElementType.GEO,ElementType.CRUSH,ElementType.INFERNO,ElementType.AERO,ElementType.COSMO),
            EnumSet.of(ElementType.PIERCE,ElementType.MORTIO),
            EnumSet.of(ElementType.GLACIO,ElementType.CELESTIO)
        ));

    	// Ender Dragon
        AFFINITIES.put(EntityType.ENDER_DRAGON, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO,ElementType.MORTIO,ElementType.AERO,ElementType.BLUNT,ElementType.SLASH,ElementType.INFERNO,ElementType.GLACIO), 
            EnumSet.of(ElementType.PIERCE,ElementType.PUNCTURE,ElementType.SLASH),   
            EnumSet.of(ElementType.COSMO,ElementType.ARCANO,ElementType.GEO)   
        ));
        
        // Warden
        AFFINITIES.put(EntityType.WARDEN, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO,ElementType.MORTIO,ElementType.AERO,ElementType.GLACIO,ElementType.INFERNO,ElementType.GEO,ElementType.SLASH,ElementType.PIERCE,ElementType.PUNCTURE,ElementType.BLUNT),
            EnumSet.of(ElementType.ARCANO), // His only "weakness"
            EnumSet.of(ElementType.COSMO,ElementType.CRUSH,ElementType.IMPALE,ElementType.SEVER,ElementType.NEUTRAL,ElementType.NONE)
        ));

        // Wither
        AFFINITIES.put(EntityType.WITHER, new MobAffinity(
    		EnumSet.of(ElementType.INFERNO,ElementType.GLACIO,ElementType.GEO,ElementType.AERO,ElementType.SLASH,ElementType.PIERCE,ElementType.BLUNT),
    		EnumSet.of(ElementType.CELESTIO,ElementType.COSMO),
    		EnumSet.of(ElementType.MORTIO,ElementType.CRUSH,ElementType.IMPALE,ElementType.SEVER)
		));
    
        //Normal mobs
    	
    	// Armadillo
        AFFINITIES.put(EntityType.ARMADILLO, new MobAffinity(
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.GLACIO),
            EnumSet.of(ElementType.BLUNT)
        ));
        
        // Axolotl
        AFFINITIES.put(EntityType.AXOLOTL, new MobAffinity(
            EnumSet.of(ElementType.GLACIO), 
            EnumSet.of(ElementType.BLUNT),   
            EnumSet.noneOf(ElementType.class)   
        ));

        // Bat
        AFFINITIES.put(EntityType.BAT, new MobAffinity(
            EnumSet.noneOf(ElementType.class),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.noneOf(ElementType.class)
        ));
        
        // Bee
        AFFINITIES.put(EntityType.BEE, new MobAffinity(
            EnumSet.of(ElementType.AERO), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Blaze
        AFFINITIES.put(EntityType.BLAZE, new MobAffinity(
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.GLACIO),
            EnumSet.of(ElementType.INFERNO)
        ));

        // Bogged
        AFFINITIES.put(EntityType.GHAST, new MobAffinity(
    		EnumSet.of(ElementType.GLACIO),
            EnumSet.of(ElementType.MORTIO),
            EnumSet.noneOf(ElementType.class)
        ));

        // Breeze
        AFFINITIES.put(EntityType.BREEZE, new MobAffinity(
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.AERO,ElementType.PUNCTURE,ElementType.IMPALE)
        ));

        // Camel
        AFFINITIES.put(EntityType.CAMEL, new MobAffinity(
    		EnumSet.noneOf(ElementType.class),
    		EnumSet.noneOf(ElementType.class),
    		EnumSet.noneOf(ElementType.class)
        ));

        // Cat
        AFFINITIES.put(EntityType.CAT, new MobAffinity(
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class)
        ));

    	// Cave Spider
        AFFINITIES.put(EntityType.CAVE_SPIDER, new MobAffinity(
            EnumSet.of(ElementType.SLASH), 
            EnumSet.of(ElementType.BLUNT),   
            EnumSet.of(ElementType.AERO)   
        ));

        // Chicken
        AFFINITIES.put(EntityType.CHICKEN, new MobAffinity(
            EnumSet.of(ElementType.AERO),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        // Cod
        AFFINITIES.put(EntityType.COD, new MobAffinity(
    		EnumSet.noneOf(ElementType.class),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.noneOf(ElementType.class)
        ));

        // Cow
        AFFINITIES.put(EntityType.COW, new MobAffinity(
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.SLASH, ElementType.MORTIO),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        // CREAKING
        AFFINITIES.put(EntityType.CREAKING, new MobAffinity(
    		EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class)
        ));

        // Creeper
        AFFINITIES.put(EntityType.CREEPER, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.INFERNO),
            EnumSet.noneOf(ElementType.class)
        ));

        // Dolphin
        AFFINITIES.put(EntityType.DOLPHIN, new MobAffinity(
            EnumSet.of(ElementType.GLACIO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.noneOf(ElementType.class)   
        ));

        // Donkey
        AFFINITIES.put(EntityType.DONKEY, new MobAffinity(
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.SLASH),
            EnumSet.noneOf(ElementType.class)
        ));

        // Zombie Nautilus
        AFFINITIES.put(EntityType.ZOMBIE_NAUTILUS, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.PIERCE, ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));
        
        
        // Drowned
        AFFINITIES.put(EntityType.DROWNED, new MobAffinity(
            EnumSet.of(ElementType.INFERNO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.GLACIO)   
        ));
        
        // Enderman
        AFFINITIES.put(EntityType.ENDERMAN, new MobAffinity(
    		EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.ARCANO),   
            EnumSet.of(ElementType.COSMO)   
        ));
        
        // Endermite
        AFFINITIES.put(EntityType.ENDERMITE, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.ARCANO),   
            EnumSet.of(ElementType.COSMO)   
        ));

        // Evoker
        AFFINITIES.put(EntityType.EVOKER, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.MORTIO)
        ));
        
        // Fox
        AFFINITIES.put(EntityType.FOX, new MobAffinity(
    		EnumSet.noneOf(ElementType.class),
    		EnumSet.noneOf(ElementType.class),
    		EnumSet.noneOf(ElementType.class)
        ));

        // Frog
        AFFINITIES.put(EntityType.ZOMBIE, new MobAffinity(
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.noneOf(ElementType.class)
        ));

        // Ghast
        AFFINITIES.put(EntityType.GHAST, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.GLACIO),   
            EnumSet.of(ElementType.INFERNO)   
        ));

        // Giant
        AFFINITIES.put(EntityType.GIANT, new MobAffinity(
            EnumSet.of(ElementType.AERO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));

        // Glow Squid
        AFFINITIES.put(EntityType.GHAST, new MobAffinity(
    		EnumSet.of(ElementType.GLACIO),
    		EnumSet.noneOf(ElementType.class),
    		EnumSet.of(ElementType.COSMO)
        ));

        // Goat
        AFFINITIES.put(EntityType.GOAT, new MobAffinity(
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.CELESTIO)
        ));

    	// Guardian
        AFFINITIES.put(EntityType.GHAST, new MobAffinity(
            EnumSet.of(ElementType.AERO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.GLACIO)   
        ));

        // Happy Ghast
        AFFINITIES.put(EntityType.HAPPY_GHAST, new MobAffinity(
    		EnumSet.of(ElementType.AERO), 
            EnumSet.of(ElementType.MORTIO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));
        
        // Hoglin
        AFFINITIES.put(EntityType.HOGLIN, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.GLACIO),   
            EnumSet.of(ElementType.INFERNO)   
        ));

        // Horse
        AFFINITIES.put(EntityType.HORSE, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.GEO)
        ));
        
        // Illusioner
        AFFINITIES.put(EntityType.ILLUSIONER, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.COSMO),   
            EnumSet.of(ElementType.ARCANO)   
        ));

        // Iron Golem
        AFFINITIES.put(EntityType.ZOMBIE, new MobAffinity(
            EnumSet.of(ElementType.GLACIO),
            EnumSet.of(ElementType.INFERNO),
            EnumSet.of(ElementType.GEO)
        ));

        // Llama
        AFFINITIES.put(EntityType.LLAMA, new MobAffinity(
			EnumSet.of(ElementType.BLUNT),
			EnumSet.of(ElementType.PIERCE),
			EnumSet.of(ElementType.GEO)
                
        ));

        // Magma Cube
        AFFINITIES.put(EntityType.MAGMA_CUBE, new MobAffinity(
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.GLACIO),
            EnumSet.of(ElementType.INFERNO)
        ));

        // Mooshroom
        AFFINITIES.put(EntityType.MOOSHROOM, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Mule
        AFFINITIES.put(EntityType.MULE, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.GEO)
        ));

    	// Ocelot
        AFFINITIES.put(EntityType.OCELOT, new MobAffinity(
            EnumSet.of(ElementType.PIERCE), 
            EnumSet.of(ElementType.GLACIO),   
            EnumSet.of(ElementType.AERO)   
        ));

        // PILLAGER
        AFFINITIES.put(EntityType.PILLAGER, new MobAffinity(
            EnumSet.of(ElementType.MORTIO),
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        //Nautilus
        AFFINITIES.put(EntityType.NAUTILUS, new MobAffinity(
        		EnumSet.of(ElementType.BLUNT),
        		EnumSet.of(ElementType.PIERCE, ElementType.GLACIO, ElementType.CRUSH),
        		EnumSet.of(ElementType.AERO, ElementType.BLAST)));
        
        // Polar Bear
        AFFINITIES.put(EntityType.POLAR_BEAR, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.GLACIO)   
        ));

        // Pufferfish
        AFFINITIES.put(EntityType.PUFFERFISH, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.GLACIO)
        ));
        
        // Rabbit
        AFFINITIES.put(EntityType.RABBIT, new MobAffinity(
            EnumSet.of(ElementType.GEO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Panda
        AFFINITIES.put(EntityType.PANDA, new MobAffinity(
            EnumSet.of(ElementType.GEO),
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.CELESTIO)
        ));

        // Parrot
        AFFINITIES.put(EntityType.PARROT, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.AERO)   
        ));

        // Phantom
        AFFINITIES.put(EntityType.PHANTOM, new MobAffinity(
            EnumSet.of(ElementType.AERO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.COSMO)
        ));

        // Pig
        AFFINITIES.put(EntityType.PIG, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.SLASH),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Piglin
        AFFINITIES.put(EntityType.PIGLIN, new MobAffinity(
            EnumSet.of(ElementType.INFERNO),
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.CELESTIO)
        ));

    	// Piglin Brute
        AFFINITIES.put(EntityType.PIGLIN_BRUTE, new MobAffinity(
            EnumSet.of(ElementType.INFERNO), 
            EnumSet.of(ElementType.GLACIO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Ravager
        AFFINITIES.put(EntityType.RAVAGER, new MobAffinity(
            EnumSet.of(ElementType.INFERNO),
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        // Salmon
        AFFINITIES.put(EntityType.SALMON, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.GLACIO)   
        ));

        // Sheep
        AFFINITIES.put(EntityType.SHEEP, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.MORTIO),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        // Shulker
        AFFINITIES.put(EntityType.SHULKER, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.CRUSH),   
            EnumSet.of(ElementType.COSMO)   
        ));

        // Silverfish
        AFFINITIES.put(EntityType.SILVERFISH, new MobAffinity(
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.PIERCE),
            EnumSet.of(ElementType.GEO)
        ));

        // Skeleton
        AFFINITIES.put(EntityType.SKELETON, new MobAffinity(
            EnumSet.of(ElementType.INFERNO), 
            EnumSet.of(ElementType.CELESTIO),   
            EnumSet.of(ElementType.MORTIO)   
        ));

        // Skeleton Horse
        AFFINITIES.put(EntityType.SKELETON_HORSE, new MobAffinity(
    		EnumSet.of(ElementType.INFERNO), 
            EnumSet.of(ElementType.CELESTIO),   
            EnumSet.of(ElementType.MORTIO)   
        ));

        // Slime
        AFFINITIES.put(EntityType.SLIME, new MobAffinity(
            EnumSet.of(ElementType.SLASH), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.PIERCE)   
        ));

        // Sniffer
        AFFINITIES.put(EntityType.ZOMBIE, new MobAffinity(
            EnumSet.of(ElementType.BLUNT),
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.GEO)
        ));

    	// Snow Golem
        AFFINITIES.put(EntityType.SNOW_GOLEM, new MobAffinity(
            EnumSet.of(ElementType.PIERCE), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.GLACIO)   
        ));

        // Spider
        AFFINITIES.put(EntityType.SPIDER, new MobAffinity(
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO),
            EnumSet.of(ElementType.AERO)
        ));
        
        // Squid
        AFFINITIES.put(EntityType.SQUID, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.GLACIO)   
        ));

        // STRAY
        AFFINITIES.put(EntityType.STRAY, new MobAffinity(
            EnumSet.of(ElementType.SLASH),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.GLACIO)
        ));
        
        // Strider
        AFFINITIES.put(EntityType.STRIDER, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.GLACIO),   
            EnumSet.of(ElementType.INFERNO)   
        ));

        // Tadpole
        AFFINITIES.put(EntityType.TADPOLE, new MobAffinity(
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class)
        ));

        // Trader Llama
        AFFINITIES.put(EntityType.TRADER_LLAMA, new MobAffinity(
    		EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class),
            EnumSet.noneOf(ElementType.class)
        ));

        // Tropical Fish
        AFFINITIES.put(EntityType.TROPICAL_FISH, new MobAffinity(
        		EnumSet.of(ElementType.AERO),
                EnumSet.of(ElementType.PIERCE),
                EnumSet.of(ElementType.GLACIO)
        ));

        // Turtle
        AFFINITIES.put(EntityType.TURTLE, new MobAffinity(
            EnumSet.of(ElementType.SLASH), 
            EnumSet.of(ElementType.PIERCE),   
            EnumSet.of(ElementType.BLUNT)   
        ));

        // Vex
        AFFINITIES.put(EntityType.VEX, new MobAffinity(
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));

    	// Villager
        AFFINITIES.put(EntityType.VILLAGER, new MobAffinity(
            EnumSet.of(ElementType.GEO), 
            EnumSet.of(ElementType.MORTIO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Vindicator
        AFFINITIES.put(EntityType.VINDICATOR, new MobAffinity(
            EnumSet.of(ElementType.MORTIO),
            EnumSet.of(ElementType.COSMO),
            EnumSet.of(ElementType.CELESTIO)
        ));
        
        // Wandering Trader
        AFFINITIES.put(EntityType.WANDERING_TRADER, new MobAffinity(
    		EnumSet.of(ElementType.GEO), 
            EnumSet.of(ElementType.MORTIO),   
            EnumSet.of(ElementType.CELESTIO)   
        ));

        // Witch
        AFFINITIES.put(EntityType.WITCH, new MobAffinity(
            EnumSet.of(ElementType.MORTIO), 
            EnumSet.of(ElementType.INFERNO),   
            EnumSet.of(ElementType.ARCANO)   
        ));

        // Wither Skeleton
        AFFINITIES.put(EntityType.GHAST, new MobAffinity(
    		EnumSet.of(ElementType.INFERNO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));

        // Wolf
        AFFINITIES.put(EntityType.ZOMBIE, new MobAffinity(
            EnumSet.noneOf(ElementType.class),
            EnumSet.of(ElementType.MORTIO),
            EnumSet.noneOf(ElementType.class)
        ));

        // Zoglin
        AFFINITIES.put(EntityType.ZOGLIN, new MobAffinity(
    		EnumSet.of(ElementType.INFERNO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));

        // Zombie
        AFFINITIES.put(EntityType.ZOMBIE, new MobAffinity(
            EnumSet.of(ElementType.ARCANO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));
        
        // Zombie Horse
        AFFINITIES.put(EntityType.ZOMBIE_HORSE, new MobAffinity(
    		EnumSet.of(ElementType.ARCANO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));

        // Zombie Villager
        AFFINITIES.put(EntityType.ZOMBIE_VILLAGER, new MobAffinity(
    		EnumSet.of(ElementType.ARCANO),
            EnumSet.of(ElementType.CELESTIO),
            EnumSet.of(ElementType.MORTIO)
        ));
        
        // Zombified Piglin
        AFFINITIES.put(EntityType.ZOMBIFIED_PIGLIN, new MobAffinity(
            EnumSet.of(ElementType.BLUNT), 
            EnumSet.of(ElementType.CELESTIO),   
            EnumSet.of(ElementType.MORTIO)   
        ));
    }

    public static MobAffinity getAffinity(EntityType type) 
    {
        return AFFINITIES.getOrDefault(type,
        		new MobAffinity(EnumSet.noneOf(ElementType.class),
				                EnumSet.noneOf(ElementType.class),
				                EnumSet.noneOf(ElementType.class)));
    }
}
