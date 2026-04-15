package com.eol.echoes.records;

import com.eol.enums.CombatStat;
import com.eol.enums.WeaponModifierCondition;

/**
 * ActiveModifier directly mutates one of the four core combat stats
 * (Attack, AttackRating, CritRate, CritModifier) when its condition is satisfied.
 *
 * magnitude is a signed delta applied to the stat:
 *   - For ATTACK:        flat bonus (isPercent=false) or % of base (isPercent=true)
 *   - For ATTACK_RATING: flat delta on the multiplier (e.g. +0.25)
 *   - For CRIT_RATE:     percentage point delta (e.g. 0.15 = +15%)
 *   - For CRIT_MODIFIER: additive multiplier delta (e.g. 1.0 = +1.0x)
 *
 * Examples from the mockups:
 *   "+50% Atk (PVE: Undead)"   → condition=UNDEAD, combatStat=ATTACK,        magnitude=0.50, isPercent=true
 *   "+15% Crit% (PVE: Undead)" → condition=UNDEAD, combatStat=CRIT_RATE,     magnitude=0.15, isPercent=true
 *   "+5 Atk (PVE: The End)"    → condition=END,    combatStat=ATTACK,        magnitude=5.0,  isPercent=false
 *   "+0.5x Crit Mod"           → condition=PASSIVE,combatStat=CRIT_MODIFIER, magnitude=0.5,  isPercent=false
 */
public record ActiveModifier(WeaponModifierCondition condition, CombatStat combatStat, double magnitude, boolean isPercent) implements Modifier
{
    @Override
    public boolean isActive()
    {
        return true;
    }
 
    @Override
    public String loreLabel()
    {
        String sign = magnitude >= 0 ? "+" : "-";
        String mag  = isPercent
                ? sign + (int)(magnitude * 100) + "% "
                : sign + (magnitude == (long) magnitude ? String.valueOf((long) magnitude) : String.valueOf(magnitude)) + " ";
 
        return mag + combatStat.getDisplayName() + " (" + formatCondition() + ")";
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
            case FLYING    		-> "PVE: Flying";
            case AQUATIC   		-> "PVE: Aquatic";
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
            case GENERICBIOMES 	-> "Biome";
        };
    }
}
