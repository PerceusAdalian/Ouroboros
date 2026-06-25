package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.enums.ObsColors;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class SpecialSpellBookPage extends ObsGui
{

	public SpecialSpellBookPage(Player player)
	{
		super(player, "Alternative Magic Spellbook", 27, Set.of());
	}

	@Override
	protected void build()
	{
		GuiButton.button(Material.END_CRYSTAL).setName(PrintUtils.color(ObsColors.ARCANO)+"&lArcano&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.ARCANO)+"&lArcano&r&e spells&f.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ArcanoSpellsPage(p));
		});
		
		GuiButton.button(Material.EMERALD).setName(PrintUtils.color(ObsColors.ARDENTIO)+"&lArdentio&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.ARDENTIO)+"&lArdentio&r&e spells&f.").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ArdentioSpellsPage(p)); 
		});

		GuiButton.button(Material.CLOCK).setName(PrintUtils.color(ObsColors.ASTRAL)+"&lAstral&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.ASTRAL)+"&lAstral&r&e spells&f.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AstralSpellsPage(p)); 
		});
		
		
		GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		paint();
	}

}
