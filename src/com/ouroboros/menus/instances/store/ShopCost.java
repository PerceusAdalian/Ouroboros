package com.ouroboros.menus.instances.store;

import com.ouroboros.enums.ElementType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Immutable multi-currency price tag for a shop entry.
 * Build via the fluent Builder:
 *
 *   ShopCost cost = ShopCost.builder().money(10).essence(ElementType.FIRE, 5).build();
 *
 * Only currencies with a value > 0 are considered "required" and will
 * appear in lore or be deducted on purchase.
 */
public final class ShopCost
{
    private final int money;
    private final int scrap;
    private final int luminaTears;
    private final Map<ElementType, Integer> essence;

    private ShopCost(Builder b)
    {
        this.money       = b.money;
        this.scrap       = b.scrap;
        this.luminaTears = b.luminaTears;
        this.essence     = Map.copyOf(b.essence);
    }

    public int getMoney()        
    { 
    	return money; 
    }
    
    public int getScrap()        
    { 
    	return scrap; 
    }
    
    public int getLuminaTears()
    { 
    	return luminaTears; 
    }

    public int getEssence(ElementType type)
    {
        return essence.getOrDefault(type, 0);
    }

    public Map<ElementType, Integer> getAllEssenceCosts()
    {
        return essence;
    }

    /** True if every field is zero — useful for validation. */
    public boolean isEmpty()
    {
        return money == 0 && scrap == 0 && luminaTears == 0 && essence.isEmpty();
    }

    /**
     * Returns a scaled copy of this cost (e.g. for buying 16× or 32×).
     * All values are multiplied by the given factor.
     */
    public ShopCost scaled(int factor)
    {
        Builder b = new Builder()
            .money(money * factor)
            .scrap(scrap * factor)
            .luminaTears(luminaTears * factor);
        for (Map.Entry<ElementType, Integer> e : essence.entrySet())
            b.essence(e.getKey(), e.getValue() * factor);
        return b.build();
    }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder
    {
        private int money       = 0;
        private int scrap       = 0;
        private int luminaTears = 0;
        private final Map<ElementType, Integer> essence = new EnumMap<>(ElementType.class);

        public Builder money(int amount)
        {
        	this.money = amount;
        	return this;
        }
        
        public Builder scrap(int amount)
        { 
        	this.scrap = amount;
        	return this; 
        }
        
        public Builder luminaTears(int amount)
        { 
        	this.luminaTears = amount; 
        	return this; 
        }
        
        public Builder essence(ElementType type, int amount)
        { 
        	essence.put(type, amount); 
        	return this; 
        }

        public ShopCost build() 
        { 
        	return new ShopCost(this); 
        }
    }
}
