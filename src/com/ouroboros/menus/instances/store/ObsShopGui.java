package com.ouroboros.menus.instances.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.objects.instances.HealthCrystal;
import com.ouroboros.utils.Symbols;

public class ObsShopGui extends ObsGui
{

    public ObsShopGui(Player player)
    {
        super(player, Symbols.OBS + "uroboros Shop", 54, Set.of());
    }

    public static final Map<UUID, Material> confirmBuyer = new HashMap<>();

    @Override
    protected void build()
    {
        GuiButton.placeShopButton(player, ShopEntry.of(Material.DIRT).cost(ShopCost.builder().money(5).build()).build(), 10, this);
        GuiButton.placeShopButton(player, ShopEntry.of(new HealthCrystal().toItemStack()).cost(ShopCost.builder().money(150).build()).build(), 11, this);
        
        GuiButton.placeShopButton(player, ShopEntry.of(Material.LADDER).displayName("Bundle of Ladders").amount(4).cost(ShopCost.builder().money(5).build()).build(), 12, this);
        
        GuiButton.placeGoBack(37, this, new ObsMainMenu(player));
        GuiButton.placeExit(43, this);

        paint();
    }
}
