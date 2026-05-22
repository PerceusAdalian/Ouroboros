package com.eol.echoes.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.eol.echoes.records.ArmorStatBlock;
import com.eol.echoes.records.BindingStatBlock;
import com.eol.echoes.records.MaterialStatBlock;
import com.eol.echoes.records.RarityBand;
import com.eol.enums.EchoMaterial;
import com.eol.enums.MateriaType;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;

/**
 * EchoConfig loads echo_config.yml and exposes typed, cached accessors for
 * every stage of the Echo generation pipeline.
 *
 * Call EchoConfig.load() once during plugin enable.
 * All pipeline classes then call EchoConfig.get() to read from the singleton.
 *
 * echo_config.yml sections:
 *   rarity_bands       — modifier slot counts and magnitude ranges per rarity tier
 *   material_stats     — weapon/tool base stat ranges per EchoMaterial
 *   armor_stats        — armor base stat ranges per EchoMaterial
 *   binding_stats      — attack rating / durability / crit deltas per binding MateriaType
 */
public class EchoConfig
{
    private static EchoConfig instance;

    private final Map<Integer,      RarityBand>        rarityBands   = new HashMap<>();
    private final Map<EchoMaterial, MaterialStatBlock> materialStats = new HashMap<>();
    private final Map<EchoMaterial, ArmorStatBlock>    armorStats    = new HashMap<>();
    private final Map<String,       BindingStatBlock>  bindingStats  = new HashMap<>();

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    private EchoConfig(YamlConfiguration config)
    {
        loadRarityBands(config);
        loadMaterialStats(config);
        loadArmorStats(config);
        loadBindingStats(config);
    }

    public static EchoConfig load()
    {
        File dataFolder = Ouroboros.instance.getDataFolder();
        File configFile = new File(dataFolder, "echo_config.yml");

        if (!configFile.exists()) copyDefault(configFile);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
        instance = new EchoConfig(yaml);
        return instance;
    }

    public static EchoConfig get()
    {
        if (instance == null)
            throw new IllegalStateException("EchoConfig has not been loaded. Call EchoConfig.load() during plugin enable.");
        return instance;
    }

    // -------------------------------------------------------------------------
    // Public accessors
    // -------------------------------------------------------------------------

    /** Returns the RarityBand for the given Rarity tier. Throws if not configured. */
    public RarityBand getRarityBand(Rarity rarity)
    {
        int tier = rarity.getRarity();
        RarityBand band = rarityBands.get(tier);
        if (band == null)
            throw new IllegalArgumentException("No rarity band configured for tier: " + tier + " (" + rarity.name() + ")");
        return band;
    }

    /** Returns the weapon/tool MaterialStatRange for the given EchoMaterial. Throws if not configured. */
    public MaterialStatBlock getMaterialStats(EchoMaterial material)
    {
        MaterialStatBlock range = materialStats.get(material);
        if (range == null)
            throw new IllegalArgumentException("No weapon material stats configured for: " + material.name());
        return range;
    }

    /** Returns the armor ArmorStatRange for the given EchoMaterial. Throws if not configured. */
    public ArmorStatBlock getArmorMaterialStats(EchoMaterial material)
    {
        ArmorStatBlock range = armorStats.get(material);
        if (range == null)
            throw new IllegalArgumentException("No armor material stats configured for: " + material.name());
        return range;
    }

    /** Returns the BindingStatBlock for the given binding MateriaType. Throws if not configured. */
    public BindingStatBlock getBindingStats(MateriaType bindingType)
    {
        BindingStatBlock block = bindingStats.get(bindingType.name().toUpperCase());
        if (block == null)
            throw new IllegalArgumentException("No binding stats configured for MateriaType: " + bindingType.name());
        return block;
    }

    // -------------------------------------------------------------------------
    // Loaders
    // -------------------------------------------------------------------------

    private void loadRarityBands(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("rarity_bands");
        if (section == null) { warn("rarity_bands section missing from echo_config.yml"); return; }

        for (String key : section.getKeys(false))
        {
            int tier;
            try { tier = Integer.parseInt(key); }
            catch (NumberFormatException e) { warn("Invalid rarity band key: " + key); continue; }

            ConfigurationSection band = section.getConfigurationSection(key);
            if (band == null) continue;

            rarityBands.put(tier, new RarityBand(
                    band.getInt("modifier_slots", 1),
                    band.getDouble("active_modifier_chance", 0.5),
                    band.getDouble("magnitude_floor", 0.05),
                    band.getDouble("magnitude_ceiling", 0.30)));
        }
    }

    private void loadMaterialStats(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("material_stats");
        if (section == null) { warn("material_stats section missing from echo_config.yml"); return; }

        for (String key : section.getKeys(false))
        {
            EchoMaterial material;
            try { material = EchoMaterial.valueOf(key.toUpperCase()); }
            catch (IllegalArgumentException e) { warn("Unknown EchoMaterial in material_stats: " + key); continue; }

            ConfigurationSection mat = section.getConfigurationSection(key);
            if (mat == null) continue;

            materialStats.put(material, new MaterialStatBlock(
                    mat.getDouble("base_attack_min",       3.0),
                    mat.getDouble("base_attack_max",       6.0),
                    mat.getDouble("base_attack_rate_min",  1.6),
                    mat.getDouble("base_attack_rate_max",  4.0),
                    mat.getDouble("crit_rate_base",        0.05),
                    mat.getDouble("crit_modifier_ceiling", 1.5),
                    mat.getInt   ("durability_min",        100),
                    mat.getInt   ("durability_max",        300)));
        }
    }

    private void loadArmorStats(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("armor_stats");
        if (section == null) { warn("armor_stats section missing from echo_config.yml — armor Echoes will not generate correctly."); return; }

        for (String key : section.getKeys(false))
        {
            EchoMaterial material;
            try { material = EchoMaterial.valueOf(key.toUpperCase()); }
            catch (IllegalArgumentException e) { warn("Unknown EchoMaterial in armor_stats: " + key); continue; }

            ConfigurationSection mat = section.getConfigurationSection(key);
            if (mat == null) continue;

            armorStats.put(material, new ArmorStatBlock(
                    mat.getInt   ("armor_rating_min",        2),
                    mat.getInt   ("armor_rating_max",        8),
                    mat.getDouble("block_rate_min",          0.05),
                    mat.getDouble("block_rate_max",          0.30),
                    mat.getInt   ("crit_armor_rating_min",   1),
                    mat.getInt   ("crit_armor_rating_max",   4),
                    mat.getDouble("crit_block_rate_min",     0.02),
                    mat.getDouble("crit_block_rate_max",     0.15),
                    mat.getInt   ("durability_min",          150),
                    mat.getInt   ("durability_max",          500)));
        }
    }

    private void loadBindingStats(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("binding_stats");
        if (section == null) { warn("binding_stats section missing from echo_config.yml"); return; }

        for (String key : section.getKeys(false))
        {
            ConfigurationSection binding = section.getConfigurationSection(key);
            if (binding == null) continue;

            bindingStats.put(key.toUpperCase(), new BindingStatBlock(
                    binding.getDouble("attack_rating_min",     1.0),
                    binding.getDouble("attack_rating_max",     4.0),
                    binding.getDouble("durability_multiplier", 1.0),
                    binding.getDouble("crit_rate_bonus",       0.0),
                    binding.getDouble("attack_bonus",          0.0)));
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void copyDefault(File target)
    {
        try (InputStream in = Ouroboros.instance.getResource("echo_config.yml"))
        {
            if (in == null) { warn("echo_config.yml not found in plugin jar resources."); return; }
            target.getParentFile().mkdirs();
            Files.copy(in, target.toPath());
        }
        catch (IOException e)
        {
            Ouroboros.instance.getLogger().log(Level.SEVERE, "Failed to copy default echo_config.yml", e);
        }
    }

    private static void warn(String msg)
    {
        Ouroboros.instance.getLogger().warning("[EchoConfig] " + msg);
    }
}