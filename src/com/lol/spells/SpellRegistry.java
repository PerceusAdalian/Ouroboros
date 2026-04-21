package com.lol.spells;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lol.spells.instances.Spell;
import com.lol.spells.instances.admin.RemoveEntity;
import com.lol.spells.instances.aero.ChainLightning;
import com.lol.spells.instances.aero.Charge;
import com.lol.spells.instances.aero.Diffindo;
import com.lol.spells.instances.aero.Fly;
import com.lol.spells.instances.aero.Galeforce;
import com.lol.spells.instances.aero.GalvanicNeedle;
import com.lol.spells.instances.aero.Gust;
import com.lol.spells.instances.aero.HealingCurrent;
import com.lol.spells.instances.aero.Levioso;
import com.lol.spells.instances.aero.Smite;
import com.lol.spells.instances.aero.Tailwind;
import com.lol.spells.instances.aero.Thunderbolt;
import com.lol.spells.instances.aero.Thunderstorm;
import com.lol.spells.instances.aero.Vaporize;
import com.lol.spells.instances.arcano.ArcaneBolt;
import com.lol.spells.instances.arcano.Freecast;
import com.lol.spells.instances.arcano.Mute;
import com.lol.spells.instances.arcano.OuroborosPrime;
import com.lol.spells.instances.arcano.PrismaOuroborealis;
import com.lol.spells.instances.arcano.Surveil;
import com.lol.spells.instances.astral.Starfall;
import com.lol.spells.instances.celestio.Ascension;
import com.lol.spells.instances.celestio.AssertOrder;
import com.lol.spells.instances.celestio.Cure;
import com.lol.spells.instances.celestio.Diagnosis;
import com.lol.spells.instances.celestio.Expell;
import com.lol.spells.instances.celestio.Heal;
import com.lol.spells.instances.celestio.Lumina;
import com.lol.spells.instances.celestio.Lumos;
import com.lol.spells.instances.celestio.MinorBlessing;
import com.lol.spells.instances.celestio.Pneuma;
import com.lol.spells.instances.celestio.Protego;
import com.lol.spells.instances.celestio.Revelio;
import com.lol.spells.instances.celestio.Satiate;
import com.lol.spells.instances.celestio.SolarExpanse;
import com.lol.spells.instances.cosmo.Antimatter;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.spells.instances.cosmo.ElementalConfinement;
import com.lol.spells.instances.cosmo.Gate;
import com.lol.spells.instances.cosmo.Nullify;
import com.lol.spells.instances.cosmo.Reconfigure;
import com.lol.spells.instances.cosmo.Teleport;
import com.lol.spells.instances.cosmo.VoidForm;
import com.lol.spells.instances.cosmo.Warp;
import com.lol.spells.instances.geo.Expelliarmus;
import com.lol.spells.instances.geo.Geomorph;
import com.lol.spells.instances.geo.SandBlast;
import com.lol.spells.instances.glacio.Chill;
import com.lol.spells.instances.glacio.Freeze;
import com.lol.spells.instances.glacio.GlacialFlood;
import com.lol.spells.instances.glacio.Glacius;
import com.lol.spells.instances.glacio.IcyWind;
import com.lol.spells.instances.glacio.Riptide;
import com.lol.spells.instances.glacio.Scald;
import com.lol.spells.instances.heresio.AvadaKedavra;
import com.lol.spells.instances.heresio.Axiom;
import com.lol.spells.instances.heresio.Beguile;
import com.lol.spells.instances.heresio.Corollary;
import com.lol.spells.instances.heresio.Hex;
import com.lol.spells.instances.heresio.Hypothesis;
import com.lol.spells.instances.heresio.Lemma;
import com.lol.spells.instances.heresio.Mania;
import com.lol.spells.instances.heresio.Penance;
import com.lol.spells.instances.heresio.Postulate;
import com.lol.spells.instances.heresio.Theorem;
import com.lol.spells.instances.inferno.AspectOfAighil;
import com.lol.spells.instances.inferno.Bombarda;
import com.lol.spells.instances.inferno.Combustion;
import com.lol.spells.instances.inferno.Ignite;
import com.lol.spells.instances.inferno.Incendio;
import com.lol.spells.instances.inferno.Meteor;
import com.lol.spells.instances.mortio.AspectOfSithis;
import com.lol.spells.instances.mortio.Demonform;
import com.lol.spells.instances.mortio.Haunt;
import com.lol.spells.instances.mortio.Reap;
import com.lol.spells.instances.mortio.Sew;
import com.lol.spells.instances.mortio.Shroud;
import com.lol.spells.instances.mortio.Siphon;
import com.lol.spells.instances.mortio.Voodoo;

public class SpellRegistry 
{
	public static final Map<String, Spell> spellRegistry = new HashMap<>();
    
	public static void init() 
    {
        List<Class<? extends Spell>> itemClasses = Arrays.asList(
        		
        		//Celestio
        		Revelio.class,
        		Protego.class,
        		AssertOrder.class,
        		Lumos.class,
        		Diagnosis.class,
        		Cure.class,
        		Expell.class,
        		Pneuma.class,
        		Ascension.class,
        		SolarExpanse.class,
        		Satiate.class,
        		Lumina.class,
        		MinorBlessing.class,
        		Heal.class,
        		
        		//Mortio
        		Haunt.class,
        		Sew.class,
        		Reap.class,
        		AspectOfSithis.class,
        		Siphon.class,
        		Demonform.class,
        		Shroud.class,
        		Voodoo.class,
        		
        		//Inferno
        		Incendio.class,
        		Bombarda.class,
        		Ignite.class,
        		Combustion.class,
        		Meteor.class,
        		AspectOfAighil.class,
        		
        		//Glacio
        		Glacius.class,
        		GlacialFlood.class,
        		Riptide.class,
        		Freeze.class,
        		IcyWind.class,
        		Scald.class,
        		Chill.class,
        		
        		//Aero
        		Diffindo.class,
        		Levioso.class,
        		Smite.class,
        		Thunderbolt.class,
        		Charge.class,
        		Thunderstorm.class,
        		GalvanicNeedle.class,
        		Gust.class,
        		Galeforce.class,
        		Tailwind.class,
        		ChainLightning.class,
        		Vaporize.class,
        		HealingCurrent.class,
        		Fly.class,
        		
        		//Geo
        		Expelliarmus.class,
        		SandBlast.class,
        		Geomorph.class,
        		
        		//Cosmo
        		ArrestoMomentum.class,
        		Gate.class,
        		Nullify.class,
        		ElementalConfinement.class,
        		Reconfigure.class,
        		Antimatter.class,
        		VoidForm.class,
        		Teleport.class,
        		Warp.class,
        		
        		//Heresio
        		Hex.class,
        		AvadaKedavra.class,
        		Mania.class,
        		Penance.class,
        		Beguile.class,
        		Axiom.class,
        		Postulate.class,
        		Hypothesis.class,
        		Lemma.class,
        		Theorem.class,
        		Corollary.class,
        		
        		//Arcano
        		Mute.class,
        		ArcaneBolt.class,
        		Surveil.class,
        		Freecast.class,
        		PrismaOuroborealis.class,
        		OuroborosPrime.class,
        		
        		//Astral
        		Starfall.class,
        		
        		//Admin Spells
        		RemoveEntity.class
        		
            );
        
        for (Class<? extends Spell> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends Spell> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Spell instance = constructor.newInstance();
                spellRegistry.put(instance.getInternalName(), instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
