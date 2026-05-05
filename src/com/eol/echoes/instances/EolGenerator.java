package com.eol.echoes.instances;

import org.bukkit.inventory.ItemStack;

import com.eol.echoes.instances.celestio.LuminusBroadsword;
import com.eol.enums.MateriaState;
import com.eol.materia.Materia;

public class EolGenerator
{
	public static ItemStack generateLuminusCatalyst()
	{
		Materia luminus = Materia.get("echo_of_luminus");
		ItemStack catalyst = luminus.getAsItemStack(MateriaState.CATALYST);
		EOLRegistry.markCatalyst(catalyst, new LuminusBroadsword().getInternalName());
		return catalyst;
	}
	
}
