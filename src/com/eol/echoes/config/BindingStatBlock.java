package com.eol.echoes.config;

/**
 * BindingStatBlock is an immutable snapshot of one Binding type's stat deltas,
 * loaded from echo_config.yml by EchoConfig.
 *
 * StatResolver applies these on top of the MaterialStatRange roll to produce
 * the final EchoData. The Binding is responsible for attack rating, durability
 * scaling, and any minor flat/crit bonuses it contributes.
 */
public record BindingStatBlock(double attackRating, double durabilityMultiplier, double critRateBonus, double attackBonus) {}
