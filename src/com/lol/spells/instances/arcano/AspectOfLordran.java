package com.lol.spells.instances.arcano;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

public class AspectOfLordran extends Spell
{

	public AspectOfLordran() 
	{
		super("Aspect of Lordran, Forgotten King", "aspect_of_lordran", Material.GLOBE_BANNER_PATTERN, 
				SpellType.ULTIMATE, SpellementType.ARCANO, CastConditions.MIXED, Rarity.SIX, 50, 0.1, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Aspect of Lordan&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oNobility Incarnate&r&f --",
				"&r&7&l┏--&r&7{&e✧ &oArbanian Combo&f: unleash a volley of "+PrintUtils.color(ObsColors.ARCANO)+"&lArcane&r&f spells.",
				"&r&7&l┗┳- &r&f1.   "+PrintUtils.color(ObsColors.ARCANO)+"Honored Coronation &f(&6⌖&f: 10&c♥&f, 40m)",
				"&r&7&l ┗┳- &r&f2.  "+PrintUtils.color(ObsColors.ARCANO)+"Regality Exhalted  &f(&6⌖&f: 25&c♥&f, 40m)",
				"&r&7&l  ┗┳- &r&f3. "+PrintUtils.color(ObsColors.ARCANO)+"Authority's Will   &f(&6⌖&f: 45&c♥&f, 40m)",
				"&r&7&l   ┗--&r&7{&r&e✧ &f4. "+PrintUtils.color(ObsColors.ARCANO)+"King's Decree &f(&6⌖&f: 50&c♥&f, 40m, &b&oEther Overload&r&f)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Aspect of Lordran&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Edict&r&f --",
				"&r&f&lPH",
				"",
				"&r&bEchoic Dissonance&r&f: ",
				"&r&fCasting "+PrintUtils.color(ObsColors.ARCANO)+"King's Degree&r&f: &lCD &r&e->&r&f &b10s&f, &f&lMC &r&e->&r&b 200",
				"&r&e&oSecondary Cast&r&f: &lCD &r&e->&r&f &b15s&r&f, &f&lMC &r&e-> &b300&f. Requires an "+PrintUtils.color(ObsColors.ARCANO)+"&lArcano &r&fattuned&r&f wand.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalManaCost() {
		// TODO Auto-generated method stub
		return 0;
	}

}
