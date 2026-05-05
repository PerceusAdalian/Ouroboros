package com.eol.enums;

import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Raider;

import com.ouroboros.enums.EntityCategory;
import com.ouroboros.utils.BiomeUtils;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.Nullable;

public enum WeaponModifierCondition
{
	//generic conditions
	PVE,
	PVP,
	PASSIVE,
	
	//world restrictions
	OVERWORLD,
	DURING_DAY,
	DURING_NIGHT,
	CLEAR_WEATHER,
	RAINY_WEATHER,
	STORMY_WEATHER,
	NETHER,
	END,

	//other restrictions
	UNDEAD,
	LIVING,
	FLYING,
	GLACIAL,
	INFERNAL,
	GROUNDED,
	COSMIC,
	OCCULTIC,
	BUGS,
	ELEMENTAL,
	RAID,
	COLDBIOMES,
	HOTBIOMES,
	GENERICBIOMES;
	
	public boolean satisfies(Player player, @Nullable LivingEntity target, World world)
	{
		return switch (this)
	    {
	        // Generic
	        case PVE     -> target != null && !(target instanceof Player);
	        case PVP     -> target instanceof Player;
	        case PASSIVE -> target == null;

	        // World
	        case OVERWORLD    -> world.getEnvironment() == World.Environment.NORMAL;
	        case NETHER       -> world.getEnvironment() == World.Environment.NETHER;
	        case END          -> world.getEnvironment() == World.Environment.THE_END;
	        case DURING_DAY   -> world.getTime() < 12300 || world.getTime() > 23850;
	        case DURING_NIGHT -> world.getTime() >= 12300 && world.getTime() <= 23850;

	        // Weather
	        case CLEAR_WEATHER -> !world.hasStorm() && !world.isThundering();
	        case RAINY_WEATHER -> world.hasStorm() && !world.isThundering();
	        case STORMY_WEATHER -> world.isThundering();

	        // Entity type restrictions
	        case UNDEAD    -> target != null && EntityCategories.canAccept(target, EntityCategory.MORTIO_MOBS);
	        case LIVING    -> target != null && !(target instanceof Player) && EntityCategories.canAccept(target, EntityCategory.CELESTIO_MOBS);
	        case FLYING    -> target != null && (EntityCategories.canAccept(target, EntityCategory.AERO_MOBS) || player.isFlying());
	        case GLACIAL   -> target != null && EntityCategories.canAccept(target, EntityCategory.GLACIO_MOBS);
	        case BUGS      -> target != null && EntityCategories.canAccept(target, EntityCategory.BUGS);
	        case ELEMENTAL -> target != null && EntityCategories.canAccept(target, EntityCategory.ELEMENTAL);
	        case INFERNAL  -> target != null && EntityCategories.canAccept(target, EntityCategory.INFERNO_MOBS);
	        case GROUNDED  -> target != null && (EntityCategories.canAccept(target, EntityCategory.GEO_MOBS) || !player.isFlying());
	        case COSMIC    -> target != null && EntityCategories.canAccept(target, EntityCategory.COSMO_MOBS);
	        case OCCULTIC  -> target != null && EntityCategories.canAccept(target, EntityCategory.HERESIO_MOBS);
	        case RAID      -> target instanceof Raider;
	        
	        // Biome restrictions
	        case COLDBIOMES    -> BiomeUtils.getTempCategory(player) == BiomeUtils.BiomeTemperatureCategory.COLD;
	        case HOTBIOMES     -> BiomeUtils.getTempCategory(player) == BiomeUtils.BiomeTemperatureCategory.HOT;
	        case GENERICBIOMES -> player.getLocation().getBlock().getBiome() != null;
	    };
	}
	
}
