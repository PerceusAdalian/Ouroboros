package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class HealthCrystal extends AbstractObsObject
{

	public HealthCrystal() 
	{
		super("Health Crystal", "hp_crystal", Material.EMERALD, true, false, 
				"&r&fA concentration of &aVerdant &bEther&f.","",
				"&r&d&oRight-Click&r&f to consume and &arestore&f 5&c"+Symbols.HP+"&f.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		if (!PlayerActions.rightClickAir(e)) return false;
		Player p = e.getPlayer();
		ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 3, 0.8, 20, 0.2, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
		ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.HAPPY_VILLAGER, null);
		EntityEffects.playSound(p, Sound.BLOCK_LARGE_AMETHYST_BUD_BREAK, SoundCategory.AMBIENT);
		EntityEffects.heal(p, 10);
		ItemCollector.remove(e, 1);
		return true;
	}

}
