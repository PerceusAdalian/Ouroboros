package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Particle;
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

public class LuminiteFragment extends AbstractObsObject
{

	public LuminiteFragment() 
	{
		super("Luminite Fragment", "money_tier3", Material.RAW_GOLD, true, true,
				"&r&fA medium concentration of &e&l&oLuminite&r&f.",
				"&r&fIt is a semi-coherent chunk with a whirring soft glow.",
				"&r&f&lRight-Click&r&f to harness into &l250&r&e₪&f.",
				"\n","&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.CLOUD, null);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.WAX_ON, null);
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.MASTER, 1, 1);
			PlayerData.addMoney(p, e.getItem().getAmount()*250);
			PrintUtils.PrintToActionBar(p, "&r&b+&f&l"+e.getItem().getAmount()*250+"&r&e₪&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
