package com.eol.materia.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;

public class Catalysts 
{
	public static void load()
	{
		//-------------------------------BASIC CATALYSTS-------------------------------//
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_1",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.ONE,
				true,
				"&r&7&oAn echo of the distant past. Faint harmonic frequencies hum softly..",
				"&r&7&oThere's a memory engram of a fallen warrior embedded within:",
				"&r&7&o\"War is coming. Be on your guard...\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_2",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.TWO,
				true,
				"&r&7&oAn echo of the distant past. Faint harmonic frequencies hum softly..",
				"&r&7&oThere's a memory engram of a fallen warrior embedded within:",
				"&r&7\"&6&oThe capital is under attack?! Quick, the gates-- guards, the gates!",
				"&r&6&oWhere is Her Majesty? What do you mean she's missing? Heavens me..&r&7&o\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_3",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.THREE,
				true,
				"&r&7&oAn echo of the distant past. Faint harmonic frequencies hum softly..",
				"&r&7&oThere's a memory engram of a fallen warrior embedded within:",
				"&r&7\"&b&oGods if I weren't so foolish, my people would be safe from them..",
				"&r&b&oPlease Luminus, there's not much time-- you must find me!&r&7&o\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_4",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FOUR,
				true,
				"&r&7&oAn echo of the distant past. Faint harmonic frequencies hum softly..",
				"&r&7&oThere's a memory engram of a fallen warrior embedded within:",
				"&r&7\"&d&oWhere is she?! You- Where is Her Majesty, Lumina! 'I saw something",
				"&r&d&odash out over there!'... Thank you, I'm coming, sister!&r&7&o\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_5",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Faint harmonic frequencies hum softly..",
				"&r&7&oThere's a memory engram of a fallen warrior embedded within:",
				"&r&7\"&e&oIt was already too late. Lumina was discovered unlikely deceased in her study.",
				"&r&e&oBefore long, The Anti-Light Legion swarmed Fantasia Major. Within hours,",
				"&r&e&oGoblin swarm was at the capital gates. Due to Her Majesty's connection with the",
				"&r&e&odivine, 'Magic' as we know it ceased. And this wasn't random, it was fated.",
				"&r&e&oEveryone quickly realized: this was the curtain rising. On the final act.&r&7&o\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_6",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Strong harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				"&r&7\"&c&oSystem logs confirm Time-of-Death for light-core 'Lumina'.",
				"&r&c&ooccurred moments before her brother’s arrival. No additional eyewitness",
				"&r&c&oaccounts were recorded. Preliminary processing suggests heretical origins;",
				"&r&c&ointernal assassination remains unlikely. Data analysis will continue until",
				"&r&c&oconclusive evidence is obtained. Unmount and place Project Fantasia into",
				"&r&c&ostasis pending further instruction.&r&7&o\""));
		Materia.register(new Materia(
				"&e&lCreation Catalyst",
				"catalyst_7",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Unstable waveform emanates..",
				"&r&7&oThere's a memory engram encoded within. It's a memory of.. you?",
				"&r&7&o\"&r&3&oWell this.. this probably shouldn't exist.&r&7&o\""));
		
		//-------------------------------EOL CATALYSTS-------------------------------//
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.CELESTIO)+Symbols.EOL+"cho Of Luminus",
				"echo_of_luminus",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				"&r&e&oMy sweet sister.. thank you for watching over me, even from beyond.",
				"&r&e&oSince we were children, &r&e&l&ngold&r&e&o was always the metal I admired most--",
				"&r&e&oyou always said it reminded you of morning light. Do you remember",
				"&r&e&othe day Father took us into the woods to hunt for &r&e&l&nrabbit pelt&r&e&o?",
				"&r&e&oYou were so patient teaching me the prayers of &r&e&l&nCelestio&r&e&o,",
				"&r&e&othe same magic you devoted yourself to until the very end.",
				"&r&e&oI carry these memories like a forge carries heat. I miss you, sister.."));
		
	}
}
