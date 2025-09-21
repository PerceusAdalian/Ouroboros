package com.ouroboros.objects.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;

public class RemembranceOfHope extends AbstractObsObject
{

	public RemembranceOfHope() 
	{
		super("&e&lRemembrance of Hope", "hope_", Material.NETHER_STAR, true, false, 
				"&r&f&oDo you remember those nights by the fire with friends?",
				"&r&fS'mores, a radio humming, laughter, soda, and ice cream.",
				"&r&fStories of loss and joy passed around, sparks rising into the dark.",
				"&r&fHold this. Now recall another memory: anger, fear, confusion, a banner raised.",
				"&r&fVoices clashing, protests swelling, unity fractured by silence; &l&nPain&r&f.","",
				"&r&fNow consider, what is the similarity between these two images?",
				"&r&fEven as we struggle collectively &othrough&r&f trials of life,",
				"&r&fliving in this broken world, we ought to lean on one another more.",
				"&r&fEven in silence, there exists &e&lHope&r&f","",
				"&r&f&oRomans 6:23 // John 14:27 // Jeremiah 29:11 // Hebrews 6:19","",
				"&r&f&l&nUsage&r&f: &d&oRight-Click&r&f to receive a random &bBoon &7&o(30s)");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		if (!PlayerActions.rightClickAir(e)) return false;
		
		Player p = e.getPlayer();
		Set<PotionEffectType> eType = Set.of(PotionEffectType.ABSORPTION,PotionEffectType.CONDUIT_POWER,
				PotionEffectType.DOLPHINS_GRACE,PotionEffectType.FIRE_RESISTANCE,PotionEffectType.HASTE,
				PotionEffectType.HEALTH_BOOST,PotionEffectType.HERO_OF_THE_VILLAGE,PotionEffectType.INSTANT_HEALTH,
				PotionEffectType.INVISIBILITY,PotionEffectType.JUMP_BOOST,PotionEffectType.LEVITATION,PotionEffectType.LUCK,
				PotionEffectType.NIGHT_VISION,PotionEffectType.REGENERATION,PotionEffectType.RESISTANCE,
				PotionEffectType.SATURATION,PotionEffectType.SPEED,PotionEffectType.STRENGTH,PotionEffectType.WATER_BREATHING);
		
		EntityEffects.playSound(p, Sound.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.MASTER);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 8, 0.5, Particle.CLOUD, null);
		OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 7, Particle.WAX_ON, null);
		
		EntityEffects.add(p, eType.iterator().next(), 600, 0);
		
		return true;
	}

}

/*
	"&r&f&nDo you've a time in your youth you'd sit around a fire with friends?","",
	"&r&fS'mores, a radio, smiles, ice cream, and soda. A summer evening.",
	"&r&fStories you share of experiences, of loss, of happiness; you concur.",
	"&r&fNow hold that feeling. Look at this other memory: That of rage, of anger,",
	"&r&fof a certain kind of polarizing fear and confusion. A flag we bear.",
	"&r&fWe protest, we lack, we scream, but we understand that we understand nothing of each other.",
	"&r&fChrist, a figure. Either you assume he's a mad man, or a prophet. One to save.",
	"&r&fNow consider this: What is the similarity between these two images?",
	"&r&fI'd like to assert, even in times of strife, and depression..","",
 	"&r&b&nThere you'll fine hope."
 */