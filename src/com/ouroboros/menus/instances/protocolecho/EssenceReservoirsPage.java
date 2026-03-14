package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class EssenceReservoirsPage extends AbstractOBSGui
{

	public EssenceReservoirsPage(Player player) 
	{
		super(player, "Essence Reservoirs", 54, Set.of(13,20,21,22,23,24,31,37,40,43));
	}

	@Override
	protected void build() 
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno&r&e Essence").setLore("Current &c&lInferno&r&f essence: "+data.getEssence(ElementType.INFERNO)+"&7/ 9999").place(this, 20, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio&r&e Essence").setLore("Current &b&lGlacio&r&f essence: "+data.getEssence(ElementType.GLACIO)+"&7/ 9999").place(this, 21, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.BRICK).setName("&6&lGeo&r&e Essence").setLore("Current &6&lGeo&r&f essence: "+data.getEssence(ElementType.GEO)+"&7/ 9999").place(this, 23, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero&r&e Essence").setLore("Current &d&lAero&r&f essence: "+data.getEssence(ElementType.AERO)+"&7/ 9999").place(this, 24, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.NETHER_STAR).setName("&e&lCelestio&r&e Essence").setLore("Current &e&lCelestio&r&f essence: "+data.getEssence(ElementType.CELESTIO)+"&7/ 9999").place(this, 13, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo&r&e Essence").setLore("Current &3&lCosmo&r&f essence: "+data.getEssence(ElementType.COSMO)+"&7/ 9999").place(this, 22, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});

		GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio&r&e Essence").setLore("Current &4&lMortio&r&f essence: "+data.getEssence(ElementType.MORTIO)+"&7/ 9999").place(this, 31, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_AMBIENT_SHORT, SoundCategory.MASTER);
		});
		
		GuiButton.button(Material.ENDER_EYE).setName("&2&lHeresio&r&e Essence").setLore("Current &2&lHeresio&r&f essence: "+data.getEssence(ElementType.HERESIO)+"&7/ 9999").place(this, 40, e->
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
