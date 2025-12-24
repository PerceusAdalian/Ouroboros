package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class TearOfLumina extends AbstractObsObject
{

	public TearOfLumina() 
	{
		super("Lesser Tear of Lumina", "tear_of_lumina_1", Material.GHAST_TEAR, true, true, 
				"A remembrance of &e&oPriestess Lumina&r&f, her tears etched into the fabric reality.",
				"&r&f&l&nUsage&r&f: &d&oRight-Click&r&f to add all on hand to your &b&oLuminite Reservoire&r&f.",
				"Can also be used for Alchemy.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.CLOUD, null);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.WAX_ON, null);
			EntityEffects.playSound(p, Sound.BLOCK_POINTED_DRIPSTONE_DRIP_WATER, SoundCategory.MASTER);
			EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER);
			PlayerData.addLuminite(p, e.getItem().getAmount());
			PrintUtils.PrintToActionBar(p, "&r&b+&f&l"+e.getItem().getAmount()+"&r&b۞&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		
		return false;
	}

}
