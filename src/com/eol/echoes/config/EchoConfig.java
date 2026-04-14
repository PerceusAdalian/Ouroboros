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

import com.eol.enums.EchoMaterial;
import com.eol.enums.MateriaType;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;

/**
 * EchoConfig loads echo_config.yml from the plugin data folder and exposes
 * typed, cached accessors for every stage of the Echo generation pipeline.
 *
 * Call EchoConfig.load() once during plugin enable.
 * All pipeline classes (StatResolver, ModifierPipeline, RarityResolver) then
 * call EchoConfig.get() to retrieve the singleton and read from it.
 *
 * If echo_config.yml does not exist in the data folder it is copied from
 * the plugin jar's resources automatically.
 */
public class EchoConfig
{
    private static EchoConfig instance;

    private final Map<Integer, RarityBand>       	   rarityBands    = new HashMap<>();
    private final Map<EchoMaterial, MaterialStatRange> materialStats  = new HashMap<>();
    private final Map<String, BindingStatBlock>        bindingStats   = new HashMap<>();

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    private EchoConfig(YamlConfiguration config)
    {
        loadRarityBands(config);
        loadMaterialStats(config);
        loadBindingStats(config);
    }

    /**
     * Loads (or reloads) the config from disk. Safe to call on /reload.
     * Returns the loaded instance.
     */
    public static EchoConfig load()
    {
        File dataFolder = Ouroboros.instance.getDataFolder();
        File configFile = new File(dataFolder, "echo_config.yml");

        if (!configFile.exists()) copyDefault(configFile);

        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
        instance = new EchoConfig(yaml);
        
        return instance;
    }

    /**
     * Returns the cached singleton. Throws if load() has not been called.
     */
    public static EchoConfig get()
    {
        if (instance == null) throw new IllegalStateException("EchoConfig has not been loaded. Call EchoConfig.load() during plugin enable.");
        
        return instance;
    }

    // -------------------------------------------------------------------------
    // Public accessors
    // -------------------------------------------------------------------------

    /**
     * Returns the RarityBand for the given Rarity.
     * Keyed on Rarity.getRarity() (1-7). NONE (0) is not a valid forge rarity.
     * Throws if the rarity is not configured.
     */
    public RarityBand getRarityBand(Rarity rarity)
    {
        int tier = rarity.getRarity();
        RarityBand band = rarityBands.get(tier);
        if (band == null) throw new IllegalArgumentException("No rarity band configured for tier: " + tier + " (" + rarity.name() + ")");
        
        return band;
    }

    /**
     * Returns the MaterialStatRange for the given EchoMaterial.
     * Throws if the material is not configured.
     */
    public MaterialStatRange getMaterialStats(EchoMaterial material)
    {
        MaterialStatRange range = materialStats.get(material);
        if (range == null) throw new IllegalArgumentException("No material stats configured for: " + material.name());
        
        return range;
    }

    /**
     * Returns the BindingStatBlock for the given MateriaType binding.
     * Keyed on MateriaType.name() (e.g. STRING, PELT, LEATHER).
     * Throws if the binding type is not configured.
     */
    public BindingStatBlock getBindingStats(MateriaType bindingType)
    {
        BindingStatBlock block = bindingStats.get(bindingType.name().toUpperCase());
        if (block == null) throw new IllegalArgumentException("No binding stats configured for MateriaType: " + bindingType.name());
        
        return block;
    }

    // -------------------------------------------------------------------------
    // Loaders
    // -------------------------------------------------------------------------

    private void loadRarityBands(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("rarity_bands");
        if (section == null)
        {
            warn("rarity_bands section missing from echo_config.yml");
            return;
        }

        for (String key : section.getKeys(false))
        {
            int tier;
            try 
            { 
            	tier = Integer.parseInt(key); 
            }
            catch (NumberFormatException e) 
            { 
            	warn("Invalid rarity band key: " + key); continue; 
            }

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
        if (section == null)
        {
            warn("material_stats section missing from echo_config.yml");
            return;
        }

        for (String key : section.getKeys(false))
        {
            EchoMaterial material;
            try 
            { 
            	material = EchoMaterial.valueOf(key.toUpperCase()); 
            }
            catch (IllegalArgumentException e) 
            {
            	warn("Unknown EchoMaterial in config: " + key); continue; 
            }

            ConfigurationSection mat = section.getConfigurationSection(key);
            if (mat == null) continue;

            materialStats.put(material, new MaterialStatRange(
                mat.getDouble("base_attack_min", 3),
                mat.getDouble("base_attack_max", 6),
                mat.getDouble("crit_rate_base", 0.05),
                mat.getDouble("crit_modifier_ceiling", 1.5)));
        }
    }

    private void loadBindingStats(YamlConfiguration config)
    {
        ConfigurationSection section = config.getConfigurationSection("binding_stats");
        if (section == null)
        {
            warn("binding_stats section missing from echo_config.yml");
            return;
        }

        for (String key : section.getKeys(false))
        {
            ConfigurationSection binding = section.getConfigurationSection(key);
            if (binding == null) continue;

            bindingStats.put(key.toUpperCase(), new BindingStatBlock(
                binding.getDouble("attack_rating", 1.0),
                binding.getDouble("durability_multiplier", 1.0),
                binding.getDouble("crit_rate_bonus", 0.0),
                binding.getDouble("attack_bonus", 0)));
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void copyDefault(File target)
    {
        try (InputStream in = Ouroboros.instance.getResource("echo_config.yml"))
        {
            if (in == null)
            {
                warn("echo_config.yml not found in plugin jar resources.");
                return;
            }
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
