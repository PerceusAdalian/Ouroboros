package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Random;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class LuminiteEssence extends AbstractObsObject
{

	public LuminiteEssence() 
	{
		super("Luminite Essence", "money_random", Material.RESIN_CLUMP, true, true, 
				"&r&fAn unstable conglomerate of &e&l&oLuminite&r&f.",
				"&r&fIt is an incandescent resin with variable waveform.. Just looking at it confuses you.",
				"&r&f&lRight-Click&r&f to harness into &l1-10000&r&e₪&f.",
				"\n","&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			Random rand = new Random();
			int money = rand.nextInt(10000 - 1 + 1) + 1; // Weird range calc, idk, thanks AI Overview.. :)
			int sum = 0;
			for (int i = 0; i <= e.getItem().getAmount(); i++)
			{
				sum += money;
			}
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.CLOUD, null);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.WAX_ON, null);
			p.playSound(p.getLocation(), Sound.ITEM_HONEYCOMB_WAX_ON, SoundCategory.MASTER, 1, 1);
			
			PlayerData.addMoney(p, sum);
			PrintUtils.PrintToActionBar(p, "&r&b+&f&l"+sum+"&r&e₪&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
