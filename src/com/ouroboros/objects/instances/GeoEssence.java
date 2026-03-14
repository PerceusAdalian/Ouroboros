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

public class GeoEssence extends AbstractObsObject
{

	public GeoEssence() 
	{
		super("&6&lGeo&r&f Essence", "geo_essence", Material.BROWN_DYE, true, false, 
				"&r&fA simple, refracted essence of &6Earth&f. This might as well be dirt.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your &6&lGeo&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawGeoCastSigil(p);
			EntityEffects.playSound(p, Sound.BLOCK_MUD_STEP, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.GEO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r&6⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
