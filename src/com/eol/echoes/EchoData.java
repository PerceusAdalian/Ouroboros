package com.eol.echoes;

public class EchoData
{
    private final double attack;
    private final double attackRating;
    private final double critRate;
    private final double critModifier;
    private final int    maxDurability;
    private final int    currentDurability;

    /**
     * @param attack
     * @param attackRating
     * @param critRate
     * @param critModifier
     * @param maxDurability
     * @param currentDurability
     */
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
    
    public EchoData setAttack(double attack) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
    public EchoData setAttackRating(double attackRating) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
    public EchoData setCritRate(double critRate) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
    public EchoData setCritModifier(double critModifier) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
    public EchoData setMaxDurability(int maxDurability) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
    public EchoData setCurrentDurability(int currentDurability) { return new EchoData(attack, attackRating, critRate, critModifier, maxDurability, currentDurability); }
}