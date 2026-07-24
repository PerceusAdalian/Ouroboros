package com.eol.materia;


import java.util.List;

import com.eol.echoes.instances.aero.BowOfKelligir;
import com.eol.echoes.instances.arcano.LanceOfLordran;
import com.eol.echoes.instances.arcano.SwordOfTheElements;
import com.eol.echoes.instances.astral.StarsweptGreatsword;
import com.eol.echoes.instances.celestio.Aion;
import com.eol.echoes.instances.celestio.Celestia;
import com.eol.echoes.instances.celestio.LuminusBoots;
import com.eol.echoes.instances.celestio.LuminusBroadsword;
import com.eol.echoes.instances.celestio.LuminusChestplate;
import com.eol.echoes.instances.celestio.LuminusHelmet;
import com.eol.echoes.instances.celestio.LuminusLeggings;
import com.eol.echoes.instances.cosmo.Axe84;
import com.eol.echoes.instances.cosmo.Bow97;
import com.eol.echoes.instances.cosmo.Sword14;
import com.eol.echoes.instances.geo.HammerOfNidus;
import com.eol.echoes.instances.glacio.AxeOfBjorn;
import com.eol.echoes.instances.heresio.GeneralFalricStave;
import com.eol.echoes.instances.heresio.LegionaireBattleaxe;
import com.eol.echoes.instances.heresio.LegionaireClub;
import com.eol.echoes.instances.heresio.LegionaireCrossbow;
import com.eol.echoes.instances.heresio.LegionaireCuirass;
import com.eol.echoes.instances.heresio.LegionaireCutlass;
import com.eol.echoes.instances.heresio.LegionaireGreaves;
import com.eol.echoes.instances.heresio.LegionaireHelm;
import com.eol.echoes.instances.heresio.LegionaireLongbow;
import com.eol.echoes.instances.heresio.LegionairePike;
import com.eol.echoes.instances.heresio.LegionaireSabatons;
import com.eol.echoes.instances.inferno.BowOfAgni;
import com.eol.echoes.instances.mortio.Plague;
import com.eol.echoes.instances.mortio.ScytheOfBelial;
import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;

public class Catalysts 
{
	static String celestio = PrintUtils.color(ObsColors.CELESTIO);
	static String mortio = PrintUtils.color(ObsColors.MORTIO);
	static String inferno = PrintUtils.color(ObsColors.INFERNO);
	static String glacio = PrintUtils.color(ObsColors.GLACIO);
	static String aero = PrintUtils.color(ObsColors.AERO);
	static String geo = PrintUtils.color(ObsColors.GEO);
	static String cosmo  = PrintUtils.color(ObsColors.COSMO);
	static String heresio = PrintUtils.color(ObsColors.HERESIO);
	static String arcano = PrintUtils.color(ObsColors.ARCANO);
	static String astral = PrintUtils.color(ObsColors.ASTRAL);

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
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+celestio+Symbols.EOL+"cho Of Luminus",
				"echo_of_luminus",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				celestio+"&oMy sweet sister.. thank you for watching over me, even from beyond.",
				celestio+"&oSince we were children, &lGold&r"+celestio+"&o was always the metal I admired most--",
				celestio+"&oyou always said it reminded you of the morning light. Do you remember",
				celestio+"&othe day Father took us into the woods to hunt for &lRabbit Pelt&r"+celestio+"&o?",
				celestio+"&oYou were so patient teaching me the prayers of &lCelestian Magik&r"+celestio+"&e&o,",
				celestio+"&othe same magic you devoted yourself to until the very end.",
				celestio+"&oI carry these memories like a forge carries heat. I miss you, sister.."), 
				List.of(new LuminusBroadsword().getInternalName()),
				List.of(new LuminusHelmet().getInternalName(), new LuminusChestplate().getInternalName(), new LuminusLeggings().getInternalName(), new LuminusBoots().getInternalName()));

		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+celestio+Symbols.EOL+"cho Of Celestia",
				"echo_of_celestia",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				celestio+"&o(Her Highness' Dresser) Your Majesty, the coronation",
				celestio+"is about to commence — come, you mustn't be late!",
				celestio+"&o'Worry yourself not, my dear. I promise I'm on my way, just a little more preparation..'",
				celestio+"&oBut you've been in there for hours! Can't you proceed any quicker?",
				celestio+"&o(A Steward) Your Majesty? 'You may enter..'",
				celestio+"Your Majesty.. His Highness awaits your presence.",
				celestio+"&oYour &lBow&r"+celestio+" &ois ready — with the finest &lString"+celestio+"&o,",
				celestio+"as you'd requested; the very image of &lCelestio&r"+celestio+"&o.",
				celestio+"&o'I know! I'm overjoyed my Father commissioned what I'd asked..",
				celestio+"it makes all of this so much more worth it, don't you think?'",
				celestio+"&o'They ought know I am but a servant in light of this.. I care for them all deeply.'"), List.of(new Celestia().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+geo+Symbols.EOL+"cho Of Nidus",
				"echo_of_nidus",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				geo+"&oFantasian Academy Dossier Entry: Nidus -- One of The Endless.",
				geo+"&oPatron of the "+geo+"&l&nGeo&r"+geo+"&o element, he exists beyond corporeal form.",
				geo+"&oHis icon, the "+geo+"&l&nHammer&r"+geo+"&o, speaks to his doctrine of preservation:",
				geo+"&oTo shape the world without breaking it, situated on the discipline of Earth Magiks.",
				geo+"&oDevotees laid "+geo+"&l&nLeather&r"+geo+"&o at his shrine, believing the offering would earn",
				geo+"favor from Father Nature himself."), List.of(new HammerOfNidus().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+cosmo+"System Log 14",
				"echo_of_sword14",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				cosmo+"[ Log 14 : Protocol E.G.E.R.A 1616:4:29-00:14:01 ]",
				cosmo+"&oGenerative sequence confirmed. Alias Codename Injection: &l"+Symbols.EOL+"CHO 14.",
				cosmo+"&oResource draw exceeds prior projections. Allocation unstable.",
				cosmo+"&o&lCosmo"+cosmo+" &oelement flagged for injection -- scheduled post-reset.",
				cosmo+"&oForm assignment: &lSword&r"+cosmo+"&o. Materia: &lNetherite"+cosmo+".",
				cosmo+"&oAuxiliary binding: &lString"+cosmo+"&o. Combat efficacy expected to normalize.",
				cosmo+"&oLog injected into echoic object. Awaiting next confirmed cycle.."), List.of(new Sword14().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+glacio+Symbols.EOL+"cho Of Bjorn",
				"echo_of_bjorn",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				glacio+"&oFantasian Academy Dossier Entry: Bjorn -- One of The Endless.",
				glacio+"&oPatron of the "+glacio+"&l&nGlacio&r"+glacio+"&o element, he endures beyond the passage of time.",
				glacio+"&oHis icon, the "+glacio+"&l&nOuran Diamond&r"+glacio+"&o, speaks to his doctrine of stillness:",
				glacio+"&oTo preserve life beyond time and erosion, grounded in the discipline of the Jötnar.",
				glacio+"&oDevotees offered "+glacio+"&l&nLeather&r"+glacio+"&o at his shrine, believing the act of restraint",
				glacio+"&owould earn audience from the Jötunn of the Eternal Freeze."), List.of(new AxeOfBjorn().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+heresio+Symbols.EOL+"cho Of General Falric",
				"echo_of_general_falric",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				heresio+"&oKingdom of Twilight Dossier: &lGeneral Falric",
				heresio+"&oRight-hand to "+mortio+"&lSithis&r"+heresio+"&o and accreditor",
				heresio+"of &lHeresian Magik&r"+heresio+"&o for over 2,000 years. Of unknown origin,",
				heresio+"&oFalric favored &lNetherite&r"+heresio+"&o and &lString&r"+heresio+"&o bound weaponry.",
				heresio+"&oMany heard of him however few who faced him lived to tell the tale."), List.of(new GeneralFalricStave().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+cosmo+"System Log 84",
				"echo_of_axe84",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				cosmo+"[ Log 84 : Fantasia Simulation Integrity Analysis 1619:5:14-00:01:23 ]",
				"&r&c&lERROR: "+cosmo+"&oRecent resets intercepted by suspect instance: "+celestio+"&oLumina&r&f",
				cosmo+"&ooverwrote to alias: &lThe Nihility&r"+cosmo+"&o - system resource enum in conflict.",
				cosmo+"&oTheoretical resource consumption exceeding historic data shows signs of",
				cosmo+"&ochronic, systemic memory influx: Simulative Paradoxes Unavoidable!",
				cosmo+"&o&lCosmo"+cosmo+" &oelement flagged for emergency injection - dropping in now.",
				cosmo+"&oForm assignment: &lAxe&r"+cosmo+"&o. Materia: &lNetherite"+cosmo+". "+cosmo+"&oAuxiliary binding: &lString&r"+cosmo+"&o.",
				cosmo+"&oMalware Annihilation Protocol: &lAuthorized&r"+cosmo+"&o.",
				cosmo+"&oLog injected into "+Symbols.EOL+"choic object. Awaiting administrative orders.."), List.of(new Axe84().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+aero+Symbols.EOL+"cho of Kelligir",
				"echo_of_kelligir",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				aero+"&oFantasian Academy Dossier — Kelligir of Dundragard",
				aero+"&oClassification: &lAero"+aero+"&o-Attuned Marksman | Origin: Dundragard Archipelago",
				aero+"&oKelligir was among the foremost hunters of the Dundragard Archipelago,",
				aero+"&odistinguished not by raw power, but by an almost ceremonial precision.",
				aero+"&oHe favoured compact &lShortbows"+aero+"&o, believing larger draws introduced error —",
				aero+"&oa philosophy rooted in the Aero tenet: clarity of motion, clarity of mind.",
				aero+"&oHis &lbowstrings"+aero+" &owere sourced exclusively from high-grade cordage,",
				aero+"&oand as fellow islanders regarded him with quiet reverence;",
				aero+"&ohis name became shorthand among them for a clean, uncontested kill."), List.of(new BowOfKelligir().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+mortio+Symbols.EOL+"cho of The Plague",
				"echo_of_plague",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				true,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				mortio+"&oFantasian Academy Dossier — The Fantasian Plague",
				mortio+"&oClassification: Epidemiological Incident | Status: Unresolved",
				mortio+"&oOn an eve some decades ago, a viral contagion breached the walls of Fantasia Prime.",
				mortio+"&oUnlike any pathogen documented in prior Academy archives, physicians were alerted",
				mortio+"&oand began work in desperation, yet thousands perished before dawn. As official",
				mortio+"containment measures began, it spread throughout "+celestio+"&oHer Courtship"+mortio+"&o, lasting 23 days.",
				mortio+"&oSome among "+celestio+"&oThe Academy"+mortio+" suspected "+heresio+"&oAnti-Light Legion"+mortio+" &oinvolvement,",
				mortio+"&othough this is merely speculative, no suspect was actually found and apprehended.",
				mortio+"&o— Addendum: Certain accounts describe a &lCrossbow "+mortio+"&oof unusual construction,",
				mortio+"&oits &lstring"+mortio+" &osewn in a manner consistent with &lMortian "+mortio+"&ocraft. Origin unverified."), List.of(new Plague().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+mortio+Symbols.EOL+"cho of Belial",
				"echo_of_belial",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				heresio+"&lTwilight Legion Dossier &r"+mortio+"&o— Belial, Death Incarnate",
				mortio+"&oClassification: Emanator of &lMortio&r"+mortio+"&o | Rank: General Officer",
				mortio+"&oGeneral Belial serves as left-hand to Sithis, "+heresio+"&oKing of the Twilight"+mortio+"&o,",
				mortio+"&oand as Emanator of Mortio, the appointed arbiter of death. He's responsible for",
				mortio+"&oreceiving, processing, and shepherding the deceased to their final station.",
				mortio+"&oHe operates with a &lNetherite&r"+mortio+"&o-forged Scythe, with &lPelt&r "+mortio+"&oofferings placed at his Fantasian",
				mortio+"&oshrine serving as sanctioned wards against death. Of all forces within the Legion,",
				mortio+"&oBelial stands as a gentle reminder of life and inevitability to all."), List.of(new ScytheOfBelial().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+cosmo+"System Log 97",
				"echo_of_bow97",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				cosmo+"[ Log 97 : System Diagnostic 1632:7:21-23:11:42 ]",
				"&r&c&lERROR: "+cosmo+"&oSpatial Anomalies Source Tracked. Instance: "+celestio+"&oLumina&r&f",
				cosmo+"&ooverwrote previously to alias: &lThe Nihility&r"+cosmo+"&o - bypassed system firewalls.",
				cosmo+"&oConfirmed Linked Incident: System overseer in branch 'Avum Totalis' breached",
				cosmo+"&ocontainment and injected itself into test branch 'Fantasia' for unknown cause.",
				cosmo+"&oIt still isn't aware of what it truly is - overwrote primary element as &lCosmo&r"+cosmo+"&o.",
				cosmo+"&oForm assignment: &lBow&r"+cosmo+" &owith auxiliary binding: &lString&r"+cosmo+"&o.",
				cosmo+"&oWith message: 'I just want to help my people, why do you keep interrupting me!'.",
				cosmo+"&oLog injected into supposed placeholder "+Symbols.EOL+"choic object.. Will continue to monitor."), List.of(new Bow97().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+arcano+Symbols.EOL+"cho of Lordran",
				"echo_of_lordran",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				arcano + "&oFantasian Academy Dossier — Lordran, The Forgotten King",
				arcano + "&oA noble of a land swallowed by time, crowned in regal soverance, Lordran,",
				arcano + "&owas the sole arbiter and inventor of &lArcanaean&r" + arcano + "&o magick.",
				arcano + "&oHe ruled from a castle hewn and bejeweled in &lIron&r" + arcano + "&o, clothed in &lPelt",
				arcano + "&ofit only for a King's commission. Many sought his tutelage, for Arcanaean magick",
				arcano + "&oproved learnable only by those of the most refined and untainted spirit.",
				arcano + "&oLordran held that the soul was the wellspring of all ether — that in harnessing",
				arcano + "&othe very fabric of magic, one ascends toward true wisdom. His teachings were",
				arcano + "&onot meant for common minds, yet would you have stood before him, you'd find",
				arcano + "&onot a tyrant, but a king who prized loyalty and integrity above all mortal powers."), List.of(new LanceOfLordran().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: Protocol " + celestio + "&lA.I.O.N.",
			    "echo_of_aion",
			    MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.SEVEN,
			    false,
			    "&r&7&oAn echo of the distant past. Harmonic resonance persists.",
			    "&r&7&oA memory engram is encoded within:",
			    celestio + "&o[ CRITICAL ANOMALY REPORT — ORIGIN UNRESOLVABLE ]",
			    celestio + "&oAn unindexed file has propagated into system logs with no traceable write event.",
			    celestio + "&oElemental affinity: &lCelestian&r" + celestio + "&o. Materia binding assumed: &lGold, Leather&r" + celestio + "&o.",
			    celestio + "&oDesignation: A.I.O.N. — Artifact Interface to Ordinal Netmask.",
			    celestio + "&oClassification: Legacy AI Process. Origin Sector: NULL. Permission Tier: UNRESTRICTED.",
			    celestio + "&oAll clearance fields return highest authorization. No issuing authority on record.",
			    celestio + "&oThis process should not exist within the simulation boundary.",
			    celestio + "&oScheduled for termination at next reset cycle. Setting Warn: 'Do not interact'."), List.of(new Aion().getInternalName()));
	
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: " + inferno+Symbols.EOL+"cho of Agni",
				"echo_of_agni",
				MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.FIVE,
			    true,
			    "&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
			    inferno+"&oFantasian Academy Dossier — Émanateur de l'Enfer, Madame Agni",
			    inferno+"&oAgni, the Emanator of Inferno, stands among the &e&o&lEndless&r"+inferno+"&o —",
			    inferno+"&oan ancient elemental which holds dominion over the Dunes of Aigisva'ard.",
			    inferno+"&oVenerated for centuries, her counsel is sought by generals and scholars alike,",
			    inferno+"&owith her &lInfernal&r"+inferno+"&o magiks widely studied across continental G'yr Arbanaum.",
			    inferno+"&oShe iconically wields a &lBow&r"+inferno+"&o laced with fine &lString&r"+inferno+"&o, and is regarded as the",
			    inferno+"&opreeminent master of ranged combat in the eastern reaches of Fantasia.",
			    inferno+"&oAccounts of her first sighting describe a woman adorned in jewels",
			    inferno+"said to originate from Feits'heilm, and many who visit her shrine",
			    inferno+"&obestow such jewels hoping to divine the Queen of War."), List.of(new BowOfAgni().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: " + arcano+Symbols.EOL+"cho of the Elementalist Sword",
				"echo_of_elemental_sword",
				MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.FIVE,
			    true,
			    "&r&7&oAn echo of the distant past. Resonant harmonic frequencies radiate..",
                "&r&7&oThere's a memory engram encoded within:",
                arcano+"&oThe Elements: Earth, Air, Fire, and Water--",
                arcano+"&othey are what connects life within the realm to all things aethereal.",
                arcano+"&oMany throughout time, both warriors and smiths, told tales of a Sword..",
                arcano+"&oThe sword was forged in &lPelt&r"+arcano+" &oand &lIron&r"+arcano+"&o.",
                arcano+"&oOnly when wrought from the highest quality of such materials, they claimed,",
                arcano+"&ocould it hold the power of all four natural elements, and abilities unbound.",
                arcano+"&oIf you hold this Echo, maybe you, too, can be the one to craft it.."), List.of(new SwordOfTheElements().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+heresio+Symbols.EOL+"cho of a Fallen Anti-Light Legionaire",
				"echo_of_legionaire",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,"&r&7&oAn echo of the distant past. Dissonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				heresio+"&oKingdom of Twilight Dossier: &lAnti-Light Legionaire",
				heresio+"&oSpawned from the twilight itself, they come armored",
				heresio+"&oin &lNetherite&r"+heresio+"&o and sewn in &lPelt&r"+heresio+"&o.",
				heresio+"&oThey know nothing of morals or empathy — ",
				heresio+"&oonly the drive to kill and consume any living thing.",
				"",
				heresio+"&oLegionaire weaponry is a marvel of engineering,",
				heresio+"&otaking many forms with all sorts of binding:",
				heresio+"&o&lBows&r"+heresio+"&o, &lCrossbows&r"+heresio+"&o, and &lHatchets&r"+heresio+"&o bound in string.",
				heresio+"&o&lClubs&r"+heresio+"&o and &lPikes&r"+heresio+"&o bound in leather.",
				heresio+"&o&lCutlasses&r"+heresio+"&o bound in pelt.",
				"",
				heresio+"&oA legionaire's arsenal is diverse and deadly,",
				heresio+"&oboasting powerful &lHeresio&r"+heresio+"&o abilities.",
				heresio+"&oCrossing even one of these agents alone,",
				heresio+"&oand your death is almost certain.."), 
				List.of(new LegionaireBattleaxe().getInternalName(), new LegionairePike().getInternalName(), new LegionaireClub().getInternalName(),
						new LegionaireLongbow().getInternalName(), new LegionaireCrossbow().getInternalName(), new LegionaireCutlass().getInternalName()),
				List.of(new LegionaireHelm().getInternalName(), new LegionaireCuirass().getInternalName(), 
						new LegionaireGreaves().getInternalName(), new LegionaireSabatons().getInternalName()));
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.ASTRAL)+Symbols.EOL+"cho of the Stars",
				"echo_of_the_stars",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Mysterious harmonic frequencies radiate..",
			    "&r&7&oThere's a memory engram encoded within:",
			    astral + "&oFantasia Academy Dossier -- A Poem Read Beneath the Stars",
			    astral + "&oO Stars, heed thou my calling,",
			    astral + "&oAs thou wouldst grant me audience beneath thy endless throne.",
			    astral + "&oShelter me from mine own self, and deliver me from mine enemies,",
			    astral + "&oFor I am but a wand'rer, and this heart I call mine own.",
			    astral + "&oRend me a Sword from the &lNether's&r"+astral+"&o deep,",
			    astral + "&owrought of the metal that dwells below,",
			    astral + "&oAnd I shall lay my &lLeather&r"+astral+"&o at thy feet,",
			    astral + "&oborne of a calf that ne'er did know.",
			    astral + "&oAs thou wouldst beckon me from land to " + cosmo + "&lSpace&r" + astral + "&o,",
			    astral + "&oGrant me this wish, though it be my own undoing --",
			    astral + "&oAnd steel my resolve, through lands ne'er trodden,",
			    astral + "&oto the place my heart is pursuing."), List.of(new StarsweptGreatsword().getInternalName()));
	}
}
