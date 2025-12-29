package com.ouroboros.objects.instances;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class TrainingWand extends AbstractObsObject
{

	public TrainingWand() 
	{
		super("Trainer Magic Wand", "trainer_magic_wand", Material.STICK, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&e&lSpell Slots&r&f: &e●","",
				"&b&lEquipped Spell&r&f: Stupefy",
				"&f&nDescription&r&f:","",
				"&r&fWeild concentrated &e&lCelestio&r&f energy to attack foes.",
				"","&r&f&nOther Details&r&f:","",
				PrintUtils.assignElementType(ElementType.CELESTIO),
				PrintUtils.assignSpellType(SpellType.OFFENSIVE),
				"&r&f&lCooldown&r&f: 1.0", "",
				"&r&7&oAt Hogwarts, every wizard has to start somewhere. Whether to hone one's skills,",
				"&r&7&oor for practicing the basics, Stupefy is crucial for learning the art of combat.",
				"&r&7&oUse this wand to increase &d&oMagical Prowess&r&f.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		if (data.getMagicProficiency() == 0)
		{
			PrintUtils.Print(p, "&r&d&oMagic Gnosis&r&e Increased&f: &70 &e&l-> &b&l1&r&f!","&r&fYou've unlocked the magic section in your menu! &7(&d&o/obs menu&r&7)");
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 10, 0.5, Particle.CLOUD, null);
			OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.WAX_ON, null);
			data.setMagicProficiency(1);
		}
		
		EntityEffects.playSound(p, Sound.ENTITY_ARROW_SHOOT, SoundCategory.AMBIENT);
		CraftArrow arrow = p.launchProjectile(CraftArrow.class);
		arrow.setDamage(2);
		arrow.setGravity(false);
		arrow.setInvisible(true);
		arrow.setGlowing(true);
		arrow.setColor(Color.WHITE);
		arrow.setCritical(false);
		arrow.setPickupStatus(PickupStatus.DISALLOWED);
		arrow.setKnockbackStrength(1);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->arrow.remove(), 20);
		
		return true;
	}

}
