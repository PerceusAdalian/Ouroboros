package com.eol.materia;


import java.util.List;

import com.eol.echoes.instances.aero.BowOfKelligir;
import com.eol.echoes.instances.arcano.LanceOfLordran;
import com.eol.echoes.instances.arcano.SwordOfTheElements;
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
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.CELESTIO)+"&oMy sweet sister.. thank you for watching over me, even from beyond.",
				PrintUtils.color(ObsColors.CELESTIO)+"&oSince we were children, &lGold&r"+PrintUtils.color(ObsColors.CELESTIO)+"&o was always the metal I admired most--",
				PrintUtils.color(ObsColors.CELESTIO)+"&oyou always said it reminded you of the morning light. Do you remember",
				PrintUtils.color(ObsColors.CELESTIO)+"&othe day Father took us into the woods to hunt for &lRabbit Pelt&r"+PrintUtils.color(ObsColors.CELESTIO)+"&o?",
				PrintUtils.color(ObsColors.CELESTIO)+"&oYou were so patient teaching me the prayers of &lCelestian Magik&r"+PrintUtils.color(ObsColors.CELESTIO)+"&e&o,",
				PrintUtils.color(ObsColors.CELESTIO)+"&othe same magic you devoted yourself to until the very end.",
				PrintUtils.color(ObsColors.CELESTIO)+"&oI carry these memories like a forge carries heat. I miss you, sister.."), 
				new LuminusBroadsword().getInternalName(),
				List.of(new LuminusHelmet().getInternalName(), new LuminusChestplate().getInternalName(), new LuminusLeggings().getInternalName(), new LuminusBoots().getInternalName()));

		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.CELESTIO)+Symbols.EOL+"cho Of Celestia",
				"echo_of_celestia",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.CELESTIO)+"&o(Her Highness' Dresser) Your Majesty, the coronation",
				PrintUtils.color(ObsColors.CELESTIO)+"is about to commence — come, you mustn't be late!",
				PrintUtils.color(ObsColors.CELESTIO)+"&o'Worry yourself not, my dear. I promise I'm on my way, just a little more preparation..'",
				PrintUtils.color(ObsColors.CELESTIO)+"&oBut you've been in there for hours! Can't you proceed any quicker?",
				PrintUtils.color(ObsColors.CELESTIO)+"&o(A Steward) Your Majesty? 'You may enter..'",
				PrintUtils.color(ObsColors.CELESTIO)+"Your Majesty.. His Highness awaits your presence.",
				PrintUtils.color(ObsColors.CELESTIO)+"&oYour &lBow&r"+PrintUtils.color(ObsColors.CELESTIO)+" &ois ready — with the finest &lString"+PrintUtils.color(ObsColors.CELESTIO)+"&o,",
				PrintUtils.color(ObsColors.CELESTIO)+"as you'd requested; the very image of &lCelestio&r"+PrintUtils.color(ObsColors.CELESTIO)+"&o.",
				PrintUtils.color(ObsColors.CELESTIO)+"&o'I know! I'm overjoyed my Father commissioned what I'd asked..",
				PrintUtils.color(ObsColors.CELESTIO)+"it makes all of this so much more worth it, don't you think?'",
				PrintUtils.color(ObsColors.CELESTIO)+"&o'They ought know I am but a servant in light of this.. I care for them all deeply.'"), new Celestia().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.GEO)+Symbols.EOL+"cho Of Nidus",
				"echo_of_nidus",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.GEO)+"&oFantasian Academy Dossier Entry: Nidus -- One of The Endless.",
				PrintUtils.color(ObsColors.GEO)+"&oPatron of the "+PrintUtils.color(ObsColors.GEO)+"&l&nGeo&r"+PrintUtils.color(ObsColors.GEO)+"&o element, he exists beyond corporeal form.",
				PrintUtils.color(ObsColors.GEO)+"&oHis icon, the "+PrintUtils.color(ObsColors.GEO)+"&l&nHammer&r"+PrintUtils.color(ObsColors.GEO)+"&o, speaks to his doctrine of preservation:",
				PrintUtils.color(ObsColors.GEO)+"&oTo shape the world without breaking it, situated on the discipline of Earth Magiks.",
				PrintUtils.color(ObsColors.GEO)+"&oDevotees laid "+PrintUtils.color(ObsColors.GEO)+"&l&nLeather&r"+PrintUtils.color(ObsColors.GEO)+"&o at his shrine, believing the offering would earn",
				PrintUtils.color(ObsColors.GEO)+"favor from Father Nature himself."), new HammerOfNidus().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.COSMO)+"System Log 14",
				"echo_of_sword14",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.COSMO)+"[ Log 14 : Protocol E.G.E.R.A 1616:4:29-00:14:01 ]",
				PrintUtils.color(ObsColors.COSMO)+"&oGenerative sequence confirmed. Alias Codename Injection: &l"+Symbols.EOL+"CHO 14.",
				PrintUtils.color(ObsColors.COSMO)+"&oResource draw exceeds prior projections. Allocation unstable.",
				PrintUtils.color(ObsColors.COSMO)+"&o&lCosmo"+PrintUtils.color(ObsColors.COSMO)+" &oelement flagged for injection -- scheduled post-reset.",
				PrintUtils.color(ObsColors.COSMO)+"&oForm assignment: &lSword&r"+PrintUtils.color(ObsColors.COSMO)+"&o. Materia: &lNetherite"+PrintUtils.color(ObsColors.COSMO)+".",
				PrintUtils.color(ObsColors.COSMO)+"&oAuxiliary binding: &lString"+PrintUtils.color(ObsColors.COSMO)+"&o. Combat efficacy expected to normalize.",
				PrintUtils.color(ObsColors.COSMO)+"&oLog injected into echoic object. Awaiting next confirmed cycle.."), new Sword14().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.GLACIO)+Symbols.EOL+"cho Of Bjorn",
				"echo_of_bjorn",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.GLACIO)+"&oFantasian Academy Dossier Entry: Bjorn -- One of The Endless.",
				PrintUtils.color(ObsColors.GLACIO)+"&oPatron of the "+PrintUtils.color(ObsColors.GLACIO)+"&l&nGlacio&r"+PrintUtils.color(ObsColors.GLACIO)+"&o element, he endures beyond the passage of time.",
				PrintUtils.color(ObsColors.GLACIO)+"&oHis icon, the "+PrintUtils.color(ObsColors.GLACIO)+"&l&nOuran Diamond&r"+PrintUtils.color(ObsColors.GLACIO)+"&o, speaks to his doctrine of stillness:",
				PrintUtils.color(ObsColors.GLACIO)+"&oTo preserve life beyond time and erosion, grounded in the discipline of the Jötnar.",
				PrintUtils.color(ObsColors.GLACIO)+"&oDevotees offered "+PrintUtils.color(ObsColors.GLACIO)+"&l&nLeather&r"+PrintUtils.color(ObsColors.GLACIO)+"&o at his shrine, believing the act of restraint",
				PrintUtils.color(ObsColors.GLACIO)+"&owould earn audience from the Jötunn of the Eternal Freeze."), new AxeOfBjorn().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.HERESIO)+Symbols.EOL+"cho Of General Falric",
				"echo_of_general_falric",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.HERESIO)+"&oKingdom of Twilight Dossier: &lGeneral Falric",
				PrintUtils.color(ObsColors.HERESIO)+"&oRight-hand to "+PrintUtils.color(ObsColors.MORTIO)+"&lSithis&r"+PrintUtils.color(ObsColors.HERESIO)+"&o and accreditor",
				PrintUtils.color(ObsColors.HERESIO)+"of &lHeresian Magik&r"+PrintUtils.color(ObsColors.HERESIO)+"&o for over 2,000 years. Of unknown origin,",
				PrintUtils.color(ObsColors.HERESIO)+"&oFalric favored &lNetherite&r"+PrintUtils.color(ObsColors.HERESIO)+"&o and &lString&r"+PrintUtils.color(ObsColors.HERESIO)+"&o bound weaponry.",
				PrintUtils.color(ObsColors.HERESIO)+"&oMany heard of him however few who faced him lived to tell the tale."), new GeneralFalricStave().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.COSMO)+"System Log 84",
				"echo_of_axe84",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.COSMO)+"[ Log 84 : Fantasia Simulation Integrity Analysis 1619:5:14-00:01:23 ]",
				"&r&c&lERROR: "+PrintUtils.color(ObsColors.COSMO)+"&oRecent resets intercepted by suspect instance: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&f",
				PrintUtils.color(ObsColors.COSMO)+"&ooverwrote to alias: &lThe Nihility&r"+PrintUtils.color(ObsColors.COSMO)+"&o - system resource enum in conflict.",
				PrintUtils.color(ObsColors.COSMO)+"&oTheoretical resource consumption exceeding historic data shows signs of",
				PrintUtils.color(ObsColors.COSMO)+"&ochronic, systemic memory influx: Simulative Paradoxes Unavoidable!",
				PrintUtils.color(ObsColors.COSMO)+"&o&lCosmo"+PrintUtils.color(ObsColors.COSMO)+" &oelement flagged for emergency injection - dropping in now.",
				PrintUtils.color(ObsColors.COSMO)+"&oForm assignment: &lAxe&r"+PrintUtils.color(ObsColors.COSMO)+"&o. Materia: &lNetherite"+PrintUtils.color(ObsColors.COSMO)+". "+PrintUtils.color(ObsColors.COSMO)+"&oAuxiliary binding: &lString&r"+PrintUtils.color(ObsColors.COSMO)+"&o.",
				PrintUtils.color(ObsColors.COSMO)+"&oMalware Annihilation Protocol: &lAuthorized&r"+PrintUtils.color(ObsColors.COSMO)+"&o.",
				PrintUtils.color(ObsColors.COSMO)+"&oLog injected into "+Symbols.EOL+"choic object. Awaiting administrative orders.."), new Axe84().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.AERO)+Symbols.EOL+"cho of Kelligir",
				"echo_of_kelligir",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.AERO)+"&oFantasian Academy Dossier — Kelligir of Dundragard",
				PrintUtils.color(ObsColors.AERO)+"&oClassification: &lAero"+PrintUtils.color(ObsColors.AERO)+"&o-Attuned Marksman | Origin: Dundragard Archipelago",
				PrintUtils.color(ObsColors.AERO)+"&oKelligir was among the foremost hunters of the Dundragard Archipelago,",
				PrintUtils.color(ObsColors.AERO)+"&odistinguished not by raw power, but by an almost ceremonial precision.",
				PrintUtils.color(ObsColors.AERO)+"&oHe favoured compact &lShortbows"+PrintUtils.color(ObsColors.AERO)+"&o, believing larger draws introduced error —",
				PrintUtils.color(ObsColors.AERO)+"&oa philosophy rooted in the Aero tenet: clarity of motion, clarity of mind.",
				PrintUtils.color(ObsColors.AERO)+"&oHis &lbowstrings"+PrintUtils.color(ObsColors.AERO)+" &owere sourced exclusively from high-grade cordage,",
				PrintUtils.color(ObsColors.AERO)+"&oand as fellow islanders regarded him with quiet reverence;",
				PrintUtils.color(ObsColors.AERO)+"&ohis name became shorthand among them for a clean, uncontested kill."), new BowOfKelligir().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.MORTIO)+Symbols.EOL+"cho of The Plague",
				"echo_of_plague",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.FIVE,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.MORTIO)+"&oFantasian Academy Dossier — The Fantasian Plague",
				PrintUtils.color(ObsColors.MORTIO)+"&oClassification: Epidemiological Incident | Status: Unresolved",
				PrintUtils.color(ObsColors.MORTIO)+"&oOn an eve some decades ago, a viral contagion breached the walls of Fantasia Prime.",
				PrintUtils.color(ObsColors.MORTIO)+"&oUnlike any pathogen documented in prior Academy archives, physicians were alerted",
				PrintUtils.color(ObsColors.MORTIO)+"&oand began work in desperation, yet thousands perished before dawn. As official",
				PrintUtils.color(ObsColors.MORTIO)+"containment measures began, it spread throughout "+PrintUtils.color(ObsColors.CELESTIO)+"&oHer Courtship"+PrintUtils.color(ObsColors.MORTIO)+"&o, lasting 23 days.",
				PrintUtils.color(ObsColors.MORTIO)+"&oSome among "+PrintUtils.color(ObsColors.CELESTIO)+"&oThe Academy"+PrintUtils.color(ObsColors.MORTIO)+" suspected "+PrintUtils.color(ObsColors.HERESIO)+"&oAnti-Light Legion"+PrintUtils.color(ObsColors.MORTIO)+" &oinvolvement,",
				PrintUtils.color(ObsColors.MORTIO)+"&othough this is merely speculative, no suspect was actually found and apprehended.",
				PrintUtils.color(ObsColors.MORTIO)+"&o— Addendum: Certain accounts describe a &lCrossbow "+PrintUtils.color(ObsColors.MORTIO)+"&oof unusual construction,",
				PrintUtils.color(ObsColors.MORTIO)+"&oits &lstring"+PrintUtils.color(ObsColors.MORTIO)+" &osewn in a manner consistent with &lMortian "+PrintUtils.color(ObsColors.MORTIO)+"&ocraft. Origin unverified."), new Plague().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.MORTIO)+Symbols.EOL+"cho of Belial",
				"echo_of_belial",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.HERESIO)+"&lTwilight Legion Dossier &r"+PrintUtils.color(ObsColors.MORTIO)+"&o— Belial, Death Incarnate",
				PrintUtils.color(ObsColors.MORTIO)+"&oClassification: Emanator of &lMortio&r"+PrintUtils.color(ObsColors.MORTIO)+"&o | Rank: General Officer",
				PrintUtils.color(ObsColors.MORTIO)+"&oGeneral Belial serves as left-hand to Sithis, "+PrintUtils.color(ObsColors.HERESIO)+"&oKing of the Twilight"+PrintUtils.color(ObsColors.MORTIO)+"&o,",
				PrintUtils.color(ObsColors.MORTIO)+"&oand as Emanator of Mortio, the appointed arbiter of death. He's responsible for",
				PrintUtils.color(ObsColors.MORTIO)+"&oreceiving, processing, and shepherding the deceased to their final station.",
				PrintUtils.color(ObsColors.MORTIO)+"&oHe operates with a &lNetherite&r"+PrintUtils.color(ObsColors.MORTIO)+"&o-forged Scythe, with &lPelt&r "+PrintUtils.color(ObsColors.MORTIO)+"&oofferings placed at his Fantasian",
				PrintUtils.color(ObsColors.MORTIO)+"&oshrine serving as sanctioned wards against death. Of all forces within the Legion,",
				PrintUtils.color(ObsColors.MORTIO)+"&oBelial stands as a gentle reminder of life and inevitability to all."), new ScytheOfBelial().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.COSMO)+"System Log 97",
				"echo_of_bow97",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SIX,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.COSMO)+"[ Log 97 : System Diagnostic 1632:7:21-23:11:42 ]",
				"&r&c&lERROR: "+PrintUtils.color(ObsColors.COSMO)+"&oSpatial Anomalies Source Tracked. Instance: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&f",
				PrintUtils.color(ObsColors.COSMO)+"&ooverwrote previously to alias: &lThe Nihility&r"+PrintUtils.color(ObsColors.COSMO)+"&o - bypassed system firewalls.",
				PrintUtils.color(ObsColors.COSMO)+"&oConfirmed Linked Incident: System overseer in branch 'Avum Totalis' breached",
				PrintUtils.color(ObsColors.COSMO)+"&ocontainment and injected itself into test branch 'Fantasia' for unknown cause.",
				PrintUtils.color(ObsColors.COSMO)+"&oIt still isn't aware of what it truly is - overwrote primary element as &lCosmo&r"+PrintUtils.color(ObsColors.COSMO)+"&o.",
				PrintUtils.color(ObsColors.COSMO)+"&oForm assignment: &lBow&r"+PrintUtils.color(ObsColors.COSMO)+" &owith auxiliary binding: &lString&r"+PrintUtils.color(ObsColors.COSMO)+"&o.",
				PrintUtils.color(ObsColors.COSMO)+"&oWith message: 'I just want to help my people, why do you keep interrupting me!'.",
				PrintUtils.color(ObsColors.COSMO)+"&oLog injected into supposed placeholder "+Symbols.EOL+"choic object.. Will continue to monitor."), new Bow97().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: "+PrintUtils.color(ObsColors.ARCANO)+Symbols.EOL+"cho of Lordran",
				"echo_of_lordran",
				MateriaType.CATALYST,
				MateriaComponent.CATALYST,
				Rarity.SEVEN,
				false,
				"&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
				PrintUtils.color(ObsColors.ARCANO) + "&oFantasian Academy Dossier — Lordran, The Forgotten King",
				PrintUtils.color(ObsColors.ARCANO) + "&oA noble of a land swallowed by time, crowned in regal soverance, Lordran,",
				PrintUtils.color(ObsColors.ARCANO) + "&owas the sole arbiter and inventor of &lArcanaean&r" + PrintUtils.color(ObsColors.ARCANO) + "&o magick.",
				PrintUtils.color(ObsColors.ARCANO) + "&oHe ruled from a castle hewn and bejeweled in &lIron&r" + PrintUtils.color(ObsColors.ARCANO) + "&o, clothed in &lPelt",
				PrintUtils.color(ObsColors.ARCANO) + "&ofit only for a King's commission. Many sought his tutelage, for Arcanaean magick",
				PrintUtils.color(ObsColors.ARCANO) + "&oproved learnable only by those of the most refined and untainted spirit.",
				PrintUtils.color(ObsColors.ARCANO) + "&oLordran held that the soul was the wellspring of all ether — that in harnessing",
				PrintUtils.color(ObsColors.ARCANO) + "&othe very fabric of magic, one ascends toward true wisdom. His teachings were",
				PrintUtils.color(ObsColors.ARCANO) + "&onot meant for common minds, yet would you have stood before him, you'd find",
				PrintUtils.color(ObsColors.ARCANO) + "&onot a tyrant, but a king who prized loyalty and integrity above all mortal powers."), new LanceOfLordran().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: Protocol " + PrintUtils.color(ObsColors.CELESTIO) + "&lA.I.O.N.",
			    "echo_of_aion",
			    MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.SEVEN,
			    false,
			    "&r&7&oAn echo of the distant past. Harmonic resonance persists.",
			    "&r&7&oA memory engram is encoded within:",
			    PrintUtils.color(ObsColors.CELESTIO) + "&o[ CRITICAL ANOMALY REPORT — ORIGIN UNRESOLVABLE ]",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oAn unindexed file has propagated into system logs with no traceable write event.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oElemental affinity: &lCelestian&r" + PrintUtils.color(ObsColors.CELESTIO) + "&o. Materia binding assumed: &lGold, Leather&r" + PrintUtils.color(ObsColors.CELESTIO) + "&o.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oDesignation: A.I.O.N. — Artifact Interface to Ordinal Netmask.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oClassification: Legacy AI Process. Origin Sector: NULL. Permission Tier: UNRESTRICTED.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oAll clearance fields return highest authorization. No issuing authority on record.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oThis process should not exist within the simulation boundary.",
			    PrintUtils.color(ObsColors.CELESTIO) + "&oScheduled for termination at next reset cycle. Setting Warn: 'Do not interact'."), new Aion().getInternalName());
	
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: " + PrintUtils.color(ObsColors.INFERNO)+Symbols.EOL+"cho of Agni",
				"echo_of_agni",
				MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.SIX,
			    false,
			    "&r&7&oAn echo of the distant past. Resonate harmonic frequencies radiate..",
				"&r&7&oThere's a memory engram encoded within:",
			    PrintUtils.color(ObsColors.INFERNO)+"&oFantasian Academy Dossier — Émanateur de l'Enfer, Madame Agni",
			    PrintUtils.color(ObsColors.INFERNO)+"&oAgni, the Emanator of Inferno, stands among the &e&o&lEndless&r"+PrintUtils.color(ObsColors.INFERNO)+"&o —",
			    PrintUtils.color(ObsColors.INFERNO)+"&oan ancient elemental which holds dominion over the Dunes of Aigisva'ard.",
			    PrintUtils.color(ObsColors.INFERNO)+"&oVenerated for centuries, her counsel is sought by generals and scholars alike,",
			    PrintUtils.color(ObsColors.INFERNO)+"&owith her &lInfernal&r"+PrintUtils.color(ObsColors.INFERNO)+"&o magiks widely studied across continental G'yr Arbanaum.",
			    PrintUtils.color(ObsColors.INFERNO)+"&oShe iconically wields a &lBow&r"+PrintUtils.color(ObsColors.INFERNO)+"&o laced with fine &lString&r"+PrintUtils.color(ObsColors.INFERNO)+"&o, and is regarded as the",
			    PrintUtils.color(ObsColors.INFERNO)+"&opreeminent master of ranged combat in the eastern reaches of Fantasia.",
			    PrintUtils.color(ObsColors.INFERNO)+"&oAccounts of her first sighting describe a woman adorned in jewels",
			    PrintUtils.color(ObsColors.INFERNO)+"said to originate from Feits'heilm, and many who visit her shrine",
			    PrintUtils.color(ObsColors.INFERNO)+"&obestow such jewels hoping to divine the Queen of War."), new BowOfAgni().getInternalName());
		
		Materia.register(new Materia("&e&lCreation Catalyst&r&f: " + PrintUtils.color(ObsColors.ARCANO)+Symbols.EOL+"cho of the Elementalist Sword",
				"echo_of_elemental_sword",
				MateriaType.CATALYST,
			    MateriaComponent.CATALYST,
			    Rarity.FIVE,
			    false,
			    "&r&7&oAn echo of the distant past. Resonant harmonic frequencies radiate..",
                "&r&7&oThere's a memory engram encoded within:",
                PrintUtils.color(ObsColors.ARCANO)+"&oThe Elements: Earth, Air, Fire, and Water--",
                PrintUtils.color(ObsColors.ARCANO)+"&othey are what connects life within the realm to all things aethereal.",
                PrintUtils.color(ObsColors.ARCANO)+"&oMany throughout time, both warriors and smiths, told tales of a Sword..",
                PrintUtils.color(ObsColors.ARCANO)+"&oThe sword was forged in &lPelt&r"+PrintUtils.color(ObsColors.ARCANO)+" &oand &lIron&r"+PrintUtils.color(ObsColors.ARCANO)+"&o.",
                PrintUtils.color(ObsColors.ARCANO)+"&oOnly when wrought from the highest quality of such materials, they claimed,",
                PrintUtils.color(ObsColors.ARCANO)+"&ocould it hold the power of all four natural elements, and abilities unbound.",
                PrintUtils.color(ObsColors.ARCANO)+"&oIf you hold this Echo, maybe you, too, can be the one to craft it.."), new SwordOfTheElements().getInternalName());
	}
}
