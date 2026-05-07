package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.MoneyHandler;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class MoneyObject extends AbstractObsObject
{
    public MoneyObject(int value)
    {
        super("Luminite Core", "money_object", Material.SUNFLOWER, true, true,
                "&r&fA concentration of &e&l&oLuminite&r&f.","",
                "&r&d&oRight-Click&r&f to harness into: &l" + value + "&r&e" + Symbols.MONEY,"",
                "&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
    }

    public MoneyObject()
    {
        this(0);
    }

    @Override
    public boolean cast(PlayerInteractEvent e)
    {
    	Player p = e.getPlayer();
		if (!PlayerActions.rightClickAir(e)) return false;
		
		ObsParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.CLOUD, null);
		ObsParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 1, 10, 1.0, Particle.WAX_ON, null);
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.AMBIENT);
		
		int amount = e.getItem().getAmount() * MoneyHandler.readValue(e.getItem());
		PlayerData.addMoney(p, amount);
		PrintUtils.PrintToActionBar(p, "&r&b+&f&l"+amount+"&r&e₪&f Added");
		ItemCollector.removeAll(e);
		
		return true;
    }
}