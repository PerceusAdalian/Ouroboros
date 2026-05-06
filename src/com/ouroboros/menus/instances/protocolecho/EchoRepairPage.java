package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.EchoManifestCodec;
import com.eol.echoes.records.EchoManifest;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoRepairPage extends ObsGui
{
	
	public EchoRepairPage(Player player)
	{
		super(player, "Ability Equip Confirmation", 27, Set.of(10,13,16));
	}

	@Override
	protected void build()
	{
		GuiButton.button(Material.GRAY_STAINED_GLASS_PANE)
		.setName("Place wand here")
		.setLore("&r&fClick this panel with your &b&lEcho&r&f to repair it.",
				"&r&cNote: Repairs cost 50 &6"+Symbols.SCRAP+"&f, and will automatically",
				"&r&fconsume the amount once your &b&lEcho&r&f is placed here.")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			ItemStack stack = p.getItemOnCursor();
			
			if (!EchoManager.isEcho(stack))
			{
				e.setCancelled(true);
				return;
			}
			
			ItemMeta meta = stack.getItemMeta();
			if (meta == null)
			{
				e.setCancelled(true);
				return;
			}
			
			String echokey = meta.getPersistentDataContainer().get(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
			if (echokey == null)
			{
				e.setCancelled(true);
				return;
			}
			
			EchoManifest codec = EchoManifestCodec.fromJson(echokey);
			if (codec == null)
			{
				e.setCancelled(true);
				return;
			}
			
			if (codec.baseStats().getCurrentDurability() == codec.baseStats().getMaxDurability())
			{
				e.setCancelled(true);
				return;
			}
			
			ItemStack repairedEcho = EchoManager.modifyDurabilityAndReturn(stack, DurabilityOperation.SETMAX, 0);
			if (repairedEcho == null) 
			{
				e.setCancelled(true);
				return;
			}
			
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			if (data.getScrap() < 50) 
			{
				e.setCancelled(true); 
				return;
			}
			
			// All passed, return a repaired echo.
			PlayerData.subtractScrap(p, 50);
			p.setItemOnCursor(null);
			InventoryUtils.add(p, repairedEcho);
			EntityEffects.playSound(p, Sound.BLOCK_ANVIL_USE, SoundCategory.AMBIENT);
			PrintUtils.OBSFormatPrint(p, "&r&fYour &b&lEcho&r&f was repaired successfully.");
			GuiHandler.close(p);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 4, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 12, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 14, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 22, e->
		{
			e.setCancelled(true);
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to \"&bProtocol&f: &e&lΣ&r&f&l.C.H.O.&r&f\" Main Page.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ProtocolEchoMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		paint();
	}

}