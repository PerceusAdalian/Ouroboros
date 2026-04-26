package com.lol.spells.instances.arcano;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
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
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Sigil extends Spell
{

	public Sigil()
	{
		super("Sigil", "sigil", Material.MOJANG_BANNER_PATTERN, SpellType.UTILITY, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 0, 1, true,
				"&r&fTrade &eLuminite&f with &6target &c&oPlayer &r&7(45m)");
	}

	public static Map<UUID, UUID> awaitingTrade = new HashMap<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 45, target ->
		{
			if (!(target instanceof Player pTarget)) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.playSound(pTarget, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER);
			ObsParticles.drawLine(p.getLocation(), pTarget.getLocation(), 0.5, 0.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawLine(p.getLocation(), pTarget.getLocation(), 3, 0.5, Particle.END_ROD, null);
			ObsParticles.drawArcanoCastSigil(pTarget);
			PrintUtils.OBSFormatDebug(p, "&r&fInitialized &e&otrade &r&fwith: &b&o"+pTarget.getDisplayName()+"&r&f..");
			PrintUtils.Print(p, "&r&fPlease enter an amount you wish to send to: &b&o"+pTarget.getDisplayName()+":");
			PrintUtils.Print(p, "&7&o(Type in chat to confirm. If you wish to cancel the transaction, type \"Cancel\".");
			PrintUtils.OBSFormatDebug(pTarget, "&r&fPlayer: &e&o"+p.getDisplayName()+"&r&f has initialized a &e&otrade&r&f with you..");
			awaitingTrade.put(p.getUniqueId(), pTarget.getUniqueId());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
			{
			    if (!awaitingTrade.containsKey(p.getUniqueId())) return;
			    cancelTrade(p, pTarget);
			    PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&lSigil&r&f: Trade &c&ltimed out&r&f.");
			}, 60 * 20L);
		})) return -1;
		
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		return 0;
	}

	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onPlayerChat(AsyncPlayerChatEvent e)
			{
				Player p = e.getPlayer();
				UUID uuid = p.getUniqueId();
				
				if (!awaitingTrade.containsKey(uuid)) return;
				Player pTarget = Bukkit.getPlayer(awaitingTrade.get(uuid));
				
				e.setCancelled(true);
				
				String message = e.getMessage().trim();
				
				if (message.isEmpty()) 
				{
					PrintUtils.Print(p, "&r&fPlease enter an amount you wish to send to: &b&o"+pTarget.getDisplayName()+":");
					PrintUtils.Print(p, "&7&o(Type in chat to confirm. If you wish to cancel the transaction, type \"Cancel\".");
					return;
				}
				
				if (message.equals("Cancel"))
				{
					cancelTrade(p, pTarget);
					return;
				}
				
				int amount;
				try 
				{
				    amount = Integer.parseInt(message);
				} 
				catch (NumberFormatException ex) 
				{
				    PrintUtils.OBSFormatError(p, "&cInvalid input — please enter a number: &o0 < &lamount &r&c&o<= "+PlayerData.maxLuminite+"&r&c.");
				    return;
				}
				
				Bukkit.getScheduler().runTask(Ouroboros.instance, ()->
				{
					PlayerData data = PlayerData.getPlayer(uuid);
					PlayerData targetData = PlayerData.getPlayer(pTarget.getUniqueId());
					
					if (data == null || targetData == null || !pTarget.isOnline()) 
					{
						cancelTrade(p, pTarget);
						return;
					}
					if (data.getLuminite() < amount)
					{
						PrintUtils.OBSFormatError(p, "&c&oInsufficient funds to conclude transaction!");
						cancelTrade(p, pTarget);
						return;
					}

					PlayerData.subtractLuminite(p, amount);
					PlayerData.addLuminite(pTarget, amount);
					PrintUtils.OBSFormatPrint(p, "&b&l"+amount+" &r&eLuminite&r&f has been deducted from your account and added to: &b&o"+pTarget.getDisplayName()+
							"\n&r&fNew Balance: "+data.getLuminite()+"&e₪&f.");
					PrintUtils.OBSFormatPrint(pTarget, "&r&fPlayer: &b&o"+p.getDisplayName()+"&r&f has transfered &b&l"+amount+
							" &r&eLuminite&f to your account. \n&r&fNew Balance: "+data.getLuminite()+"&e₪&f.");
					awaitingTrade.remove(uuid);
				});
			}
		}, plugin);
	}
	
	public static void cancelTrade(Player p, Player target)
	{
		if (target.isOnline()) 
		{
			PrintUtils.OBSFormatDebug(target, "&r&fPlayer: &e&o"+p.getDisplayName()+"&r&f has canceled a &e&otrade&r&f with you..");
			ObsParticles.drawArcanoCastSigil(target);
		}
		if (p.isOnline())
		{
			PrintUtils.OBSFormatDebug(p, "&r&eTrade &c&lCanceled&r&f with player: &b&o"+target.getDisplayName()+"&r&f.");
			ObsParticles.drawArcanoCastSigil(p);
		}
		awaitingTrade.remove(p.getUniqueId());
	}
}
