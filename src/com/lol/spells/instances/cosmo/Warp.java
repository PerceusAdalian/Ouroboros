package com.lol.spells.instances.cosmo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.WarpMenu;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Warp extends Spell
{

	public Warp()
	{
		super("Warp", "warp", Material.OMINOUS_TRIAL_KEY, SpellType.UTILITY, SpellementType.COSMO, CastConditions.MIXED, Rarity.FIVE, 100, 1, false, true,
				"&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&3Warp&f: &3&oWarp Core Protocol γ&r&f --",
				"&r&aInitialize&f/&bExecute &3Warp Cores&f with saved locations.",
				"&b&lMana&r&f is deducted only on &3Teleportation&f.","",
				"&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&3Warp&f: &3&oWarp Core Protocol δ&r&f --",
				"&r&cDelete&f currently registered &3Warp Core Data&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.BLOCK_VAULT_ACTIVATE, SoundCategory.AMBIENT);
			GuiHandler.open(p, new WarpMenu(p, new Wand(e.getItem()), true));
			return -1;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
			CollectWandData.wandCollector.put(p.getUniqueId(), new Wand(e.getItem()));
			ItemCollector.remove(e);
			GuiHandler.open(p, new WarpMenu(p, new Wand(e.getItem()), false));
			return -1;
		}

		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 0;
	}
	
	public static Map<UUID, Integer> awaitingWarpNickname = new HashMap<>();
	
	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
		    public void onPlayerChat(AsyncPlayerChatEvent e)
		    {
		        Player p = e.getPlayer();
		        UUID uuid = p.getUniqueId();

		        if (!awaitingWarpNickname.containsKey(uuid)) return;

		        e.setCancelled(true);

		        String nickname = e.getMessage().trim();
		        int warpIndex = awaitingWarpNickname.get(uuid);

		        if (nickname.isEmpty() || nickname.length() > 16)
		        {
		        	PrintUtils.OBSFormatError(p, "&cInvalid nickname -- must be 1–16 characters. Please try again..");
		            return;
		        }

		        awaitingWarpNickname.remove(uuid);

		        Bukkit.getScheduler().runTask(Ouroboros.instance, () ->
		        {
		        	Wand wand = CollectWandData.wandCollector.get(uuid);
		        	if (wand != null) p.getInventory().addItem(wand.getAsItemStack());
		        	
		            PlayerData data = PlayerData.getPlayer(uuid);
		            data.setWarpData(warpIndex, p.getLocation(), nickname);
		            PrintUtils.OBSFormatPrint(p, "&3&lWarp Core &r&b#&e&l" + warpIndex + "&r&f set to "+ PrintUtils.color(ObsColors.COSMO) + "&l" + nickname + "&r&f!");
		            EntityEffects.playSound(p, Sound.BLOCK_VAULT_ACTIVATE, SoundCategory.AMBIENT);
		        });
		    }
		}, plugin);
	}
	
}
