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

public class CosmoEssence extends AbstractObsObject
{

	public CosmoEssence() 
	{
		super(PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f Essence", "cosmo_essence", Material.DRAGON_BREATH, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fAn impossible asbtraction of "+PrintUtils.color(ObsColors.COSMO)+"Space and Time&f.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawCosmoCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_SHULKER_TELEPORT, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.COSMO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r"+PrintUtils.color(ObsColors.COSMO)+"⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
