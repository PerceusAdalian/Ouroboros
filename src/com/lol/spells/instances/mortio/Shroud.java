package com.lol.spells.instances.mortio;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Shroud extends Spell
{

	public Shroud() 
	{
		super("Shroud", "shroud", Material.OMINOUS_BOTTLE, SpellType.BUFF, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 45, false,
				"&r&fApply &4Shroud &bII&f and remove emnity for self &7(45s | 30m)","",
				"&r&4Shroud &eEffect&f: Grants an increase to &b&oSpeed&r&f and &b&oJump Height&r&f",
				"&r&fequal to the magnitude of Shroud, plus &b&oInvisibility&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 6, 0.5, Particle.DUST_PLUME, null);
		EntityEffects.addShroud(p, 1, 45);
		
		if (!RayCastUtils.getNearbyEntities(p, 30, r ->
		{
			if (r == null || !(r instanceof Mob)) return;
			if (((Mob) r).getTarget().equals(p)) ((Mob)r).setTarget(null);
		})) return -1;
		
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
