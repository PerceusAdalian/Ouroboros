package com.lol.spells.instances.arcano;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Surveil extends Spell
{

	public Surveil() 
	{
		super("Surveil", "serveil", Material.SPYGLASS, SpellType.CANTRIP, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 3, false, true,
				"&r&fScan and display &6target &d&omob&r&f's &b&ostats &r&7(30m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 30, target->
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.WARPED_SPORE, null);
			if (target instanceof LivingEntity && !(target instanceof Player))
			{
				MobData data = MobData.getMob(target.getUniqueId());
				if (data == null) return;
				
				String name = data.getName();
				if (name == null) name = PrintUtils.getFancyEntityName(target.getType());
				double currentHP = new BigDecimal(data.getHp(false)).setScale(1, RoundingMode.HALF_UP).doubleValue();
				
				ObsParticles.drawArcanoCastSigil((LivingEntity) target);
				
				PrintUtils.Print(p, "&b&l+&r&7-------------&f{&bΩ&f}&7-------------&b&l+",
									"         &r&b&lTarget Identified&f:",
									"         &r&f&l- &eLv&f: &b&l"+data.getLevel()+"&r&f "+name,
									"            &r&f&lCurrent Stats&r&f:",
									"    &r&f&l- &r&fHP&c♥&r&f: &a"+Math.min(currentHP, data.getHp(true))+"&7/"+
											data.getHp(true)+"&f | &r&6AR&f: &6"+data.getArmor(false)+"&7/"+data.getArmor(true),
									"            &r&6Break &fStatus: "+(data.isBreak() == false ? "&c&l✕" : "&a&l✔"),
									"      &r&fElemental Affinity: "+EntityCategories.parseElementType(target.getType()).getType(),
									"&b&l+&r&7-------------&f{&bΩ&f}&7-------------&b&l+");
				return;
			}
		})) return -1;
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
}
