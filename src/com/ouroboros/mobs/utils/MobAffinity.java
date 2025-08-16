package com.ouroboros.mobs.utils;

import java.util.Set;

import com.ouroboros.enums.ElementType;

public class MobAffinity 
{
	private final Set<ElementType> resistances;
	private final Set<ElementType> weaknesses;
	private final Set<ElementType> immunities;
	
	public MobAffinity(Set<ElementType> resistances, Set<ElementType> weaknesses, Set<ElementType> immunities) 
	{
        this.resistances = resistances;
        this.weaknesses = weaknesses;
        this.immunities = immunities;
    }
	
    public Set<ElementType> getResistances() { return resistances; }
    public Set<ElementType> getWeaknesses() { return weaknesses; }
    public Set<ElementType> getImmunities() { return immunities; }

    public boolean resists(ElementType category) { return resistances.contains(category); }
    public boolean weakTo(ElementType category) { return weaknesses.contains(category); }
    public boolean immuneTo(ElementType category) { return immunities.contains(category); }
}
