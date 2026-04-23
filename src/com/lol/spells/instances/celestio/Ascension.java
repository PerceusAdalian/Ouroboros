package com.lol.spells.instances.celestio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.aero.Fly;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Ascension extends Spell
{

	public Ascension() 
	{
		super("Ascension", "ascension", Material.ALLAY_SPAWN_EGG, SpellType.ULTIMATE, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.FIVE, 0, 1, false,
				"&r&e&oPrimary &r&f" + PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&eAscension&r&f: &e&oJudgement&r&f -- 25 &b&lMana",
				"&r&fDeal 10&c♥ "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage to &6target&f, and apply &eHumility &7(45m, 20s)","",
				"&r&e&oSecondary &r&f" + PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eAscension&r&f: &e&oAspect of the Seraphim&r&f -- 50 &b&lMana",
				"&r&e&oToggled&r&f: Grants flight to &6self&f. &c&oDeactivating&r&f doesn't cost &b&lMana&r&f.",
				"&r&fWhile airborne, you may only cast &e&oJudgement&r&f.",
				"&r&fLanding automatically toggles this &eSpell&f.",
				"&r&cWarning&f: You may receive fall damage if toggling mid-air.","",
				"&r&eHumility&r&f: Affected take &b&o15%&r&f more "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage.","",
				"&r&b&oEchoic Disonance&r&f: On toggle, &lCD &r&e-> &b&o30 seconds&r&f");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
	    Player p = e.getPlayer();

	    if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
        {
	    	
	    	return Fly.playSpellEffect(p, 50, true, true);
        }

	    if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
	    {
	    	return castJudgement(p, 25);
	    }

	    return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}
	
	private int castJudgement(Player p, int finalManaCost)
	{
	    if (!p.isFlying()) return -1;
	    
		boolean hit = RayCastUtils.getEntity(p, 45, target ->
	    {
	        if (!(target instanceof LivingEntity)) return;

	        EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
	        ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.END_ROD, null);

	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            if (target == null || target.isDead()) return;

	            EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
	            ObsParticles.drawSpiralVortex(target.getLocation(), 110, 3, 0.5, Particle.CLOUD, null);
	            ObsParticles.drawSpiralVortex(target.getLocation(), 90, 4, 0.4, Particle.END_ROD, null);
	            MobData.damageUnnaturally(p, target, 10, true, true, ElementType.CELESTIO);
	            CelestioEffects.addHumility((LivingEntity) target, 20);
	        }, 15);
	    });

	    if (!hit) return -1;
	    
	    return finalManaCost;
	}

	
}
