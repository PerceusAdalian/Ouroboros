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

public class HeresioEssence  extends AbstractObsObject
{

	public HeresioEssence() 
	{
		super("&2&lHeresio&r&f Essence", "heresio_essence", Material.ENDER_EYE, true, false, 
				"&r&fA simple, refracted essence of &2Knowledge&f.. &f&oThis feels &2&owrong&r&f&o..","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your &2&lHeresio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			OBSParticles.drawHeresioCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.HERESIO, e.getItem().getAmount()*1);
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()*1+"&r&2⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
