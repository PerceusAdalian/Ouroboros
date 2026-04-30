package com.eol.echoes.records;

/**
 * Stat block loaded from echo_config.yml for a given binding MateriaType.
 *
 * attackRatingMin/Max  - rolled attack speed range (attacks per second)
 * durabilityMin/Max    - rolled durability range (absolute item durability units)
 * durabilityMultiplier - scalar applied to the rolled durability value
 * critRateBonus        - flat addition to material's crit_rate_base
 * attackBonus          - flat addition to rolled base attack
 */
public record BindingStatBlock(
    double attackRatingMin,
    double attackRatingMax,
    double durabilityMultiplier,
    double critRateBonus,
    double attackBonus
) {}