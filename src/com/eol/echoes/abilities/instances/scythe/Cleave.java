package com.eol.echoes.abilities.instances.scythe;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class Cleave extends EchoAbility
{

    public Cleave() // Check for functionality
    {
        super("Cleave", "cleave", Material.BLACK_DYE, StatType.MELEE, 20, 10, 25, AbilityType.COMBAT, ElementType.MORTIO, CastConditions.RIGHT_CLICK_AIR,EchoForm.SCYTHE, 
                "&r&fDeal &l30&r&c♥ "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f damage inflicting &4Dread &7(7m, 15s)","",
                "&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
				"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
				"&r&fsubsequent applications will inflict &4Doom&f after a second application.");
    }

    @Override
    public int cast(PlayerInteractEvent e) 
    {
        Player p = e.getPlayer();
        
        if (!RayCastUtils.getEntity(p, 5, target ->
		{
			if (!(target instanceof LivingEntity le) || (target instanceof Player)) return;
			EntityEffects.rushEntity(p, le, 2);
			
			ObsParticles.drawX(target.getLocation(), p.getEyeLocation().getDirection(), 7, 0.5, 0.2, false, Particle.SMOKE, null);
			ObsParticles.drawX(target.getLocation(), p.getEyeLocation().getDirection(), 8, 0.5, 0.1, false, Particle.CRIMSON_SPORE, null);
			ObsParticles.drawX(target.getLocation(), p.getEyeLocation().getDirection(), 6, 0.6, 0.4, false, Particle.ASH, null);
			EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.MASTER);
			MobData.damageUnnaturally(p, target, 30, true, true, ElementType.MORTIO);
			MortioEffects.addDread(le, 300);
		})) return -1;
		
        return 25;
    }

	@Override
	public int getFinalDurabilityCost()
	{
		return 25;
	}
}

/**
 * "&r&eDoom&r&f Description: Target non-&4&lMortio&r&f becomes marked,", 
                "&r&fsustaining a &cDOT&f with severity equal to its &bmagnitude&f.",
                "&r&fNon-&4&lMortio&r&f mobs take an additional &b&l1.25x &r&4&lMortio&r&f &cdmg&f,",
                "&r&fotherwise the inflicted target is &a&ohealed&r&f."
 */