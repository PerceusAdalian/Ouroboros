package com.lol.spells.instances.astral;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.lol.spells.instances.celestio.Heal;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class CollectiveUnconscious extends Spell
{

	public CollectiveUnconscious()
	{
		super("Collective Unconscious", "collective_unconscious", Material.ENDER_EYE, SpellType.CONTROL, SpellementType.ASTRAL, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 300, 10, true,
				true, 
				PrintUtils.assignAstralVariant("Collective Unconscious", true) + " &r&e&oHarmony &r&f--",
				"&r&a&oRestore &r&f1500&c"+Symbols.HP+" &fto nearby &c&oPlayers&r&f and apply &e&oWard &r&bIII &7(30m, 20s)","",
				PrintUtils.assignAstralVariant("Collective Unconscious", false) + " &r&9&oMania &r&f--",
				"&r&6&oBreak &r&6self &fand nearby &c&oPlayers&r&f/&d&oMobs &r&7(30m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getNearbyEntities(p, 30, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			ObsParticles.drawAstralCastSigil(le, TimeUtils.checkTime(p.getWorld(), Timeframe.DAY));
			
			if (TimeUtils.checkTime(p.getWorld(), Timeframe.DAY))
			{				
				if (!(target instanceof Player pTarget)) return;
				if (!Heal.playSpellEffect(pTarget, 1500)) return;
				CelestioEffects.addWard(pTarget, 2, 20);
			}
			else
			{
				if (target instanceof Player pTarget)
				{
					PlayerData.getPlayer(pTarget.getUniqueId()).setBreak();
				}
				else if (!(le instanceof Player))
				{
					MobData.getMob(le.getUniqueId()).setBreak();
				}
			}
		}));
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		return 300;
	}

	@Override
	public int getTotalManaCost()
	{
		return 300;
	}

}
