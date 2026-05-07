package com.ouroboros.objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.instances.MoneyObject;

public class MoneyHandler
{
    public static final NamespacedKey moneyKey = new NamespacedKey(Ouroboros.instance, "money_object");

    private final ItemStack stack;
    private final int amount;

    private MoneyHandler(ItemStack stack, int amount)
    {
        this.stack = stack;
        this.amount = amount;
    }

    public static MoneyHandler of(int value)
    {
        ItemStack stack = new MoneyObject(value).toItemStack();
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return null;

        meta.getPersistentDataContainer().set(moneyKey, PersistentDataType.INTEGER, value);
        stack.setItemMeta(meta);

        return new MoneyHandler(stack, value);
    }

    public static int readValue(ItemStack stack)
    {
        if (stack == null) return 0;
        ItemMeta meta = stack.getItemMeta();
        
        if (meta == null) return 0;
        
        Integer val = meta.getPersistentDataContainer().get(moneyKey, PersistentDataType.INTEGER);
        return val != null ? val : 0;
    }

    public int getAmount() 
    {
    	return amount; 
    }
    
    public ItemStack build() 
    {
    	return stack;
    }
}