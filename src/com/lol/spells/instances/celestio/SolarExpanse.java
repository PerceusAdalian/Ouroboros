package com.lol.spells.instances.celestio;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import com.lol.wand.Wand;
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

public class SolarExpanse extends Spell
{

	public SolarExpanse()
	{
		super("Solar Expanse", "solar_expanse", Material.GOLDEN_SPEAR, SpellType.OFFENSIVE, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.FOUR, 150, 3, false, true,
				"&ePrimary&f "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&eSolar Expanse&f: &e&oHoly Bayonet&r&f --",
				"&r&fDealing 25&c♥ &e&lImpale&r&f damage to your &6target&f applying &e&oExpose&r&7 (30m)","",
				"&eSecondary&f "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eSolar Expanse&f: &e&oGuilded Lance&r&f --",
				"&r&f&6Broken Target&r&f -> deal "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage equal to &b&o45%&r&f current &aHP&c♥&7 (30m)","",
				"&r&bEchoic Dissonance&r&f: Casting &e&oGuilded Lance&r&f: &lCD &r&e-> &b10s&f, &lMC &r&e-> &b250&f");
	}

	private static Set<UUID> cooldown = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (cooldown.contains(p.getUniqueId())) 
			{
				PrintUtils.PrintToActionBar(p, "&7&o'&e&oGuilded Lance&7&o' on Cooldown..");
				return -1;
			}
			
			Wand wand = new Wand(e.getItem());
			if (wand.getCurrentMana() < 250)
			{
				PrintUtils.PrintToActionBar(p, "&7&oNot Enough Mana for '&e&oGuilded Lance&7&o'");
				return -1;
			}
			
			if (!RayCastUtils.getEntity(p, 30, target->
			{
				if (!(target instanceof LivingEntity le) || target instanceof Player) return;
				MobData data = MobData.getMob(le.getUniqueId());
				if (data == null) return;
				
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.45, Particle.WAX_ON, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.5, Particle.CLOUD, null);
				EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.AMBIENT);
				
				if (data.isBreak())
					MobData.damageUnnaturally(p, le, data.getHp(false) * .45, true, true, ElementType.CELESTIO);

				cooldown.add(p.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown.remove(p.getUniqueId()), 200);
			})) return -1;
			return 250;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 30, target->
			{
				if (!(target instanceof LivingEntity le) || target instanceof Player) return;
				
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.END_ROD, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.5, Particle.CLOUD, null);
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_WIND_BURST, SoundCategory.AMBIENT);

				CelestioEffects.addExposed(le, 20);
				MobData.damageUnnaturally(p, le, 25, true, true, ElementType.IMPALE);
			})) return -1;
			return this.getManacost();
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return this.getManacost();
	}

}
