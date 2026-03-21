package com.eol.materia.instances.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.instances.Materia;
import com.ouroboros.enums.Rarity;

public class Bindings 
{
	public static void load()
	{
		//-------------------------------STRING-------------------------------//
		Materia.register(new Materia("String Binding",
				"string_1",
				MateriaType.STRING,
				MateriaComponent.BINDING,
				Rarity.ONE,
				false,
				"&r&7&oA simple strung string.. nothing to write home about."));
		Materia.register(new Materia("String Binding",
				"string_2",
				MateriaType.STRING,
				MateriaComponent.BINDING,
				Rarity.TWO,
				false,
				"&7&o\"&6&oBinding is necessary for all crafts.. Including clotheslining.&7&o\""));
		Materia.register(new Materia("String Binding",
				"string_3",
				MateriaType.STRING,
				MateriaComponent.BINDING,
				Rarity.THREE,
				false,
				"&7&o\"&b&oSometimes, the only way out of war was by handy craftsmanship.&7&o\""));
		Materia.register(new Materia("String Binding",
				"string_4",
				MateriaType.STRING,
				MateriaComponent.BINDING,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oWhen a weapon wasn't near, a finely taught string worked just fine.&7&o\""));
		Materia.register(new Materia("String Binding",
				"string_5",
				MateriaType.STRING,
				MateriaComponent.BINDING,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA seamstress's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------PELT-------------------------------//
		Materia.register(new Materia("Pelt Binding",
				"pelt_1",
				MateriaType.PELT,
				MateriaComponent.BINDING,
				Rarity.ONE,
				false,
				"&r&7&oA simple strung pelt.. nothing to write home about."));
		Materia.register(new Materia("Pelt Binding",
				"pelt_2",
				MateriaType.PELT,
				MateriaComponent.BINDING,
				Rarity.TWO,
				false,
				"&7&o\"&6&oPelts are a reliable binding material for many crafts..&7&o\""));
		Materia.register(new Materia("Pelt Binding",
				"pelt_3",
				MateriaType.PELT,
				MateriaComponent.BINDING,
				Rarity.THREE,
				false,
				"&7&o\"&b&oStrong and durable, pelt is a preferred binding material.&7&o\""));
		Materia.register(new Materia("Pelt Binding",
				"pelt_4",
				MateriaType.PELT,
				MateriaComponent.BINDING,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oWhen other materials fail, pelt remains a steadfast choice.&7&o\""));
		Materia.register(new Materia("Pelt Binding",
				"pelt_5",
				MateriaType.PELT,
				MateriaComponent.BINDING,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA artisan's hard work payed off.. Next!&7&o\""));

		//-------------------------------LEATHER-------------------------------//
		Materia.register(new Materia("Leather Binding",
				"leather_1",
				MateriaType.LEATHER,
				MateriaComponent.BINDING,
				Rarity.ONE,
				false,
				"&r&7&oA simple strung leather.. nothing to write home about."));
		Materia.register(new Materia("Leather Binding",
				"leather_2",
				MateriaType.LEATHER,
				MateriaComponent.BINDING,
				Rarity.TWO,
				false,
				"&7&o\"&6&oLeather was a common binding material for many crafts.&7&o\""));
		Materia.register(new Materia("Leather Binding",
				"leather_3",
				MateriaType.LEATHER,
				MateriaComponent.BINDING,
				Rarity.THREE,
				false,
				"&7&o\"&b&oLeather: Timeless, reliable, and strong.&7&o\""));
		Materia.register(new Materia("Leather Binding",
				"leather_4",
				MateriaType.LEATHER,
				MateriaComponent.BINDING,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oEither sown for weapons or clothing, leather was a reliable choice.&7&o\""));
		Materia.register(new Materia("Leather Binding",
				"leather_5",
				MateriaType.LEATHER,
				MateriaComponent.BINDING,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oAn expert's touch makes all the difference.. Next!&7&o\""));

	}
}
