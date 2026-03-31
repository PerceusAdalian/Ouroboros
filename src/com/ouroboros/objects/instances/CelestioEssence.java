package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class CelestioEssence extends AbstractObsObject
{

	public CelestioEssence() 
	{
		super(PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f Essence", "celestio_essence", Material.QUARTZ, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fA refracted crystal of "+PrintUtils.color(ObsColors.CELESTIO)+"Light&f with calming hymns echo within.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawCelestioCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.CELESTIO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r"+PrintUtils.color(ObsColors.CELESTIO)+"⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
