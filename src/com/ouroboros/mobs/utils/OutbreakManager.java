package com.ouroboros.mobs.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.ouroboros.mobs.MobSummoner;
import com.ouroboros.mobs.Outbreak;
import com.ouroboros.mobs.Outbreak.OutbreakEntry;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class OutbreakManager
{
	public static void trigger(Location center, Outbreak outbreak)
    {
        for (OutbreakEntry entry : outbreak.entries)
        {
            Location spawnLoc = scatter(center, 8); // random offset within radius
            String name = entry.customName() != null
                ? entry.customName()
                : PrintUtils.getFancyEntityName(entry.type());

            MobSummoner.build(spawnLoc, entry.type(), entry.level(), name);
        }

        announceNearby(center, outbreak.outbreakName, 64);
    }

    private static Location scatter(Location center, int radius)
    {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        double ox = rng.nextDouble(-radius, radius);
        double oz = rng.nextDouble(-radius, radius);
        return center.clone().add(ox, 0, oz);
    }

    private static void announceNearby(Location center, String name, double radius)
    {
        if (!RayCastUtils.getNearbyPlayers(center, radius, c ->
        {
        	PrintUtils.Print((Player) c, "&c&lOutbreak&f: &e" + name + " &fhas begun nearby!");
        })) return;
    }
}
