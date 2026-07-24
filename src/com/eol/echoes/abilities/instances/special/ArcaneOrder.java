package com.eol.echoes.abilities.instances.special;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ArcaneOrder extends EchoAbility
{

	public ArcaneOrder()
	{
		super("Arcane Order", "arcane_order", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.ARCANO,
				CastConditions.MIXED, EchoForm.POLEARM, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Arcane Order&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oNoble Discernment&r&f --",
				"&r&7&l┏--&r&7{&e✧ &oArbanian Stance&f: "+PrintUtils.color(ObsColors.ARCANO)+"Regal Offence &7<-+-> "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Defense",
				"&r&7&l┗┳- "+PrintUtils.color(ObsColors.ARCANO)+"Regal Offense&r&f: &b&oAtk &r&e-> &b&l150",
				"&r&7&l ┗┳- "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Defense&r&f: "+Symbols.INCOMING+" dmg &a&orestores &b&oDurability&r&f scaled by &6"+Symbols.TARGET+" &flevel.",
				"&r&7&l  ┗--&r&7{&r&e✧ &oEnforce Order&r&f: Rush/Disengage &6target &dMob&f and apply &6Taunt&f/&ePacify &7(20m/6m)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Arcane Order&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oSoverance&r&f -- &cRemove &f10 &b&oDurability",
				"&r&fWhile in "+PrintUtils.color(ObsColors.ARCANO)+"Regal Offense&r&f, grant a stack of "+PrintUtils.color(ObsColors.ARCANO)+"Jest &r&fto &6self&f.",
				"&r&fWhile in "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Defense&r&f, grant &6Guarded &bV &fto &6self &7(30s)","",
				PrintUtils.color(ObsColors.ARCANO)+"Jest &eEffect&f: Increases "+Symbols.INCOMING+"/"+Symbols.OUTGOING+" dmg by &b&o10%&r&f per stack.",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 5 instances.",
				"&r&bEchoic Dissonance&f: any triggered effects are nullified on stance change.");
	}

	private static final Map<UUID, Boolean> stanceMap     = new HashMap<>();
	
	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p    = e.getPlayer();
		UUID   uuid = p.getUniqueId();

		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			// I'll implement this later... <3
			return -1;
		}

		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			boolean offensive = !stanceMap.containsKey(uuid) || !stanceMap.get(uuid);
			
			Entity target = RayCastUtils.getEntity(p, 20);
			if (target != null && target instanceof Mob mob)
			{
				if (offensive)
				{
					EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.AMBIENT);
					EntityEffects.rushEntity(p, mob, 3);
					mob.setTarget(p);
				}
				else if (!offensive && p.getLocation().distance(mob.getLocation()) <= 6)
				{
					EntityEffects.playSound(p, Sound.ENTITY_BREEZE_WHIRL, SoundCategory.AMBIENT);
					EntityEffects.disengageEntity(p, mob, 3);
					mob.setTarget(null);
				}
			}
			
			if (offensive)
			{
				stanceMap.put(uuid, true);
				p.getInventory().setItemInMainHand(EchoManager.modifyEchoData(e.getItem(), data -> data.setAttack(150)));
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO) + "&oRegal Offence Engaged");
				EntityEffects.playSound(p, Sound.ENTITY_IRON_GOLEM_ATTACK, SoundCategory.AMBIENT);
			}
			else
			{
				stanceMap.put(uuid, false);
				p.getInventory().setItemInMainHand(EchoManager.modifyEchoData(e.getItem(), data -> data.setAttack(0)));
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO) + "&oCrowned Defense Engaged");
				EntityEffects.playSound(p, Sound.ITEM_SHIELD_BLOCK, SoundCategory.AMBIENT);
			}
			return 0;
		}

		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

	public static void registerAbilityHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onHit(EntityDamageByEntityEvent e)
			{
				if (!(e.getEntity() instanceof Player p)) return;
				if (!(e.getDamager() instanceof LivingEntity le)) return;
				
				MobData data = MobData.getMob(le.getUniqueId());
				if (data == null) return;
				
				UUID uuid = p.getUniqueId();
				if (!stanceMap.containsKey(uuid) || stanceMap.get(uuid)) return;
				
				ItemStack echo = p.getInventory().getItemInMainHand();
				if (!EchoManager.isEcho(echo)) return;
				
				EchoManifest codec = EchoManager.getCodec(echo);
				if (codec.lockedAbilityKey() == null || !codec.lockedAbilityKey().equals("arcane_order")) return;
				
				EchoManager.modifyDurability(p, echo, DurabilityOperation.ADD, data.getLevel(), false);
				EntityEffects.playSound(p, Sound.ITEM_SHIELD_BLOCK, SoundCategory.AMBIENT);
				ObsParticles.drawGeoCastSigil(p);
			}
		}, plugin);
	}
	
}