package com.eol.echoes.records;

import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.WeaponModifierCondition;

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
public record PassiveModifier(WeaponModifierCondition condition, PassiveEchoEffect effectKey, double magnitude) implements Modifier
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
        // Passive modifier lore uses the ">" prefix convention seen in the mockups.
        // The effectKey is transformed into a human-readable label here.
        // For hand-authored EOL modifiers, this can be overridden by supplying
        // a custom display string via PassiveModifier.withLabel().
        String base = ">" + formatEffect();

        if (condition != WeaponModifierCondition.PASSIVE)
            base += " (" + formatCondition() + ")";

        return base;
    }

    private String formatEffect()
    {
        // Simple key-to-display mapping. Extend as effects are added.
        return switch (effectKey)
        {
            case EXPOSE          -> "Attacks Apply Expose";
            case BURNING         -> "Attacks Inflict Burn";
            case POISONOUS       -> "Attacks Are Corrosive";
            case SLOWING  	 	 -> "Attacks Inflict Slowness";
            case FATIGUING   	 -> "Attacks Inflict Fatigue";
            case STUNNING      	 -> "Attacks Inflict Stun";
            case IGNORE_ARROW    -> "Ignore Arrow Consumption: "+(int)(magnitude * 100) + "%";
            case SET_ATTACK_RATE -> "Atk Rate becomes " + magnitude;
            case MOVEMENT_SPEED  -> "Swift Footed";
            case KNOCKBACK       -> "Increased Knockback";
            case LUCKY 			 -> "Increased Collection Luck";
            case NIMBLE          -> "Nimble Footed";
            case INFINITY        -> "Infinity";
        };
    }

    private String formatCondition()
    {
        return switch (condition)
        {
            case PVE       		-> "PVE";
            case PVP       		-> "PVP";
            case PASSIVE   		-> "Passive";
            case UNDEAD    		-> "PVE: Undead";
            case LIVING    		-> "PVE: Living";
            case FLYING    		-> "PVE: Flying";
            case GLACIAL   		-> "PVE: Glacial";
            case INFERNAL 		-> "PVE: Infernal";
            case GROUNDED		-> "PVE: Grounded";
            case COSMIC			-> "PVE: Cosmic";
            case OCCULTIC		-> "PVE: Occultic";
            case BUGS      		-> "PVE: Bugs";
            case RAID      		-> "PVE: Raid";
            case OVERWORLD 		-> "Overworld";
            case DURING_DAY   	-> "Overworld: Daytime";
            case DURING_NIGHT 	-> "Overworld: Nighttime";
            case CLEAR_WEATHER  -> "Overworld: Clear";
            case RAINY_WEATHER  -> "Overworld: Rain";
            case STORMY_WEATHER -> "Overworld: Storm";
            case NETHER       	-> "World: The Nether";
            case END          	-> "World: The End";
            case COLDBIOMES   	-> "Biome: Cold";
            case HOTBIOMES    	-> "Biome: Hot";
            case GENERICBIOMES 	-> "Biome";
            case ELEMENTAL 		-> "Elemental";
        };
    }

	@Override
	public double getMagnitude()
	{
		return magnitude;
	}
}
