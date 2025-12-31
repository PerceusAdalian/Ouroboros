package com.lol.spells.instances.geo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Expelliarmus extends Spell
{

	public Expelliarmus() 
	{
		super("Expelliarmus", "expelliarmus", Material.LEAD, SpellType.UTILITY, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 45, 10, 
				"&r&fWhip your target within &b&o15 meters&r&f with a clump of hot sand and ash",
				"&r&fin their eyes causing them to drop their on-hand item. Inflicts &6Sanded&7 (20s)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&6&oSmokescreen&r&7&o', however, colloquially known as '&r&6&oExpelliarmus&r&7&o'.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 15);

		if (target instanceof Mob mob) 
		{
			if (mob.getItemInUse() == null || mob.getItemInUse().getType() == Material.AIR || mob.getEquipment() == null) return false;
			ItemStack mobItem = mob.getItemInUse().clone();
			mob.getItemInUse().setType(Material.AIR);
			Bukkit.getWorld(target.getWorld().getUID()).dropItem(target.getLocation(), mobItem).setPickupDelay(100);
			OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), 4, 0.5, 25, 8, 0.5, Particle.SMOKE, null);
			return true;
		}
		
		OBSParticles.drawGeoCastSigil(p);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.ASH, null);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 2, 0.5, Particle.SMOKE, null);
		EntityEffects.addSanded((LivingEntity) target, 20);
		
		
		return false;
	}

}
