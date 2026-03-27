package com.ouroboros.mobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.mobs.utils.LevelTable;
import com.ouroboros.mobs.utils.OutbreakManager;


public class Outbreak
{
	
	public record OutbreakEntry(EntityType type, int level, String customName) {}
	public final List<OutbreakEntry> entries;
    public final String outbreakName; // e.g. "Inferno Surge", displayed to players nearby

    public Outbreak(String outbreakName, List<OutbreakEntry> entries)
    {
        this.outbreakName = outbreakName;
        this.entries = entries;
    }

    // Factory for random outbreaks — pulls element and level range from context
    public static Outbreak random(Biome biome, ElementType element, int groupSize)
    {
        Set<EntityType> pool = EntityCategory.parseByElementType(element); // filter EntityCategories by element
        List<OutbreakEntry> entries = new ArrayList<>();
        for (int i = 0; i < groupSize; i++)
        {
            EntityType type = pool.stream()
                .skip(ThreadLocalRandom.current().nextInt(pool.size()))
                .findFirst().orElseThrow();
            int level = LevelTable.getLevel(biome);
            entries.add(new OutbreakEntry(type, level, null)); // null = no custom name, uses default
        }
        return new Outbreak("Swarm", entries); // unknown name until revealed
    }
    
    public static void registerRandomOutbreakTask(Plugin plugin)
    {
        Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                if (ThreadLocalRandom.current().nextDouble() > 0.02) continue;

                Location center = pickOutbreakCenter(p, 32, 64);
                if (center == null) continue;

                Biome biome = center.getBlock().getBiome();
                ElementType element = ElementType.random();
                Outbreak outbreak = Outbreak.random(biome, element, ThreadLocalRandom.current().nextInt(3, 7));
                OutbreakManager.trigger(center, outbreak);
            }
        }, 0L, 200L);
    }

    private static Location pickOutbreakCenter(Player p, int minDist, int maxDist)
    {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        Location playerLoc = p.getLocation();

        for (int attempts = 0; attempts < 10; attempts++)
        {
            double angle = rng.nextDouble(0, Math.PI * 2);
            double dist  = rng.nextDouble(minDist, maxDist);
            double ox    = Math.cos(angle) * dist;
            double oz    = Math.sin(angle) * dist;

            Location candidate = playerLoc.clone().add(ox, 0, oz);
            candidate.setY(candidate.getWorld().getHighestBlockYAt(candidate));

            // Reject if chunk isn't loaded; don't force-load chunks for a random outbreak
            if (!candidate.getChunk().isLoaded()) continue;

            return candidate;
        }

        return null; // no valid location found after attempts, skip this cycle
    }
    
   
    
    
}
