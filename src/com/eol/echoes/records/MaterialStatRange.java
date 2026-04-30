package com.eol.echoes.records;

/**
 * MaterialStatRange is an immutable snapshot of one EchoMaterial tier's
 * base stat ranges, loaded from echo_config.yml by EchoConfig.
 *
 * StatResolver reads this to roll the base EchoData for a forged Echo.
 * Attack Rating and Durability are NOT here — those come from BindingStatBlock.
 */
public record MaterialStatRange(
	    double baseAttackMin,
	    double baseAttackMax,
	    double baseAttackRateMin,
	    double baseAttackRateMax,
	    double critRateBase,
	    double critModifierCeiling,
	    int    durabilityMin,
	    int    durabilityMax)
	{
	    public double rollBaseAttack()
	    {
	        return baseAttackMin + (Math.random() * (baseAttackMax - baseAttackMin));
	    }

	    public double rollCritModifier()
	    {
	        return 1.0 + (Math.random() * (critModifierCeiling - 1.0));
	    }

	    public int rollDurability()   // ADD
	    {
	        return durabilityMin + (int)(Math.random() * (durabilityMax - durabilityMin + 1));
	    }
	}