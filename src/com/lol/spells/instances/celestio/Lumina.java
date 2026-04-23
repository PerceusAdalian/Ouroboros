package com.lol.spells.instances.celestio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Lumina extends Spell
{

	public Lumina()
	{
		super("Lumina", "lumina", Material.LANTERN, SpellType.SIGNATURE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_BLOCK, Rarity.ONE, 10, 1, false,
				"&r&fCast a whisp of light at &6target &dBlock &7(30s)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		Block block = e.getClickedBlock().getRelative(e.getBlockFace());
		if (!block.getType().isAir()) return -1;

        EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
        
        block.setType(Material.LIGHT);
		Levelled lightData = (Levelled) block.getBlockData();
        lightData.setLevel(15);
        block.setBlockData(lightData);
        p.sendBlockChange(block.getLocation(), lightData);
		
        ObsTimer.runWithCancel(Ouroboros.instance, b->
        {
        	if (block == null || !block.getType().equals(Material.LIGHT)) b.cancel();
        	
        	ObsParticles.drawWisps(block.getLocation(), 2, 2, 4, Particle.END_ROD, null);
        }, 20, 600);
        
        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
        {
            if (block.getType().equals(Material.LIGHT))
            {
                block.setType(Material.AIR);
                ObsParticles.drawWisps(block.getLocation(), 2, 2, 4, Particle.CLOUD, null);
            }
        }, 600);
		
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost()
	{
		return 10;
	}

}
