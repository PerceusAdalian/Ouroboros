package com.eol.echoes.records;

import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.ModifierCondition;

/**
 * PassiveModifier triggers a side effect when its condition is satisfied.
 * Unlike ActiveModifier, it does not mutate a core stat directly.
 *
 * effectKey is a string identifier that maps to a registered effect handler
 * in the combat pipeline (e.g. "apply_burn", "apply_expose", "movement_speed").
 * This keeps PassiveModifier data-only and decouples it from Bukkit event logic.
 *
 * magnitude is optional context for the effect (e.g. proc chance, duration scalar).
 * A magnitude of 0.0 means the effect fires unconditionally on condition match
 * with no scalar applied (the effect handler decides its own defaults).
 *
 * Examples from the mockups:
 *   ">Arrows Apply Expose"                    -> effectKey="apply_expose",    magnitude=0.0
 *   ">35% Chance to Ignore Arrow Consumption" -> effectKey="ignore_arrow",   magnitude=0.35
 *   ">Atk Rate become 2.0 in The End"         -> effectKey="set_attack_rate", magnitude=2.0,  condition=END
 *   ">Increased Movement Speed (Nether)"      -> effectKey="movement_speed",  magnitude=0.0,  condition=NETHER
 */
public record PassiveModifier(ModifierCondition condition, PassiveEchoEffect effectKey, double magnitude) implements Modifier
{
    @Override
    public boolean isActive()
    {
        return false;
    }
    
    public PassiveEchoEffect getType()
    {
    	return effectKey;
    }

    @Override
    public String loreLabel()
    {
        String base = "&b>)&r&f " + formatEffect();

        if (condition != ModifierCondition.PASSIVE)
            base += " &r&7(" + formatCondition()+")";

        return base;
    }

    private String formatEffect()
    {
        return switch (effectKey)
        {
            case EXPOSE            		   -> "Attacks Apply Expose: "+(int)(magnitude * 100) + "%";
            case BURNING           		   -> "Attacks Inflict Burn: "+(int)(magnitude * 100) + "%";
            case POISONOUS         		   -> "Attacks Apply Erosion: "+(int)(magnitude * 100) + "%";
            case SLOWING  	 	           -> "Attacks Inflict Slowness: "+(int)(magnitude * 100) + "%";
            case FATIGUING   	           -> "Attacks Inflict Fatigue: "+(int)(magnitude * 100) + "%";
            case STUNNING      	           -> "Attacks Inflict Stun: "+(int)(magnitude * 100) + "%";
            case IGNORE_ARROW              -> "Optimized Quiver: "+(int)(magnitude * 100) + "%";
            case RECYCLE_ARROWS            -> "Recyclable Arrows";
            case SET_ATTACK_RATE           -> "Atk Rate becomes " + magnitude;
            case INCREASED_MOVEMENT_SPEED  -> "Swift Footed";
            case DECREASED_MOVEMENT_SPEED  -> "Heavy Weapon";
            case PROTECTIVE	               -> "Protective Armament";
            case KNOCKBACK                 -> "Increased Knockback";
            case LUCKY 			           -> "Increased Collection Luck";
            case NIMBLE                    -> "Nimble Footed";
            case INFINITY                  -> "Optimized Quiver";
            case NIGHTSIGHT		           -> "Grants Nightvision";
            case VAMPIRE                   -> "Vampiric Attacks";
            case HERESIO_ARMAMENT          -> "Heresian Armament";
            case COSMO_ARMAMENT            -> "Cosmic Armament";
            case CELESTIO_ARMAMENT         -> "Celestian Armament";
            case MORTIO_ARMAMENT		   -> "Mortian Armament";
            case INFERNO_ARMAMENT		   -> "Infernal Armament";
            case GLACIO_ARMAMENT		   -> "Glacian Armament";
            case GEO_ARMAMENT			   -> "Geo Armament";
            case AERO_ARMAMENT 			   -> "Aero Armament";
            case ARCANO_ARMAMENT		   -> "Arcane Armament";
        };
    }

    private String formatCondition()
    {
        return switch (condition)
        {
            case PVE       		-> "PVE";
            case PVP       		-> "PVP";
            case PASSIVE  		-> "Passive";
            case UNDEAD    		-> "PVE: Undead";
            case LIVING    		-> "PVE: Living";
            case FLYING    		-> "PVE: Arial";
            case GLACIAL   		-> "PVE: Glacial";
            case INFERNAL 		-> "PVE: Infernal";
            case GROUNDED		-> "PVE: Grounded";
            case COSMIC			-> "PVE: Cosmic";
            case OCCULTIC		-> "PVE: Occultic";
            case ELEMENTAL 		-> "PVE: Elementals";
            case BUGS      		-> "PVE: Bugs";
            case RAID      		-> "PVE: Raid";
            case OVERWORLD 		-> "Overworld";
            case DURING_DAY   	-> "Overworld: Daytime";
            case DURING_NIGHT	-> "Overworld: Nighttime";
            case CLEAR_WEATHER  -> "Overworld: Clear";
            case RAINY_WEATHER  -> "Overworld: Rain";
            case STORMY_WEATHER -> "Overworld: Storm";
            case NETHER       	-> "World: The Nether";
            case END            -> "World: The End";
            case COLDBIOMES   	-> "Biome: Cold";
            case HOTBIOMES    	-> "Biome: Hot";
            case GENERICBIOMES 	-> "Biome: Any";
        };
    }

	@Override
	public double getMagnitude()
	{
		return magnitude;
	}
}
