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
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class SolarExpanse extends Spell
{

	public SolarExpanse()
	{
		super("Solar Expanse", "solar_expanse", Material.GOLDEN_SPEAR, SpellType.OFFENSIVE, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.FOUR, 150, 3, false,
				"&ePrimary&f "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&eSolar Expanse&f: &e&oBayonet&r&f --",
				"&r&fPierce your target with light &7(30m)&f dealing 25&c♥ &f&o&lImpale&r&f damage.","",
				"&eSecondary&f "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eSolar Expanse&f: &e&oGuilded Lance&r&f --",
				"&r&fSend a piercing photon lance at target applying &e&oExpose&7 (30m | 20s)",
				"&r&fIf the target is &6&oBroken&r&f, deal &e&lCelestio&r&f damage equal to &b&o45%&r&f current &aHP&c♥&f.","",
				"&r&bEchoic Dissonance&r&f: &e&oSecondary Cast&r&f sets CD to &b&o10 seconds&f and costs an extra &b100 &9&lMana&r&f.");
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
				
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.END_ROD, null);
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.45, Particle.WAX_ON, null);
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.5, Particle.CLOUD, null);
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				
				if (data.isBreak())
					MobData.damageUnnaturally(p, le, data.getHp(false) * .45, true, ElementType.CELESTIO);

				EntityEffects.addExposed(le, 20);
				
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
				
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.END_ROD, null);
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.5, Particle.CLOUD, null);
				EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.AMBIENT);
				
				MobData.damageUnnaturally(p, le, 25, true, ElementType.IMPALE);
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
