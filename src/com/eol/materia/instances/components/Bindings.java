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
		
		//-------------------------------SINEW-------------------------------//
		
		//-------------------------------LEATHER-------------------------------//
	}
}
