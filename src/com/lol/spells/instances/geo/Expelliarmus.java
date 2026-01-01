package com.lol.spells.instances.geo;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
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

		if (!(target instanceof LivingEntity living)) return false;

		EntityEquipment equipment = living.getEquipment();
		if (equipment == null) return false;

		ItemStack heldItem = equipment.getItemInMainHand();
		if (heldItem.getType() == Material.AIR) 
		{
		    heldItem = equipment.getItemInOffHand();
		    if (heldItem.getType() == Material.AIR) return false;
		}

		OBSParticles.drawGeoCastSigil(p);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.ASH, null);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 2, 0.5, Particle.SMOKE, null);
		EntityEffects.addSanded(living, 20);

		ItemStack droppedItem = heldItem.clone();
		if (heldItem.equals(equipment.getItemInMainHand())) 
			equipment.setItemInMainHand(new ItemStack(Material.AIR));
		else equipment.setItemInOffHand(new ItemStack(Material.AIR));

		target.getWorld().dropItem(target.getLocation(), droppedItem).setPickupDelay(100);
		OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), 4, 0.5, 25, 8, 0.5, Particle.SMOKE, null);

		return true;
	}

}
