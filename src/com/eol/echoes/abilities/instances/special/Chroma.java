package com.eol.echoes.abilities.instances.special;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Chroma extends EchoAbility
{

	public Chroma()
	{
		super("Chroma", "chroma", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.ARCANO,
				CastConditions.MIXED, EchoForm.POLEARM, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Chroma&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oPaint with Passion!&r&f -- &cRemove &f50 &b&oDurability",
				"&r&fRush &6" + Symbols.TARGET + " &fdealing damage of selected element -> apply &eSignature Debuff&f.",
				"&r&f- Aspected Damage Calculation: &b&l250 &f&o+ (Crit Rate × Crit Multiplier) &r&7(20m, 15s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Chroma&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oName your Colour!&r&f --",
				"&r&fCycle between the primary elements and gain a charge of "+PrintUtils.color(ObsColors.ARCANO)+"&oPrisma&r&f.");
	}

	private static Map<UUID, ElementType> cycledElements = new HashMap<>();
	
	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (cycledElements.get(p.getUniqueId()) == null || !cycledElements.containsKey(p.getUniqueId())) 
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT);
				return -1;
			}
			
			if (!RayCastUtils.getEntity(p, 20, target -> 
			{				
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				ElementType eType = cycledElements.get(p.getUniqueId());
				EchoManifest codec = EchoManager.getCodec(e.getItem());
				if (codec == null || e.getItem().getItemMeta() == null) return;
				EchoData stats = codec.baseStats();
				double damage = (250 * (1 + stats.getCritRate())) * stats.getCritModifier();
				
				EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_2, SoundCategory.AMBIENT);
				ObsParticles.playCastSigil(p, eType);
				EntityEffects.rushEntity(p, le, 3);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					MobData.damageUnnaturally(p, le, damage, true, true, eType, codec);
					ObsParticles.playCastSigil(le, eType);
					ObsParticles.drawLandingWave(le);
				}, 10);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.playCastSigil(p, eType);
					EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.AMBIENT);
					EntityEffects.disengageEntity(p, le, 4);
					ArcanoEffects.addChroma(le, eType, 0, 15);
				}, 25);
				
			})) return -1;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
	        ElementType current = cycledElements.get(p.getUniqueId());
	        ElementType next = (current == null) ? ElementType.CELESTIO : switch (current)
	        {
	            case CELESTIO -> ElementType.MORTIO;
	            case MORTIO   -> ElementType.INFERNO;
	            case INFERNO  -> ElementType.GLACIO;
	            case GLACIO   -> ElementType.AERO;
	            case AERO     -> ElementType.GEO;
	            case GEO      -> ElementType.COSMO;
	            case COSMO    -> ElementType.HERESIO;
	            case HERESIO  -> ElementType.CELESTIO;
	            default       -> ElementType.CELESTIO;
	        };

	        cycledElements.put(p.getUniqueId(), next);
	        PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO) + "&oCycled Element&r&f: "
	            + PrintUtils.getElementTypeColor(next) + next.getType());
	        return 0;
		}
		
		return 0;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
