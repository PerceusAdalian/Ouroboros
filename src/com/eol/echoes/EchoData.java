package com.eol.echoes;

public class EchoData 
{
	private double attack;
	private double attackRating;
	private double critRate;
	private double critModifier;
	
	public EchoData(double attack, double attackRating, double critRate, double critModifier)
	{
		this.attack = attack;
		this.attackRating = attackRating;
		this.critRate = critRate;
		this.critModifier = critModifier;
	}

	public double getAttack() 
	{
		return attack;
	}

	public double getAttackRating() 
	{
		return attackRating;
	}

	public double getCritRate() 
	{
		return critRate;
	}

	public double getCritModifier() 
	{
		return critModifier;
	}
}
