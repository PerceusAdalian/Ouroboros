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

public class LuminiteShard extends AbstractObsObject
{

	public LuminiteShard() 
	{
		super("Luminite Shard", "money_tier2", Material.GOLD_NUGGET, true, true,
				"&r&fA small concentration of &e&l&oLuminite&r&f.",
				"&r&fIt is a shard with faint humming and static radiation.",
				"&r&f&lRight-Click&r&f to harness into &l150&r&e₪&f.",
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
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1, 1);
			PlayerData.addMoney(p, 150);
			PrintUtils.PrintToActionBar(p, "&r&b+&f&l150&r&e₪&f Added");
			ItemCollector.remove(e);
			return true;
		}
		return false;
	}

}
