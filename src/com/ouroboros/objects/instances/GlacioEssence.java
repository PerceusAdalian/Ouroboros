package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class GlacioEssence extends AbstractObsObject
{

	public GlacioEssence() 
	{
		super("&b&lGlacio&r&f Essence", "glacio_essence", Material.PRISMARINE_CRYSTALS, true, false, 
				"&r&fA simple, refracted essence of &bWater&f. It's cool to the touch.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your &b&lGlacio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawGlacioCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_CREAKING_FREEZE, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.GLACIO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r&b⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
