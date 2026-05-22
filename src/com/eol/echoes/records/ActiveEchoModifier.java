package com.eol.echoes.records;

import com.eol.enums.CombatStat;
import com.eol.enums.ModifierCondition;

/**
 * ActiveModifier directly mutates one of the four core combat stats
 * (Attack, AttackRating, CritRate, CritModifier) when its condition is satisfied.
 *
 * magnitude is a signed delta applied to the stat:
 *   - For ATTACK:        flat bonus (isPercent=false) or % of base (isPercent=true)
 *   - For ATTACK_RATING: flat delta on the multiplier (e.g. +0.25)
 *   - For CRIT_RATE:     percentage point delta (e.g. 0.15 = +15%)
 *   - For CRIT_MODIFIER: additive multiplier delta (e.g. 1.0 = +1.0x)
 */
public record ActiveEchoModifier(ModifierCondition condition, CombatStat combatStat, double magnitude, boolean isPercent, boolean isNegative) implements Modifier
{
    @Override
    public boolean isActive()
    {
        return true;
    }
 
    @Override
    public String loreLabel()
    {
        String sign = magnitude >= 0 ? "&a+" : "&c-";
        String mag  = isPercent
                ? sign + ")&f " + (int)(Math.abs(magnitude) * 100) + "% "
                : sign + ")&f " + (magnitude == (long) magnitude ? String.valueOf((long) Math.abs(magnitude)) : String.valueOf(Math.abs(magnitude))) + " ";
 
        return mag +"&f"+ combatStat.getDisplayName() + " &7(" + formatCondition()+")";
    }
 
    /**
     * Formats the condition into the display style seen in the mockups:
     * PVE conditions → "PVE: <Entity>"
     * World conditions → "World: <Location>" or just the condition name
     */
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
    
    /**
     * @return True if the modifier is/should be expressed as a percent value.
     */
    public boolean isPercent()
    {
    	return isPercent;
    }
    
    /**
     * @return True if the modifier is/should be expressed as a negative value.
     */
    public boolean isNegative()
    {
    	return isNegative;
    }
    
    /**
     * @return Returns the magnitude of the modifier's stat.
     */
    public double getMagnitude()
    {
    	return magnitude;
    }
}
