package com.lol.spells.instances.glacio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.craftbukkit.entity.CraftArrow;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;

public class Glacius extends Spell
{
	
	public Glacius() 
	{
		
		super("Glacius", "glacius", Material.TIPPED_ARROW, SpellType.OFFENSIVE, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 20, 1.5, 
				"&r&fSummon and arrow laced with &b&lGlacio&r&f energy firing it forward.",
				"&r&fProjectile applies &bFrosted I &7(10s)",
				"&bFrosted&f Effect: &d&oSlows&r&f and &d&oWeakens&r&f those afflicted.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&b&oFrosted Arrow&r&7&o', however, colloquially known as '&r&b&oGlacius&r&7&o'.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_DROWNED_SHOOT, SoundCategory.AMBIENT);
		CraftArrow arrow = p.launchProjectile(CraftArrow.class);
		arrow.setGravity(false);
		arrow.setVelocity(p.getLocation().getDirection().multiply(1.5));
		arrow.setDamage(5);
		arrow.setPierceLevel(2);
		arrow.setCritical(false);
		arrow.addCustomEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 0), true);
		arrow.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0), true);
		arrow.setPickupStatus(PickupStatus.DISALLOWED);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->arrow.remove(), 40);
		return true;
	}

}
