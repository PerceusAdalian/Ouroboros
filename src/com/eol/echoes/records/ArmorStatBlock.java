package com.eol.echoes.records;

/**
 * ArmorStatRange is an immutable snapshot of one armor EchoMaterial tier's
 * base stat ranges, loaded from echo_config.yml by EchoConfig.
 *
 * StatResolver reads this to roll the base ArmorData for a forged armor Echo.
 * Durability ranges are sourced here (not from BindingStatBlock) since armor
 * binding interactions are separate from weapon binding interactions.
 */
public record ArmorStatBlock(
    int    armorRatingMin,
    int    armorRatingMax,
    double blockRateMin,
    double blockRateMax,
    int    critArmorRatingMin,
    int    critArmorRatingMax,
    double critBlockRateMin,
    double critBlockRateMax,
    int    durabilityMin,
    int    durabilityMax) {}