package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ScrapMateria extends AbstractObsObject
{

	public ScrapMateria() 
	{
		super("Scrap Materia", "scrap_materia", Material.PHANTOM_MEMBRANE, true, false, 
				"&r&fScrap materia obtained by failed refinery attempts.",
				"&r&fCan be used in other &bProtocol&f: &eΣ&f.C.H.O. processes.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fConsume all on-hand to store in your account.","",
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
			EntityEffects.playSound(p, Sound.ITEM_AXE_SCRAPE, SoundCategory.AMBIENT);
			PlayerData.addScrap(p, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r&6♻&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
