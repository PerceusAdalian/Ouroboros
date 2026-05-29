package com.lol.spells.instances.arcano;

import java.util.HashMap;
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
import com.lol.spells.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.accounts.PlayerHud;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.MoneyHandler;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ExtractEther extends Spell
{

	public ExtractEther()
	{
		super("Extract Ether", "concentrate_ether", Material.GHAST_TEAR, SpellType.UTILITY, SpellementType.ARCANO, CastConditions.MIXED, Rarity.THREE, 0, 15, false,
				true, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Extract Ether&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oExtract "+Symbols.MONEY+"&r&f --",
				"&r&fManifest &e"+Symbols.MONEY+" &fby withdrawing it from your reserves.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Extract Ether&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oExtract "+Symbols.LUMINITE+"&r&f --",
				"&r&fManifest &b"+Symbols.LUMINITE+" &fby withdrawing it from your reserves.");
	}
	
	public static HashMap<UUID, Boolean> awaitingInput = new HashMap<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{		
			if (awaitingInput.containsKey(p.getUniqueId()) && !awaitingInput.get(p.getUniqueId()))
			{
				cancel(p);
				return 0;
			}
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER);
			PrintUtils.Print(p, "&r&fPlease enter an amount you want to withdraw:");
			PrintUtils.Print(p, "&r&f(Type in chat to confirm. If you wish to cancel, recast this &e&oSpell&r&f.)");
			awaitingInput.put(p.getUniqueId(), false);
			
			return 0;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (awaitingInput.containsKey(p.getUniqueId()) && awaitingInput.get(p.getUniqueId()))
			{
				cancel(p);
				return 0;
			}
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.MASTER);
			PrintUtils.Print(p, "&r&fPlease enter an amount you want to withdraw:");
			PrintUtils.Print(p, "&r&f(Type in chat to confirm. If you wish to cancel, recast this &e&oSpell&r&f.)");
			awaitingInput.put(p.getUniqueId(), true);
			
			return 0;
		}
		
		return -1;
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
				
				if (!awaitingInput.containsKey(uuid)) return;
				
				boolean isMoney = awaitingInput.get(uuid);
				
				e.setCancelled(true);
				
				String message = e.getMessage().trim();
				
				if (message.isEmpty()) 
				{
					PrintUtils.Print(p, "&r&fPlease enter an amount you wish to withdraw:");
					PrintUtils.Print(p, "&r&f(Type in chat to confirm. If you wish to cancel, recast this &e&oSpell&r&f.)");
					return;
				}
				
				if (message.equals("Cancel"))
				{
					cancel(p);
					return;
				}
				
				int amount;
				try 
				{
				    amount = Integer.parseInt(message);
				} 
				catch (NumberFormatException ex) 
				{
				    PrintUtils.OBSFormatError(p, "&cInvalid input — please enter a number: &o0 < &lamount &r&c&o<= "+(isMoney ? PlayerData.fundsIntegerMax : PlayerData.maxLuminite)+"&r&c.");
				    return;
				}
				
				Bukkit.getScheduler().runTask(Ouroboros.instance, ()->
				{
					PlayerData data = PlayerData.getPlayer(uuid);
					
					if (data == null) 
					{
						cancel(p);
						return;
					}
					
					if (isMoney)
					{						
						if (data.getFunds(false) < amount)
						{
							PrintUtils.OBSFormatError(p, "&c&oInsufficient funds to conclude transaction!");
							cancel(p);
							return;
						}
						
						PlayerData.subtractMoney(p, amount);
						PlayerHud.update(p);
						InventoryUtils.add(p, MoneyHandler.of(amount).build());
						PrintUtils.OBSFormatPrint(p, "&b&l"+amount+" &r&eLuminite&r&f has been deducted from your account and manifested as a &e&oCore Crystal&r&f."+
								"\n&r&fNew Balance: "+data.getFunds(false)+"&e₪&f.");
						awaitingInput.remove(uuid);
					}
					else
					{
						if (data.getLuminite() < amount)
						{
							PrintUtils.OBSFormatError(p, "&c&oInsufficient Lumina Tears to conclude transaction!");
							cancel(p);
							return;
						}
						
						PlayerData.subtractLuminite(p, amount);
						InventoryUtils.addRecursively(p, new TearOfLumina().toItemStack(), amount);
						PrintUtils.OBSFormatPrint(p, "&e&l"+amount+" &r&bLumina Tears&r&f has been deducted from your account and manifested as physical item(s)."+
								"\n&r&fNew Balance: "+data.getLuminite()+"&e"+Symbols.LUMINITE+"&f.");
						awaitingInput.remove(uuid);
					}
					
				});
			}
		}, plugin);
	}
	
	public static void cancel(Player p)
	{
		if (p.isOnline())
		{
			PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oCanceled Withdraw..");
			ObsParticles.drawArcanoCastSigil(p);
		}
		awaitingInput.remove(p.getUniqueId());
	}
	
}
