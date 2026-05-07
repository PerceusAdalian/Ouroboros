package com.ouroboros.mobs.events;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.echoes.EchoManager;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.lol.spells.SpellRegistry;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.arcano.PrismaOuroborealis;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.accounts.PlayerHud;
import com.ouroboros.accounts.XpUtils;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.enums.Rarity;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.MoneyHandler;
import com.ouroboros.objects.instances.AeroEssence;
import com.ouroboros.objects.instances.ArcanoEssence;
import com.ouroboros.objects.instances.CelestioEssence;
import com.ouroboros.objects.instances.CosmoEssence;
import com.ouroboros.objects.instances.GeoEssence;
import com.ouroboros.objects.instances.GlacioEssence;
import com.ouroboros.objects.instances.HeresioEssence;
import com.ouroboros.objects.instances.InfernoEssence;
import com.ouroboros.objects.instances.MortioEssence;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.EntityCategoryToSpellement;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class MobDeathEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler
			public void onDeath(EntityDeathEvent e) 
			{
				Map<Spell, Boolean> spellDropsRegistry = new HashMap<>();
				Map<Spell, Boolean> spellShardDropsRegistry = new HashMap<>();
				
			    LivingEntity le = e.getEntity();
			    if (!le.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY)) return;
			    le.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1);
			    MobData data = MobData.getMob(le.getUniqueId());
			    if (data == null) return;
			    
			    EntityCategory mobCategory = EntityCategoryToSpellement.getMobCategory(e.getEntityType());
			    
			    final int maxSpellDrops = 1;
			    final int maxSpellShardDrops = 1;
			    final int maxEssenceDrops = 4;
			    final int catalystDrops = 1;
			    int level = data.getLevel();
			    int chanceBonus = 0;
			    int currentSpellDrops = 0;
			    int currentSpellShardDrops = 0;
			    int currentCatalystDrops = 0;
			    boolean overrideDrops = false;
			    
			    if (e.getEntity().getKiller() instanceof Player p)
			    {
			    	if (PrismaOuroborealis.arcane_prisma_registry.contains(p.getUniqueId())) chanceBonus += 20;
			    	PlayerData pData = PlayerData.getPlayer(p.getUniqueId());
			    	
			    	ItemStack held = p.getInventory().getItemInMainHand();
			        StatType sType = null;
			        if (EchoManager.isEcho(held))
			        {
			            EchoManifest codec = EchoManager.getCodec(held);
			            if (codec != null) 
			            	sType = (codec.echoForm() == EchoForm.BOW || codec.echoForm() == EchoForm.CROSSBOW)
			                	? StatType.RANGED : StatType.MELEE;
			        }
			        else if (held.getType().isAir()) 
			        	sType = StatType.MELEE;

			        int xp = XpUtils.getXp(le);
			        if (sType != null) PlayerData.addXP(p, sType, xp);
			    	
			    	if (pData.doAutoCollectLootDrops())
			    	{
			    		overrideDrops = true;
			    		int tears = 0;
			    		if (Chance.of(Math.min(75 + chanceBonus, 100))) tears++;
			    		int moneyAmount = level * 10;
			    		int essenceAmount = 0;
			    		for (int i = 0; i <= maxEssenceDrops; i++) 
			        	{
			        		if (!Chance.of(Math.min(30 + chanceBonus, 100))) continue;
			        		essenceAmount++;
			        	}
			    		
			    		PlayerData.addLuminite(p, tears);
			    		PlayerData.addMoney(p, moneyAmount);
			    		ElementType element = ElementType.getFromEntityCategory(mobCategory);
			    		if (element != null) PlayerData.addEssence(p, element, essenceAmount);
			    		PlayerHud.update(p);
			    		
			    		if (pData.doLevelUpSound())
			                EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
			            PrintUtils.PrintToActionBar(p, 
			    				(pData.doXpNotification()?"&r&e+&r&f&l" + xp + " &r&b" + PrintUtils.printStatType(sType)+"&r&7&l | ":"")
			    				+("&r&e+&f&l"+moneyAmount+"&e"+Symbols.MONEY)
			    				+(tears > 0 ? " &r&e+&f&l" + tears + "&r&b" + Symbols.LUMINITE : "")
			    				+(essenceAmount > 0 ? " &r&e+&f&l" + essenceAmount + "&r" + PrintUtils.getElementTypeColor(element) + Symbols.ESSENCE : ""));
			    	}
			    	else 
			    	{
			    		if (pData.doLevelUpSound())
			                EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
			        	PrintUtils.PrintToActionBar(p, "&r&e+&r&f&l" + xp + " &r&b" + PrintUtils.printStatType(sType));
			    	}
			    	
			    }
			    
			    // Tear Drop
			    if (overrideDrops == false && Chance.of(Math.min(75 + chanceBonus, 100)))
			    		e.getDrops().add(new TearOfLumina().toItemStack());
			    
			    
			    // Money Drop
			    if (overrideDrops == false)
			    	e.getDrops().add(MoneyHandler.of(level*10).build());
			    
			    // Spell, Shard, & Essence drops
			    if (mobCategory != null)
			    {
			        for (Spell spell : SpellRegistry.spellRegistry.values())
			        {
			        	if (currentSpellDrops >= maxSpellDrops) break;
			        	if (!EntityCategoryToSpellement.isElementMatch(spell.getElementType(), mobCategory)) continue;
			        	if (!Chance.of(Math.min(1.5 + chanceBonus, 100))) continue;
			            if (spell.getRarity().getRarity() > Rarity.getRarityForMobLevel(level)) continue;
			            if (spellDropsRegistry.getOrDefault(spell, false)) continue;
			            
			            e.getDrops().add(spell.getAsItemStack(Spell.SpellGenerateCondition.BOOK));
			            spellDropsRegistry.put(spell, true);
			            currentSpellDrops++;
			        }
			        
			        for (Spell shard : SpellRegistry.spellRegistry.values())
			        {
			        	if (currentSpellShardDrops >= maxSpellShardDrops) break;
			        	if (!EntityCategoryToSpellement.isElementMatch(shard.getElementType(), mobCategory)) continue;
			        	if (!Chance.of(Math.min(9.5 + chanceBonus, 100))) continue;
			            if (shard.getRarity().getRarity() > Rarity.getRarityForMobLevel(level)) continue;
			            if (spellShardDropsRegistry.getOrDefault(shard, false)) continue;
			           
			            e.getDrops().add(shard.getAsItemStack(Spell.SpellGenerateCondition.SHARD));
			            spellShardDropsRegistry.put(shard, true);
			            currentSpellShardDrops++;
			        }
			        
			        if (overrideDrops == false)
			        {			        	
			        	for (int i = 0; i <= maxEssenceDrops; i++) 
			        	{
			        		if (!Chance.of(Math.min(30 + chanceBonus, 100))) continue;
			        		AbstractObsObject essence = switch (mobCategory) 
	        				{
	        				case CELESTIO_MOBS -> new CelestioEssence();
	        				case MORTIO_MOBS   -> new MortioEssence();
	        				case INFERNO_MOBS  -> new InfernoEssence();
	        				case GLACIO_MOBS   -> new GlacioEssence();
	        				case AERO_MOBS     -> new AeroEssence();
	        				case GEO_MOBS      -> new GeoEssence();
	        				case COSMO_MOBS    -> new CosmoEssence();
	        				case HERESIO_MOBS  -> new HeresioEssence();
	        				case ARCANO_MOBS   -> new ArcanoEssence();
	        				default            -> null;
	        				};
	        				if (essence != null) e.getDrops().add(essence.toItemStack());
			        	}
			        }
			    }
			    
			    // Catalyst Drops
			    if (Chance.of(Math.min(5 + chanceBonus, 100)))
			    {
			    	for (Materia materia : Materia.materia_registry.values().stream().filter(m -> m.getMateriaType() == MateriaType.CATALYST).collect(Collectors.toList()))
			    	{
			    		if (materia.getRarity().getRarity() > Rarity.getRarityForMobLevel(level)) continue;
			    		if (currentCatalystDrops >= catalystDrops) break;
			    		
			    		ItemStack catalystStack = materia.getAsItemStack(MateriaState.CATALYST);
			    		if (catalystStack != null) e.getDrops().add(catalystStack);
			    		currentCatalystDrops++;
			    	}
			    }
			    
			    // Clear recently dropped maps after 30 seconds
			    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
			    {
			    	spellDropsRegistry.clear();	
			    	spellShardDropsRegistry.clear();
			    }, 600L);
			    
			    if (Ouroboros.debug) 
			    {
			        String mobName = le.getCustomName() != null ? le.getCustomName() : le.getType().name();
			        PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 Mob: " + mobName + " || &c&oDied Successfully");
			    }
			    
			    if(data != null) data.deleteFile();
			}
		},plugin);
	}
}
