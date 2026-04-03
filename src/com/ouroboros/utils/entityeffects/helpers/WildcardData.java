package com.ouroboros.utils.entityeffects.helpers;

import org.bukkit.potion.PotionEffectType;

public class WildcardData
{
	public final PotionEffectType effect;
    public final int magnitude;
    
    public WildcardData(PotionEffectType effect, int magnitude) 
    {
        this.effect = effect;
        this.magnitude = magnitude;
    }
}
