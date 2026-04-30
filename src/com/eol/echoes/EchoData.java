package com.eol.echoes;

public class EchoData
{
    private final double attack;
    private final double attackRating;
    private final double critRate;
    private final double critModifier;
    private final int    maxDurability;
    private final int    currentDurability;

    public EchoData(double attack, double attackRating, double critRate, double critModifier, int maxDurability, int currentDurability)
    {
        this.attack            = attack;
        this.attackRating      = attackRating;
        this.critRate          = critRate;
        this.critModifier      = critModifier;
        this.maxDurability     = maxDurability;
        this.currentDurability = currentDurability;
    }

    public double getAttack()            { return attack; }
    public double getAttackRating()      { return attackRating; }
    public double getCritRate()          { return critRate; }
    public double getCritModifier()      { return critModifier; }
    public int    getMaxDurability()     { return maxDurability; }
    public int    getCurrentDurability() { return currentDurability; }
}