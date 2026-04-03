package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EssenceReservoirsPage extends ObsGui
{

	public EssenceReservoirsPage(Player player) 
	{
		super(player, "Essence Reservoirs", 54, Set.of(13,20,21,22,23,24,31,37,40,43));
	}

	@Override
	protected void build() 
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		GuiButton.button(Material.FIRE_CHARGE).setName(PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f essence: "+data.getEssence(ElementType.INFERNO)+"&7/ 9999").place(this, 20, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.SNOWBALL).setName(PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f essence: "+data.getEssence(ElementType.GLACIO)+"&7/ 9999").place(this, 21, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.BRICK).setName(PrintUtils.color(ObsColors.GEO)+"&lGeo&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f essence: "+data.getEssence(ElementType.GEO)+"&7/ 9999").place(this, 23, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.WIND_CHARGE).setName(PrintUtils.color(ObsColors.AERO)+"&l&lAero&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f essence: "+data.getEssence(ElementType.AERO)+"&7/ 9999").place(this, 24, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.NETHER_STAR).setName(PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f essence: "+data.getEssence(ElementType.CELESTIO)+"&7/ 9999").place(this, 13, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.ECHO_SHARD).setName(PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f essence: "+data.getEssence(ElementType.COSMO)+"&7/ 9999").place(this, 22, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.WITHER_ROSE).setName(PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f essence: "+data.getEssence(ElementType.MORTIO)+"&7/ 9999").place(this, 31, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});
		
		GuiButton.button(Material.ENDER_EYE).setName(PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&e Essence")
		.setLore("Current "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f essence: "+data.getEssence(ElementType.HERESIO)+"&7/ 9999").place(this, 40, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
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

	
}
