package com.eol.materia.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.enums.Rarity;

public class Bases 
{
	public static void load()
	{
		//-------------------------------WOOD-------------------------------//
		Materia.register(new Materia("Wooden Chunk",
				"wooden_chunk_1",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA chipped wooden chunk.. nothing to write home about."));
		Materia.register(new Materia("Wooden Chunk",
				"wooden_chunk_2",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oThey came in hoards, even wooden swords had their use.&7&o\""));
		Materia.register(new Materia("Wooden Chunk",
				"wooden_chunk_3",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oWith the last slain, more came. We had no other choice.&7&o\""));
		Materia.register(new Materia("Wooden Chunk",
				"wooden_chunk_4",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oPitiful knight. Don't you know wood warps? Stand up, and fight!&7&o\""));
		Materia.register(new Materia("Wooden Chunk",
				"wooden_chunk_5",
				MateriaType.WOOD,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA carpenter's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------STONE-------------------------------//
		Materia.register(new Materia("Stone Chunk",
				"stone_chunk_1",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA chipped stone chunk.. nothing to write home about."));
		Materia.register(new Materia("Stone Chunk",
				"stone_chunk_2",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oSome chose wood.. smart people chose weight.&7&o\""));
		Materia.register(new Materia("Stone Chunk",
				"stone_chunk_3",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oEven the dullest of blades can bash someone's head in.&7&o\""));
		Materia.register(new Materia("Stone Chunk",
				"stone_chunk_4",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oThis could pack a serious punch.. Oh look, a zombie!&7&o\""));
		Materia.register(new Materia("Stone Chunk",
				"stone_chunk_5",
				MateriaType.STONE,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA mason's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------COPPER-------------------------------//
		Materia.register(new Materia("Copper Nugget",
				"copper_nugget_1",
				MateriaType.COPPER,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA simple copper nugget.. nothing to write home about."));
		Materia.register(new Materia("Copper Nugget",
				"copper_nugget_2",
				MateriaType.COPPER,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oEven salvaged copper had its uses..&7&o\""));
		Materia.register(new Materia("Copper Nugget",
				"copper_nugget_3",
				MateriaType.COPPER,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oHomes were built with copper, so were our swords.&7&o\""));
		Materia.register(new Materia("Copper Nugget",
				"copper_nugget_4",
				MateriaType.COPPER,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oWe salvaged everything we could, down to the insulation.&7&o\""));
		Materia.register(new Materia("Copper Nugget",
				"copper_nugget_5",
				MateriaType.COPPER,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oAn thurgist's hard work paid off.. Next!&7&o\""));

		//-------------------------------IRON-------------------------------//
		Materia.register(new Materia("Iron Slag",
				"iron_slag_1",
				MateriaType.IRON,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA shilling of iron slag.. nothing to write home about."));
		Materia.register(new Materia("Iron Slag",
				"iron_slag_2",
				MateriaType.IRON,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oSome slag and paste, and you got a 'sword'..&7&o\""));
		Materia.register(new Materia("Iron Slag",
				"iron_slag_3",
				MateriaType.IRON,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oIf slicing didn't cut it, we'd bludgeon them.&7&o\""));
		Materia.register(new Materia("Iron Slag",
				"iron_slag_4",
				MateriaType.IRON,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oOur smiths quickly became used to bulk orders..&7&o\""));
		Materia.register(new Materia("Iron Slag",
				"iron_slag_5",
				MateriaType.IRON,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA blacksmith's hard work paid off.. Next!&7&o\""));
		
		//-------------------------------GOLD-------------------------------//
		Materia.register(new Materia("Gold Nugget",
				"gold_nugget_1",
				MateriaType.GOLD,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA simple gold nugget.. nothing to write home about."));
		Materia.register(new Materia("Gold Nugget",
				"gold_nugget_2",
				MateriaType.GOLD,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oPrecious in currency and in piercing some skulls.&7&o\""));
		Materia.register(new Materia("Gold Nugget",
				"gold_nugget_3",
				MateriaType.GOLD,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oGold was seldom used as decoration but payment for.. services.&7&o\""));
		Materia.register(new Materia("Gold Nugget",
				"gold_nugget_4",
				MateriaType.GOLD,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oNo one knows for certain where pure Luminite Ore came",
				"&r&d&ofrom but it had its uses for pretty much everything.&7&o\""));
		Materia.register(new Materia("Gold Nugget",
				"gold_nugget_5",
				MateriaType.GOLD,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA master goldsmith's work is unmatched.. Next!&7&o\""));

		//-------------------------------DIAMOND-------------------------------//
		Materia.register(new Materia("Ouran Diamond",
				"diamond_1",
				MateriaType.DIAMOND,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA low karat diamond.. nothing to write home about."));
		Materia.register(new Materia("Ouran Diamond",
				"diamond_2",
				MateriaType.DIAMOND,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oEven the lower quality diamonds was a step up from gold..&7&o\""));
		Materia.register(new Materia("Ouran Diamond",
				"diamond_3",
				MateriaType.DIAMOND,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oOuran Diamonds: collateral, currency, and status symbol.&7&o\""));
		Materia.register(new Materia("Ouran Diamond",
				"diamond_4",
				MateriaType.DIAMOND,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oExclusive to Solthur, Ouran Diamonds were a sight to behold..&7&o\""));
		Materia.register(new Materia("Ouran Diamond",
				"diamond_5",
				MateriaType.DIAMOND,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oA master gemcutter's work is unmatched.. Next!&7&o\""));

		//-------------------------------NETHERITE-------------------------------//
		Materia.register(new Materia("Netherite Scrap",
				"netherite_scrap_1",
				MateriaType.NETHERITE,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oSome imperfect netherite scrap.. nothing to write home about."));
		Materia.register(new Materia("Netherite Scrap",
				"netherite_scrap_2",
				MateriaType.NETHERITE,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oThose of us that treked into Hell came out with some",
				"&r&6&ovaluable resources. Netherite was one of them.&7&o\""));
		Materia.register(new Materia("Netherite Scrap",
				"netherite_scrap_3",
				MateriaType.NETHERITE,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oNetherite was used for the most elite weapons and armor.&7&o\""));
		Materia.register(new Materia("Netherite Scrap",
				"netherite_scrap_4",
				MateriaType.NETHERITE,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oNetherite: Protective, durable, and unyieldingly powerful.&7&o\""));
		Materia.register(new Materia("Netherite Scrap",
				"netherite_scrap_5",
				MateriaType.NETHERITE,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oOnly the highest quality for Her Majesty.. Next!&7&o\""));

		//-------------------------------LEATHER-------------------------------//
		Materia.register(new Materia("Hide",
				"hide_1",
				MateriaType.LEATHER,
				MateriaComponent.BASE,
				Rarity.ONE,
				false,
				"&r&7&oA simple woven hide.. nothing to write home about."));
		Materia.register(new Materia("Hide",
				"hide_2",
				MateriaType.LEATHER,
				MateriaComponent.BASE,
				Rarity.TWO,
				false,
				"&7&o\"&6&oHides were a common material for clothing and armor.&7&o\""));
		Materia.register(new Materia("Hide",
				"hide_3",
				MateriaType.LEATHER,
				MateriaComponent.BASE,
				Rarity.THREE,
				false,
				"&7&o\"&b&oWe used what we had, and it was enough.&7&o\""));
		Materia.register(new Materia("Hide",
				"hide_4",
				MateriaType.LEATHER,
				MateriaComponent.BASE,
				Rarity.FOUR,
				false,
				"&7&o\"&d&oWhen quality was paramount, hide proved to be a reliable choice.&7&o\""));
		Materia.register(new Materia("Hide",
				"hide_5",
				MateriaType.LEATHER,
				MateriaComponent.BASE,
				Rarity.FIVE,
				false,
				"&7&o\"&e&oAn artisan's touch made all the difference.. Next!&7&o\""));

	}
}
