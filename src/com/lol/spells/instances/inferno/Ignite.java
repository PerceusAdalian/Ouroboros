package com.lol.spells.instances.inferno;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Ignite extends Spell
{

	public Ignite()
	{
		super("Ignite", "ignite", Material.FLINT_AND_STEEL, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.MIXED, Rarity.THREE, 25, 3, true,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&cIgnite&f: &c&oBurn Other&r&f --",
				"&r&fIgnite target &d&oPlayer&r&f or &d&oMob&r&f inflicting &cBurn&r&7 (20m, 20s | &cPVP&7: &c10s&7)",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_BLOCK),
				"&r&cIgnite&f: &c&oStoking Flame&r&f --",
				"&r&fIgnite target &d&oBlock&r&f, setting it ablaze.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_BLOCK))
		{
			EntityEffects.playSound(p, Sound.ITEM_FLINTANDSTEEL_USE, SoundCategory.AMBIENT);
		    Block block = e.getClickedBlock().getRelative(BlockFace.UP);

		    if (block == null || block.getType() != Material.AIR) return -1;

		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
		    {
		        block.setType(Material.FIRE);
		        OBSParticles.drawDisc(block.getLocation(), 2, 1, 5, 0.2, Particle.LAVA, null);
		        EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		    }, 4);
		    return 25;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			Entity target = RayCastUtils.getEntity(p, 20);
			if (target == null || !(target instanceof LivingEntity)) return -1;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.FLAME, null);
			InfernoEffects.addBurn((LivingEntity) target, target instanceof Player ? 10 : 20);
			return 25;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}

}
