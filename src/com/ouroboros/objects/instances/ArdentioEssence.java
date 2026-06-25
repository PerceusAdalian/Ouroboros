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
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ArdentioEssence extends AbstractObsObject
{

	public ArdentioEssence() 
	{
		super(PrintUtils.color(ObsColors.ARDENTIO)+"&lArdentio&r&f Essence", "arcano_essence", Material.EMERALD, true, false, 
				PrintUtils.assignRarity(Rarity.FIVE),"",
				"&r&fA verdant and lustrous essence of "+PrintUtils.color(ObsColors.ARDENTIO)+"Life&f.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.ARDENTIO)+"&lArdentio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			ObsParticles.drawArdentioCastSigil(p);
			EntityEffects.playSound(p, Sound.BLOCK_COPPER_BULB_BREAK, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.ARDENTIO, e.getItem().getAmount());
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()+"&r"+PrintUtils.color(ObsColors.ARDENTIO)+"⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
