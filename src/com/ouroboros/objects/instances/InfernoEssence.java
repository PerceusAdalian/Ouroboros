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
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class InfernoEssence extends AbstractObsObject
{

	public InfernoEssence() 
	{
		super(PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f Essence", "inferno_essence", Material.BLAZE_POWDER, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fA simple wisp of "+PrintUtils.color(ObsColors.INFERNO)+"Fire&f essence.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f reservoir by consuming all on-hand.","",
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
			PlayerData.addEssence(p, ElementType.INFERNO, e.getItem().getAmount());
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()+"&r&c⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
