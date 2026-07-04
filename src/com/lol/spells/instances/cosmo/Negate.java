package com.lol.spells.instances.cosmo;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Negate extends Spell
{

	public Negate()
	{
		super("Negate", "negate", Material.SHIELD, SpellType.DEFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 150, 12.5, true,
				false, PrintUtils.color(ObsColors.COSMO)+"&oNegate &r&fnext &b"+Symbols.INCOMING+" &fdamage source for &6self &7(10s)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!CosmoEffects.addNegate(p, 10)) return -1;
		ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 8, 0.5, 0.1, Particle.ENCHANT, null);
		ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 4, 80, 15, 0.2, Particle.DRAGON_BREATH, 0.5f);
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
