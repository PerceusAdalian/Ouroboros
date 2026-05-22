package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import com.eol.echoes.EchoForge;
import com.eol.enums.MateriaComponent;
import com.eol.materia.Materia;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoForgePage extends ObsGui
{
	private ItemStack stack;
	private boolean forgeConfirmed = false;
	public EchoForgePage(Player player, ItemStack stack)
	{
		super(player, Symbols.EOL+"cho Forge", 27, Set.of(10,12,13,14,16));
		this.stack = stack;
	}

	@Override
	protected void build()
	{
		
		GuiButton.button(Material.DARK_OAK_SIGN)
		.setName("&7[&ei&7] &bProtocol &e&lΣ&r&f - &oEchoic Catalyzation of Harmonic Objects &r&7(&e&lΣ&r&f.C.H.O&7)")
		.setLore("&r&fForge a procedurally generated &b&o"+Symbols.EOL+"cho&r&f from &3Materia&f placed in the slots below.","",
				"&e&lCreation Catalyst &7(Slot 0)&f:",
				"&r&fDetermines the final &b&o"+Symbols.EOL+"cho's&r&f overall rarity,",
				"&r&fhow each Materia's rarity influences &oBase Stats&r&f,",
				"&r&fand the number of modifier slots generated.","",
				"&6Base &7(Slot 1)&f: ",
				"&r&fDetermines the &otype&r&f of &b&o"+Symbols.EOL+"cho&r&f produced.",
				"&r&fInfluences &oBase Attack&r&f, &oCritical Rate&r&f, &oCritical Modifier&r&f, and &bDurability&r&f.","",
				"&dBinding &7(Slot 2)&f: Determines the &b&o"+Symbols.EOL+"cho's&r&f &oAttack Rate&r&f and &oCritical&r&f bounds.","",
				"&3Element Core &7(Slot 3) &l[Optional]&r&f:",
				"&r&fEquips the &b&o"+Symbols.EOL+"cho&r&f with an &e&oElementium Slot&r&f,",
				"&r&fenabling ability socketing. Required by some &e&l"+Symbols.EOL+"OL&r&f items.","",
				"&e&l"+Symbols.EOL+"OL &r&fForging&f: Certain &b&o"+Symbols.EOL+"choes&r&f are crafted using a unique &e&lCatalyst&r&f,",
				"&r&frequiring a specific &3Materia&f recipe hinted within the Catalyst's description.",
				"&r&fRecipes are rarity-agnostic — only the &3Materia&f types matter, not their quality.",
				"&r&fThe resulting &b&o"+Symbols.EOL+"choes&r&f are among the most &opowerful &r&fweapons to wield.")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
		.setName("&r&f^^^ &bSlot 1&r&f ^^^")
		.setLore("&r&fPlace a valid &6Base&f above this indicator.")
		.place(this, 21, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
		.setName("&r&f^^^ &bSlot 2&r&f ^^^")
		.setLore("&r&fPlace a valid &dBinding&f above this indicator.")
		.place(this, 22, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
		.setName("&r&f^^^ &bSlot 3&r&f ^^^")
		.setLore("&r&fPlace a valid &3Element Core&f above this indicator.")
		.place(this, 23, e->
		{
			e.setCancelled(true);
		});
		
		//Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE)
		.setName("&a&lConfirm Forge")
		.setLore("Click to &a&lConfirm Forge&r&f.","&c&lWarning&r&f: consumed materials may not be recovered.")
		.place(this, 16, e->
		{
		    Player p = (Player) e.getWhoClicked();
		    e.setCancelled(true);

		    ItemStack item1 = this.getInventory().getItem(12);
		    ItemStack item2 = this.getInventory().getItem(13);
		    ItemStack item3 = this.getInventory().getItem(14);

		    if (item1 == null || item1.getType().isAir()) { rejectForge(p); return; }
		    if (item2 == null || item2.getType().isAir()) { rejectForge(p); return; }

		    Materia base = Materia.get(item1);
		    if (base == null || base.getMateriaComponent() != MateriaComponent.BASE || Materia.isUnrefined(item1))
		        { rejectForge(p); return; }

		    Materia binding = Materia.get(item2);
		    if (binding == null || binding.getMateriaComponent() != MateriaComponent.BINDING || Materia.isUnrefined(item2))
		        { rejectForge(p); return; }

		    Materia echoCore = (item3 != null && !item3.getType().isAir()) ? Materia.get(item3) : null;
		    if (item3 != null && !item3.getType().isAir() && (echoCore == null || echoCore.getMateriaComponent() != MateriaComponent.ELEMENT_CORE))
		        { rejectForge(p); return; }

		    if (item1.getAmount() > 1)
		    {
		        ItemStack leftover = item1.clone();
		        leftover.setAmount(item1.getAmount() - 1);
		        InventoryUtils.add(p, leftover);
		    }
		    if (item2.getAmount() > 1)
		    {
		        ItemStack leftover = item2.clone();
		        leftover.setAmount(item2.getAmount() - 1);
		        InventoryUtils.add(p, leftover);
		    }
		    if (echoCore != null && item3.getAmount() > 1)
		    {
		        ItemStack leftover = item3.clone();
		        leftover.setAmount(item3.getAmount() - 1);
		        InventoryUtils.add(p, leftover);
		    }

		    ItemStack forgedEcho = EchoForge.forge(stack, base, binding, echoCore);
		    EntityEffects.playSound(p, Sound.BLOCK_VAULT_OPEN_SHUTTER, SoundCategory.AMBIENT);
		    InventoryUtils.add(p, forgedEcho);

		    forgeConfirmed = true;
		    this.getInventory().clear();
		    ItemCollector.remove(stack);
		    GuiHandler.close(p);
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lCancel Forging").setLore("&r&fClick to &ccancel&f and &cExit&f the forge.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		paint();
	}

	private static void rejectForge(Player p)
	{
		EntityEffects.playSound(p, Sound.BLOCK_VAULT_EJECT_ITEM, SoundCategory.AMBIENT);
		PrintUtils.OBSFormatError(p, "One or all &3Materia&f were not valid. Please try again.");
		GuiHandler.close(p);
	}
	
	@Override
    public void close(InventoryCloseEvent e)
    {
        if (forgeConfirmed) return;
        ItemStack item1 = inv.getItem(12);
        ItemStack item2 = inv.getItem(13);
        ItemStack item3 = inv.getItem(14);
        if (item1 != null && !item1.getType().isAir()) InventoryUtils.add(player, item1);
        if (item2 != null && !item2.getType().isAir()) InventoryUtils.add(player, item2);
        if (item3 != null && !item3.getType().isAir()) InventoryUtils.add(player, item3);
    }
	
}
