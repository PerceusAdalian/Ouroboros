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

public class GlacioEssence extends AbstractObsObject
{

	public GlacioEssence() 
	{
		super(PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f Essence", "glacio_essence", Material.PRISMARINE_CRYSTALS, true, false, 
				PrintUtils.assignRarity(Rarity.ONE),"",
				"&r&fA simple, crystalized essence of "+PrintUtils.color(ObsColors.GLACIO)+"Water&f.","",
				"&r&fUsage: &d&oRight-Click&r&f",
				"&r&fCharge your "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f reservoir by consuming all on-hand.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (PlayerActions.rightClickAir(e)) 
		{
			e.setCancelled(true);
			ObsParticles.drawGlacioCastSigil(p);
			EntityEffects.playSound(p, Sound.ENTITY_CREAKING_FREEZE, SoundCategory.AMBIENT);
			PlayerData.addEssence(p, ElementType.GLACIO, e.getItem().getAmount());
			PrintUtils.PrintToActionBar(p, "&r&e+&f&l"+e.getItem().getAmount()+"&r&b⚛&f Added");
			ItemCollector.removeAll(e);
			return true;
		}
		return false;
	}

}
