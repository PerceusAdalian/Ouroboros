package com.eol.enums;

import com.ouroboros.utils.PrintUtils;

public enum MateriaComponent 
{
	CATALYST("catalyst", 'e'),
	BASE("base", '6'),
	BINDING("binding", 'd'),
	ELEMENT_CORE("element_core", '3');
	
	private final String materiaComponent;
	private final char color;
	
	MateriaComponent(String materiaComponent, char color)
	{
		this.materiaComponent = materiaComponent;
		this.color = color;
	}
	
	public String getType()
	{
		return materiaComponent;
	}
	
	public String getKey()
	{
		return this.name();
	}
	
	public char getColorCode()
	{
		return color;
	}

	public String getLabel()
	{
		return PrintUtils.ColorParser("&"+color+PrintUtils.formatEnumName(materiaComponent));
	}
}
