package com.eol.enums;

import java.util.Set;

public enum EchoForm 
{

	// Weapons and tools
	SWORD("&r&cSwords&f"),
	HATCHET("&r&cHatchets&f"),
	POLEARM("&r&cPolearms&f"),
	PICKAXE("&r&6Pickaxes&f"),
	SPADE("&r&6Spades&f"),
	SCYTHE("&r&6Scythes&f"),
	
	HAMMER("&r&bHammer&f"),
	ARMAMENT("&r&bArmament&f"),
	BOW("&r&dBow&f"),
	CROSSBOW("&r&dCrossbow&f"),

	// Armor pieces
	HELMET("&r&eHelmet&f"),
	CHESTPLATE("&r&eChestplate&f"),
	LEGGINGS("&r&eLeggings&f"),
	BOOTS("&r&eBoots&f")
	;
	
	private final String symbol;
	EchoForm(String string)
	{
		this.symbol = string;
	}
	
	public String getLabel()
	{
		return symbol;
	}
	
	public static EchoForm fromString(String input)
    {
        try
        {
            return EchoForm.valueOf(input.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
	
	public static Set<EchoForm> armor_forms = Set.of(HELMET, CHESTPLATE, LEGGINGS, BOOTS);
	
}
