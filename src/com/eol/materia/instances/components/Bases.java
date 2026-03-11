package com.eol.materia.instances.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.instances.Materia;
import com.ouroboros.enums.Rarity;

public class Bases 
{
	public static void load()
	{
		//-------------------------------WOOD-------------------------------//
		Materia.register(new Materia("Wooden Block",
				"wooden_block_1",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA simple wooden block.. nothing to write home about."));
		Materia.register(new Materia("Wooden Block",
				"wooden_block_2",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oThey came in hoards, even wooden swords had their use.&7&o\""));
		Materia.register(new Materia("Wooden Block",
				"wooden_block_3",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oWith the last slain, more came. We had no other choice.&7&o\""));
		Materia.register(new Materia("Wooden Block",
				"wooden_block_4",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oPitiful knight. Don't you know wood warps? Stand up, and fight!&7&o\""));
		Materia.register(new Materia("Wooden Block",
				"wooden_block_5",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA carpenter's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------STONE-------------------------------//
		Materia.register(new Materia("Stone Block",
				"stone_block_1",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA simple stone block.. nothing to write home about."));
		Materia.register(new Materia("Stone Block",
				"stone_block_2",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oSome chose wood.. smart people chose weight.&7&o\""));
		Materia.register(new Materia("Stone Block",
				"stone_block_3",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oEven the dullest of blades can bash someone's head in.&7&o\""));
		Materia.register(new Materia("Stone Block",
				"stone_block_4",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oThis could pack a serious punch.. Oh look, a zombie!&7&o\""));
		Materia.register(new Materia("Stone Block",
				"stone_block_5",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA mason's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------COPPER-------------------------------//
		
		//-------------------------------IRON-------------------------------//
		
		//-------------------------------GOLD-------------------------------//
		
		//-------------------------------DIAMOND-------------------------------//
		
		//-------------------------------NETHERITE-------------------------------//
	}
}
