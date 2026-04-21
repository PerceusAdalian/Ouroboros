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

public class HeresioEssence  extends AbstractObsObject
{

	public HeresioEssence() 
	{
		super(PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f Essence", "heresio_essence", Material.ENDER_EYE, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fAn eye of pure, unyielding "+PrintUtils.color(ObsColors.HERESIO)+"Knowledge&f glares at you expectantly..","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			ObsParticles.drawHeresioCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.HERESIO, e.getItem().getAmount());
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()+"&r&2⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
