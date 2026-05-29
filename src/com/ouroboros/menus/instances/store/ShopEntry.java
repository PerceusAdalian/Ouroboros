package com.ouroboros.menus.instances.store;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ouroboros.utils.PrintUtils;

public final class ShopEntry
{
    private final ItemStack itemTemplate;
    private final String    displayName;
    private final int       amount;
    private final ShopCost  cost;

    private ShopEntry(Builder b)
    {
        if (b.stack != null)
            this.itemTemplate = b.stack.clone();
        else
            this.itemTemplate = new ItemStack(b.material, 1);

        this.displayName = b.displayName;
        this.amount      = b.amount;
        this.cost        = b.cost;
    }

    public Material getMaterial()
    {
        return itemTemplate.getType();
    }

    public ItemStack createStack(int count)
    {
        ItemStack copy = itemTemplate.clone();
        copy.setAmount(count);
        return copy;
    }

    public int getAmount() 
    { 
    	return amount; 
    }

    public ShopCost getCost() 
    { 
    	return cost; 
    }

    public String getDisplayName()
    {
        if (displayName != null) return displayName;

        ItemMeta meta = itemTemplate.getItemMeta();
        if (meta != null && meta.hasDisplayName())
            return ChatColor.stripColor(meta.getDisplayName());

        return PrintUtils.formatEnumName(itemTemplate.getType().name());
    }

        public static Builder of(Material material)
    {
        return new Builder(material);
    }

    public static Builder of(ItemStack stack)
    {
        return new Builder(stack.clone());
    }

    public static final class Builder
    {
        private final Material  material;
        private final ItemStack stack;

        private String   displayName = null;
        private int      amount      = 1;
        private ShopCost cost        = ShopCost.builder().build();

        private Builder(Material material)
        {
            this.material = material;
            this.stack    = null;
        }

        private Builder(ItemStack stack)
        {
            this.material = null;
            this.stack    = stack;
        }

        public Builder displayName(String name)
        {
            this.displayName = name;
            return this;
        }

        public Builder amount(int amount)
        {
            this.amount = amount;
            return this;
        }

        public Builder cost(ShopCost cost)
        {
            this.cost = cost;
            return this;
        }

        public ShopEntry build()
        {
            return new ShopEntry(this);
        }
    }
}