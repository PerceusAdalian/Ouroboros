package com.ouroboros.menus.instances.protocolecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.eol.enums.MateriaState;
import com.eol.materia.Materia;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class CoreCraftingPage extends ObsGui
{

	public CoreCraftingPage(Player player) 
	{
		super(player, "Core Crafting", 54, Set.of(13,20,21,22,23,24,31,37,40,43));
	}

	@Override
	protected void build() 
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		GuiButton.button(Material.BLAZE_ROD).setName("&r&fCraft "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&e Core").setLore(setCoreLore(data, ElementType.INFERNO)).place(this, 20, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.INFERNO);
			build();
		});

		GuiButton.button(Material.NAUTILUS_SHELL).setName("&r&fCraft "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&e Core").setLore(setCoreLore(data, ElementType.GLACIO)).place(this, 21, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.GLACIO);
			build();
		});

		GuiButton.button(Material.RESIN_CLUMP).setName("&r&fCraft "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&e Core").setLore(setCoreLore(data, ElementType.GEO)).place(this, 23, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.GEO);
			build();
		});

		GuiButton.button(Material.AMETHYST_SHARD).setName("&r&fCraft "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&e Essence").setLore(setCoreLore(data, ElementType.AERO)).place(this, 24, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.AERO);
			build();
		});

		GuiButton.button(Material.END_CRYSTAL).setName("&r&fCraft "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&e Essence").setLore(setCoreLore(data, ElementType.CELESTIO)).place(this, 13, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.CELESTIO);
			build();
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&r&fCraft "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&e Essence").setLore(setCoreLore(data, ElementType.COSMO)).place(this, 22, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.COSMO);
			build();
		});

		GuiButton.button(Material.WITHER_SKELETON_SKULL).setName("&r&fCraft "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&e Essence").setLore(setCoreLore(data, ElementType.MORTIO)).place(this, 31, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.MORTIO);
			build();
		});
		
		GuiButton.button(Material.TOTEM_OF_UNDYING).setName("&r&fCraft "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&e Essence").setLore(setCoreLore(data, ElementType.HERESIO)).place(this, 40, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			addCore(p, ElementType.HERESIO);
			build();
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to \"&bProtocol&f: &e&lΣ&r&f&l.C.H.O.\" Main Page.").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BOOK_PUT, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ProtocolEchoMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BOOK_PUT, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();
	}

	private static List<String> setCoreLore(PlayerData data, ElementType eType)
	{
		List<String> lore = new ArrayList<>();
		lore.add("&r&fClick to craft. Craft cost: 100 "+PrintUtils.getElementTypeColor(eType)+"&l"+eType.getType()+" &eessence&f, and 25 &bScrap&f.");
		lore.add("");
		lore.add("Current "+PrintUtils.getElementTypeColor(eType)+"&l"+eType.getType()+" essence: &a"+data.getEssence(eType)+"&7/100");
		lore.add("Current &bScrap&f: &6"+data.getScrap()+"&7/25");
		return lore;
	}
	
	private static boolean addCore(Player p, ElementType eType)
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		if (data.getScrap() >= 25 && data.getEssence(eType) >= 100)
		{
			Materia materia = Materia.get(resolveInternalName(eType));
			if (materia == null) 
			{
				PrintUtils.OBSFormatError(p, "Could not resolve crafting item: Element Core. Please try again.");
				return false;
			}
			
			OBSParticles.playCastSigil(p, eType);
			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
			ItemStack stack = materia.getAsItemStack(MateriaState.ELEMENT_CORE);
			InventoryUtils.add(p, stack);
			PlayerData.addXP(p, StatType.REFINEMENT, 500);
			PlayerData.subtractScrap(p, 25);
			PlayerData.subtractEssence(p, eType, 100);
			data.save();
			return true;
		}
		EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
		return false;
	}
	
	private static String resolveInternalName(ElementType eType)
	{
		String internalName = switch(eType)
		{
			case INFERNO -> "inferno_core";
			case GLACIO -> "glacio_core";
			case GEO -> "geo_core";
			case AERO -> "aero_core";
			case CELESTIO -> "celestio_core";
			case MORTIO -> "mortio_core";
			case COSMO -> "cosmo_core";
			case HERESIO -> "heresio_core";
			default -> throw new IllegalArgumentException("Unexpected value: " + eType);
		};
		
		return internalName;
	}
	
}
