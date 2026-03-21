package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class InfernoEssence extends AbstractObsObject
{

	public InfernoEssence() 
	{
		super("&c&lInferno&r&f Essence", "inferno_essence", Material.BLAZE_POWDER, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fA simple wisp of &cFire&f essence.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your &c&lInferno&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawInfernoCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.INFERNO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r&c⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
