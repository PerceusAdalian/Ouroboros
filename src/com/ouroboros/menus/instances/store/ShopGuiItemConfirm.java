package com.ouroboros.menus.instances.store;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.InventoryUtils;

public class ShopGuiItemConfirm extends ObsGui
{
    ShopEntry entry;

    public ShopGuiItemConfirm(Player player, ShopEntry entry)
    {
        super(player, "Confirm Purchase", 27, Set.of(4, 10, 11, 12, 13, 14, 15, 16));
        this.entry = entry;
    }

    @Override
    protected void build()
    {
        GuiButton.button(Material.OAK_SIGN)
            .setName("&r&f[&e&li&r&f]&o Purchase Terms and Conditions")
            .setLore(
                "&r&a&oConfirm&r&f your &e&opurchase&r&f below by choosing the &oamount&r&f you'd wish to buy.",
                "&r&fThe cost associated is listed in the &b&oitem's description&r&f.",
                "&r&f&l&oNecessary currencies &r&fmust be present to complete the purchase.",
                "&r&fYou may continue to &omake multiple purchases of the same item&r&f.",
                "To close or return, click &c&o'Exit'&r&f or &a&o'Go Back'&r&f.",
                "&r&f&nThank you for choosing to shop with us&r&f.")
            .place(this, 4, e ->
            {
                e.setCancelled(true);
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
            });

        // Purchase buttons
        placePurchaseButton(entry, 1,  11);
        placePurchaseButton(entry, 8,  12);
        placePurchaseButton(entry, 16, 13);
        placePurchaseButton(entry, 32, 14);
        placePurchaseButton(entry, 64, 15);

        // Navigation
        GuiButton.button(Material.GREEN_STAINED_GLASS_PANE)
            .setName("<- &e&lGo Back")
            .setLore("Click to return to previous screen.")
            .place(this, 10, e ->
            {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
                GuiHandler.changeMenu(p, new ObsShopGui(p));
            });

        GuiButton.button(Material.RED_STAINED_GLASS_PANE)
            .setName("&c&lExit Menu")
            .setLore("Click to exit.")
            .place(this, 16, e ->
            {
                Player p = (Player) e.getWhoClicked();
                p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
                GuiHandler.close(p);
            });

        paint();
    }

    /**
     * Places a purchase button for `multiplier` units of `entry`.
     *
     * The GUI icon is built from entry.createStack(1) so custom items
     * (health crystals, etc.) show their actual appearance and lore
     * rather than a plain vanilla icon.
     *
     * On purchase, entry.createStack() is used to give the item, which
     * preserves all PDC data, enchantments, and meta from the template.
     */
    private void placePurchaseButton(ShopEntry entry, int multiplier, int slot)
    {
        ShopCost     scaledCost = entry.getCost().scaled(multiplier);
        List<String> loreCost   = GuiButton.buildShopLore(entry.getCost(), multiplier);

        // Use the actual item template as the icon base so custom items
        // display correctly in the GUI. GuiButton.fromStack overrides
        // the name and appends cost lore on top of any existing item lore.
        ItemStack icon = entry.createStack(multiplier);

        GuiButton.fromStack(icon)
            .setName("&r&f" + entry.getDisplayName() +" &7("+entry.getAmount()+ " x " + multiplier + ")")
            .setLore(loreCost)
            .place(this, slot, e ->
            {
                Player p = (Player) e.getWhoClicked();
                e.setCancelled(true);

                if (!canAfford(p, scaledCost))
                {
                    p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
                    return;
                }

                if (scaledCost.getMoney() > 0)
                    PlayerData.subtractMoney(p, scaledCost.getMoney());

                if (scaledCost.getScrap() > 0)
                    PlayerData.subtractScrap(p, scaledCost.getScrap());

                for (var ess : scaledCost.getAllEssenceCosts().entrySet())
                    if (ess.getValue() > 0)
                        PlayerData.subtractEssence(p, ess.getKey(), ess.getValue());

                if (scaledCost.getLuminaTears() > 0)
                    PlayerData.subtractLuminite(p, scaledCost.getLuminaTears());

                // createStack() clones the template — PDC, enchants, and
                // meta are all preserved, regardless of what the item is.
                InventoryUtils.add(p, entry.createStack(entry.getAmount() * multiplier));
                p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1, 1);
            });
    }

    private boolean canAfford(Player p, ShopCost cost)
    {
        PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        if (data == null) return false;

        if (cost.getMoney()       > 0 && data.getFunds(false) < cost.getMoney())       return false;
        if (cost.getScrap()       > 0 && data.getScrap()      < cost.getScrap())       return false;
        if (cost.getLuminaTears() > 0 && data.getLuminite()   < cost.getLuminaTears()) return false;

        for (var ess : cost.getAllEssenceCosts().entrySet())
            if (ess.getValue() > 0 && data.getEssence(ess.getKey()) < ess.getValue())
                return false;

        return true;
    }
}