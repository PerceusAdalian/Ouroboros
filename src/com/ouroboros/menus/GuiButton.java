package com.ouroboros.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lol.spells.instances.Spell;
import com.ouroboros.abilities.instances.ObsAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.menus.instances.abilities.AbilityConfirmationPage;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class GuiButton 
{
	private final ItemStack item;
    private final ItemMeta meta;
    private final List<String> lore = new ArrayList<>();
    
    public GuiButton(Material material) 
    {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }
    
    public static GuiButton button(Material material) 
    {
        return new GuiButton(material);
    }
    
    public GuiButton setName(String name) 
    {
        meta.setDisplayName(PrintUtils.ColorParser(name));
        return this;
    }

    public GuiButton setLore(String...lines) 
    {
        for (String line : lines) 
        {
            lore.add(PrintUtils.ColorParser("&r&f" + line));
        }
        return this;
    }
    
    public GuiButton setLore(List<String> lines) 
    {
    	for (String line : lines) 
        {
            lore.add(PrintUtils.ColorParser("&r&f" + line));
        }
        return this;
    }
    
    public void place(ObsGui gui, int slot, Consumer<InventoryClickEvent> action) 
    {
    	meta.setLore(lore);
        item.setItemMeta(meta);
        gui.getInventory().setItem(slot, item);
        gui.clickActions.put(slot, action);
    }
    

	public static Map<Player, ObsAbility> abilityConfirmMap = new HashMap<>();
	public static Map<Player, Boolean> confirmRegister = new HashMap<>();
	
    public static void placeAbilityButton(Player player, ObsAbility ability, int slot, ObsGui gui) 
	{
	    ItemStack stack = ability.toIcon(player);

	    GuiButton.button(stack.getType())
	        .setName(stack.getItemMeta().getDisplayName())
	        .setLore(stack.getItemMeta().getLore())
	        .place(gui, slot, e -> 
	        {
	            Player p = (Player) e.getWhoClicked();
	            if (PlayerData.canRegister(p.getUniqueId(), ability)) 
	    		{
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, true);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isRegistered()) 
	    	    {
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, false);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else 
	    	    {
	    	        e.setCancelled(true);
	    	        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
	    	    }
	        });
	}
    
    public static Map<UUID, Spell> spellActivateConfirm = new HashMap<>();
    
    public static void placeSpellButton(Player player, Spell spell, int slot, ObsGui gui)
	{
    	ItemStack spellIcon = spell.getAsItemStack(true);
		
		boolean spellRegistered = PlayerData.getPlayer(player.getUniqueId()).getSpell(spell).isRegistered();
		if (spellRegistered)
		{	        	
			GuiButton.button(spellIcon.getType())
			.setName(PrintUtils.getElementTypeColor(spell.getElementType()) + "&l" + ChatColor.stripColor(spellIcon.getItemMeta().getDisplayName()))
			.setLore(spellIcon.getItemMeta().getLore())
			.place(gui, slot, e -> 
			{
				Player p = (Player) e.getWhoClicked();
				EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
				spellActivateConfirm.put(player.getUniqueId(), spell);
				CollectWandData.pageController.put(p.getUniqueId(), "spellselect");
				GuiHandler.changeMenu(p, new CollectWandData(p));
			});
		}
		else
		{
			// Create obfuscated lore as a List<String>
            List<String> obfuscatedLore = new ArrayList<>();
            for (String line : spellIcon.getItemMeta().getLore())
            {
                // Strip existing color codes and make obfuscated
                String plainText = line.replaceAll("§[0-9a-fk-or]", "").replaceAll("&[0-9a-fk-or]", "");
                obfuscatedLore.add(PrintUtils.ColorParser("&7&k" + plainText));
            }
            
            GuiButton.button(Material.PAPER)
            .setName("&cLocked Spell&f: "+PrintUtils.getElementTypeColor(spell.getElementType())+"&l&k" + ChatColor.stripColor(spellIcon.getItemMeta().getDisplayName()))
            .setLore(obfuscatedLore)
            .place(gui, slot, e -> 
            {
                Player p = (Player) e.getWhoClicked();
                EntityEffects.playSound(p, Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER);
                e.setCancelled(true);
            });
		}
	}
    
    public static void placeCantripSpellButton(Player player, Spell spell, int slot, ObsGui gui)
	{
		ItemStack spellIcon = spell.getAsItemStack(true);
		
		boolean spellRegistered = PlayerData.getPlayer(player.getUniqueId()).getSpell(spell).isRegistered();
		int essenceHeld = PlayerData.getPlayer(player.getUniqueId()).getEssence(ElementType.getFromSpellement(spell.getElementType()));
		int luminaTearsHeld = PlayerData.getPlayer(player.getUniqueId()).getLuminite();
		String spellName = PrintUtils.getElementTypeColor(spell.getElementType())+ "&l" + ChatColor.stripColor(spellIcon.getItemMeta().getDisplayName());
		
		List<String> lore = new ArrayList<>();
		
		if (!spellRegistered)
		{			
			lore.add("&r&c&oPreview&r&f:");
			lore.add("");
			lore.addAll(spellIcon.getItemMeta().getLore());
			lore.add("");
			lore.add("&r&fClick to register this &aCantrip &fwithout a &eSpell Tome&f.");
			lore.add("&r&eRegistration Cost&f: "+25+PrintUtils.getElementTypeColor(spell.getElementType())+"⚛&r&7/"+essenceHeld+"&r&f, 30&b۞&7/"+luminaTearsHeld);
		}
		else
		{
			lore.addAll(spellIcon.getItemMeta().getLore());
		}
		
		GuiButton.button(!spellRegistered ? Material.PAPER : spellIcon.getType())
		.setName(!spellRegistered ? "&cLocked Spell&f: " + spellName : spellName)
		.setLore(lore)
		.place(gui, slot, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (spellRegistered)
			{
				EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
				spellActivateConfirm.put(player.getUniqueId(), spell);
				CollectWandData.pageController.put(p.getUniqueId(), "spellselect");
				GuiHandler.changeMenu(p, new CollectWandData(p));
			}
			else
			{
				if (essenceHeld < 25 || luminaTearsHeld < 30) 
				{
					EntityEffects.playSound(p, Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER);
	                e.setCancelled(true);
	                return;
				}
				
				EntityEffects.playSound(player, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
				PlayerData.subtractEssence(player, ElementType.getFromSpellement(spell.getElementType()), 25);
				PlayerData.subtractLuminite(player, 30);
				PlayerData.getPlayer(player.getUniqueId()).getSpell(spell).setRegistered(true);
				GuiHandler.reload(player);
			}
		});
	}
    
}
