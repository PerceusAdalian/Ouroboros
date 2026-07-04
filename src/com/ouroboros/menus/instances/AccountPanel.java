package com.ouroboros.menus.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.NumberUtils;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AccountPanel extends ObsGui
{
	public AccountPanel(Player player)
	{
		super(player, buildTitle(player), 54, Set.of());
	}

	private static String buildTitle(Player player)
	{
		String name = player.getDisplayName();
		char last = Character.toLowerCase(name.charAt(name.length() - 1));
		String possessive = last == 's' ? name + "'" : name + "'s";
		return possessive + " Account";
	}

	@Override
	protected void build()
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		boolean maxDefaultHP = data.getDefaultHP() == PlayerData.playerHpMax;
		boolean maxDefaultAR = data.getDefaultArmor() == PlayerData.playerArmorMax;
		
		int upgradeCostLuminiteHP = (int) NumberUtils.lerp(1500, 1000000, (int) data.getDefaultHP(), (int) PlayerData.playerHpMin, (int) PlayerData.playerHpMax, 2.0);
		int upgradeEssenceCostHP = data.getDefaultHP() >= PlayerData.playerHpMax/2 
				? (int) NumberUtils.lerp(10, 100, (int) data.getDefaultHP(), (int) PlayerData.playerHpMax/2, (int) PlayerData.playerHpMax, 2.0) 
				: 0;
		
		int upgradeCostLuminiteAR = (int) NumberUtils.lerp(5000, 2500000, data.getDefaultArmor(), PlayerData.playerArmorMin, PlayerData.playerArmorMax, 2.0);
		int upgradeEssenceCostAR = data.getDefaultArmor() >= PlayerData.playerArmorMax/2 
				? (int) NumberUtils.lerp(10, 100, data.getDefaultArmor(), PlayerData.playerArmorMax/2, PlayerData.playerArmorMax, 2.0) 
				: 0;
		
		List<String> hpLore = new ArrayList<>();
		if (maxDefaultHP)
			hpLore.add("&r&fUnable to further &e&lUpgrade&r&f maximum default &aHP&c"+Symbols.HP+"&r&f.");
		else
		{
			hpLore.add("&r&fClick to &e&lUpgrade&r&f your maximum default &aHP&c"+Symbols.HP+"&r&f.");
			hpLore.add("&r&fDoing so will increase your &aHP&c"+Symbols.HP+"&r&f total: "+(int)data.getDefaultHP()+" &e-> &b&l"+(int)(data.getDefaultHP()+250d));
			hpLore.add("");
			hpLore.add("&e&lUpgrade Cost&r&f:");
			hpLore.add("&r&f  - "+upgradeCostLuminiteHP+"&e"+Symbols.MONEY+"&7/"+data.getFunds(false));
			if (upgradeEssenceCostHP > 0)
				hpLore.add("&r&f  - "+upgradeEssenceCostHP+PrintUtils.color(ObsColors.ARDENTIO)+Symbols.ESSENCE+"&7/"+data.getEssence(ElementType.ARDENTIO));
		}
		GuiButton.button(maxDefaultHP ? Material.BARRIER : Material.EMERALD).setName("&e&lUpgrade &r&f-- &aHP&c"+Symbols.HP).setLore(hpLore)
		.place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (maxDefaultHP || data.getFunds(false) < upgradeCostLuminiteHP || data.getEssence(ElementType.ARDENTIO) < upgradeEssenceCostHP)
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT);
				e.setCancelled(true);
				return;
			}
			else
			{
				PlayerData.subtractMoney(p, upgradeCostLuminiteHP);
				PlayerData.subtractEssence(p, ElementType.ARDENTIO, upgradeEssenceCostHP);
				int hpPre = (int) data.getDefaultHP();
				data.setDefaultHP(data.getDefaultHP()+250);
				data.save();
				PlayerData.fullRestore(p);

				
				PrintUtils.OBSFormatPrint(p, "&r&fSuccessfully &e&lUpgraded &r&aHP&c"+Symbols.HP+"&f: &7"+hpPre+" &e-> &b&l"+data.getDefaultHP()+"&r&f!");
				EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
				GuiHandler.reload(p);
			}
		});;
		
		List<String> arLore = new ArrayList<>();
		if (maxDefaultAR)
			arLore.add("&r&fUnable to further &e&lUpgrade&r&f maximum default &6AR"+Symbols.ARMOR+"&r&f.");
		else
		{
			arLore.add("&r&fClick to &e&lUpgrade&r&f your maximum default &6AR"+Symbols.ARMOR+"&r&f.");
			arLore.add("&r&fDoing so will increase your &6AR"+Symbols.ARMOR+"&r&f total: "+data.getDefaultArmor()+" &e-> &b&l"+(int)(data.getDefaultArmor()+50));
			arLore.add("");
			arLore.add("&e&lUpgrade Cost&r&f:");
			arLore.add("&r&f  - "+upgradeCostLuminiteAR+"&e"+Symbols.MONEY+"&7/"+data.getFunds(false));
			if (upgradeEssenceCostAR > 0)
				arLore.add("&r&f  - "+upgradeEssenceCostAR+PrintUtils.color(ObsColors.ARDENTIO)+Symbols.ESSENCE+"&7/"+data.getEssence(ElementType.ARDENTIO));
		}
		GuiButton.button(maxDefaultAR ? Material.BARRIER : Material.COPPER_CHESTPLATE).setName("&e&lUpgrade &r&f-- &6AR"+Symbols.ARMOR).setLore(arLore)
		.place(this, 19, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (maxDefaultAR || data.getFunds(false) < upgradeCostLuminiteAR || data.getEssence(ElementType.ARDENTIO) < upgradeEssenceCostAR)
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT);
				e.setCancelled(true);
				return;
			}
			else
			{
				PlayerData.subtractMoney(p, upgradeCostLuminiteAR);
				PlayerData.subtractEssence(p, ElementType.ARDENTIO, upgradeEssenceCostAR);
				int armorPre = (int) data.getDefaultArmor();
				data.setDefaultArmor(data.getDefaultArmor()+50);
				data.save();
				PlayerData.fullRestore(p);
				
				PrintUtils.OBSFormatPrint(p, "&r&fSuccessfully &e&lUpgraded &r&6AR"+Symbols.ARMOR+"&f: &7"+armorPre+" &e-> &b&l"+data.getDefaultArmor()+"&r&f!");
				EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
				GuiHandler.reload(p);
			}
		});;
		
		//Exits
		GuiButton.placeGoBack(37, this, new ObsMainMenu(player));
		GuiButton.placeExit(43, this);
		
		paint();
	}
}
