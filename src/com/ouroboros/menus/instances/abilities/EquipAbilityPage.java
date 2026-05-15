package com.ouroboros.menus.instances.abilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManifestCodec;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.records.EchoManifest;
import com.ouroboros.enums.ElementType;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

import net.md_5.bungee.api.ChatColor;

public class EquipAbilityPage extends ObsGui
{
	public static Map<UUID, EchoAbility> echoEquip = new HashMap<>();
	
	public EquipAbilityPage(Player player)
	{
		super(player, "Ability Equip Confirmation", 27, Set.of(10,13,16));
	}

	@Override
	protected void build()
	{
		GuiButton.button(Material.GRAY_STAINED_GLASS_PANE)
		.setName("Place wand here")
		.setLore("&r&fClick this panel with your &b&lEcho&r&f to equip the selected ability.",
				"&r&fIf compatible, it will override any already-equipped ability.",
				"&r&c&lRequirements&r&f: The &b&lEcho&r&f's form must match,",
				"&r&fand a compatible &b&lElement Core&r&f must be slotted.")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			ItemStack stack = p.getItemOnCursor();
			if (stack != null && EchoManager.isEcho(stack) && !stack.getType().isAir())
			{
				ItemMeta meta = stack.getItemMeta();
				if (meta == null) 
				{
					e.setCancelled(true);
					return;
				}
			
				if (meta.getPersistentDataContainer().has(AbstractEOL.eolKey))
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
				
				EchoAbility ability = echoEquip.get(p.getUniqueId());
				
				boolean isModulo        = ability.getElementType() == ElementType.MODULO;
				boolean hasSlot         = codec.hasElementiumSlot();
				boolean formMismatch    = !ability.getEchoForm().equals(codec.getEchoForm());
				boolean elementMismatch = hasSlot && !ElementType.getFromElementiumSlotType(codec.getElementiumSlotType()).equals(ability.getElementType());

				boolean shouldCancel;
				// MODULO abilities ignore element matching but still require form match
				if (isModulo) shouldCancel = !hasSlot || formMismatch ;
				// Non-MODULO abilities require both element match and form match, and a slot must exist
				else shouldCancel = elementMismatch || formMismatch || !hasSlot;
				
				if (shouldCancel)
				{
				    e.setCancelled(true);
				    return;
				}
				
				// All checks passed, equip the ability
				if (codec.hasEquippedAbility()) EchoManager.removeAbility(stack);
				e.setCancelled(true);
				p.setItemOnCursor(null);
				InventoryUtils.add(p, EchoManager.equipAbility(stack, ability.getInternalName()));
				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatPrint(p, "&r&fSuccessfully equipped &e&oAbility&r&f: "+
						PrintUtils.getElementTypeColor(ability.getElementType())+"&l"+
						ChatColor.stripColor(ability.getDisplayName())+" &r&fto your &b&lEcho&r&f.");
				echoEquip.remove(p.getUniqueId());
				GuiHandler.close(p);
			}
			else e.setCancelled(true);
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
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to your the Magic Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			echoEquip.remove(p.getUniqueId());
			GuiHandler.changeMenu(p, new AbilitiesMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			echoEquip.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		
		paint();
	}

}
