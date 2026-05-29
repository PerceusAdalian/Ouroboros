package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManifestCodec;
import com.eol.materia.Materia;
import com.ouroboros.enums.Rarity;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.objects.instances.ScrapMateria;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ScrapPage extends ObsGui
{

	public ScrapPage(Player player)
	{
		super(player, "Scrap Page", 27, Set.of());
	}

	@Override
	protected void build()
	{
		GuiButton.button(Material.HEART_OF_THE_SEA).setName("Scrap Capsule").setLore("Place valid item(s) here.")
        .place(this, 13, e ->
        {
            Player p = (Player) e.getWhoClicked();
            ItemStack stack = p.getItemOnCursor();

            if (stack == null || stack.getType().isAir())
            {
                e.setCancelled(true);
                return;
            }

            PersistentDataContainer meta = stack.getItemMeta().getPersistentDataContainer();
            Rarity rarity = null;
            boolean isEcho = false;

            if (meta.has(EchoManifestCodec.MANIFEST_KEY))
            {
                isEcho = true;
                rarity = EchoManager.getCodec(stack).rarity();
            }
            else if (meta.has(Materia.materiaKey) && !Materia.isUnrefined(stack))
            {
                rarity = Materia.get(stack).getRarity();
            }
            else
            {
                EntityEffects.playSound(p, Sound.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT);
                e.setCancelled(true);
                return;
            }

            int scrapAmount = isEcho ? rarity.getRarity() * 15 : rarity.getRarity() * stack.getAmount();
            InventoryUtils.addRecursively(p, new ScrapMateria().toItemStack(), scrapAmount);
            
            p.setItemOnCursor(null);
            EntityEffects.playSound(p, Sound.BLOCK_VAULT_EJECT_ITEM, SoundCategory.AMBIENT);
            GuiHandler.reload(p);
        });
		
		//Reticle
		GuiButton.button(Material.OAK_SIGN).setName("&7[&ei&7]").setLore(
				"&r&fInsert any &3Materia &for &b"+Symbols.EOL+"cho&f into the slot below.",
				"&r&6Scrap "+Symbols.SCRAP+" &fwill be returned scaled by the object's &d&oRarity&r&f.","",
				"&c&lWarning&r&f: scrapped items cannot be recovered.")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		
		//Exits
		GuiButton.placeGoBack(10, this, new ProtocolEchoMainPage(player));
		GuiButton.placeExit(16, this);
		
		paint();
	}

}
