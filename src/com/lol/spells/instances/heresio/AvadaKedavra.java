package com.lol.spells.instances.heresio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
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
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AvadaKedavra extends Spell
{

	public AvadaKedavra() 
	{
		super("Avada Kedavra", "avada_kedavra", Material.TOTEM_OF_UNDYING, SpellType.OFFENSIVE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.SEVEN, 1500, 600, true,
				PrintUtils.color(ObsColors.HERESIO)+"&l&oUnforgivable Curse&r&f --",
				"&r&fDeal "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage equal to the target's current &cHP&f.",
				"&r&cPVP&f: Instantly kill target &cPlayer&f. Your Wand's &e&oAffinity&r&f must be "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f.",
				PrintUtils.color(ObsColors.HERESIO)+"&lAvada Kedavra&r&f can &2Backfire&f, applying a &2Wildcard&f to &6self &7(5min)","",			
				"&r&2WildCard&e Effect&r&f: inflicts a random debuff.",
				"&r&2Backfire &eEffect&f: The spell fizzles, and may include a &2&oPenance&f.",
				"&r&2Backfire &bChance&f: &b&o5.0% &r&7/ &b&o1.25%&r&7&o (Heresio Affinity Wand)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&2&oApotheosis&r&7&o', however, colloquially known as '&r&2&oAvada Kedavra&r&7&o'.",
				"&r&7&oThis spell is listed to be an '&2&l&oUnforgivable Curse&r&7&o', whereas possession",
				"&r&7&oand attempted replication of such knowledge is strictly &c&oprohibited&r&7&o.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Wand wand = new Wand(e.getItem());
		
		if (!RayCastUtils.getEntity(p, 40, target -> 
		{
		    if (!(target instanceof LivingEntity le)) return;

		    EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.GLOW_SQUID_INK, null);
		    ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.3, Particle.TRIAL_OMEN, null);
		    ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.3, Particle.WARPED_SPORE, null);
		    ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, 0.3, 8, Particle.WITCH, null);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, 0.4, 8, Particle.TRIAL_OMEN, null);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, 0.35, 8, Particle.GLOW_SQUID_INK, null);
			
		    if (Chance.of(wand.getElementType() == ElementType.HERESIO ? 1.25 : 5.0)) 
		    {
		        Hex.playBackfire(p, 300);
		        return;
		    }

		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		    {
		        MobData data = MobData.getMob(le.getUniqueId());
		        double damage = data != null
		            ? data.getHp(false)
		            : le.getAttribute(Attribute.MAX_HEALTH).getBaseValue();

		        ObsParticles.drawHeresioCastSigil(le);

		        if (le instanceof Player pTarget && wand.getElementType() == ElementType.HERESIO)
		            pTarget.setHealth(0);
		        else if (!(le instanceof Player))
		            MobData.damageUnnaturally(p, le, damage, true, false, ElementType.HERESIO);
		        else
		            PrintUtils.PrintToActionBar(p, "&cFizzle!");
		    }, 20);

		})) return -1;

		return 1500;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 1500;
	}

}
