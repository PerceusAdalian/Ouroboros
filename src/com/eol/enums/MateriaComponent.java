package com.eol.enums;

public enum MateriaComponent 
{
	CATALYST("catalyst"),
	BASE("base"),
	BINDING("binding"),
	ELEMENT_CORE("element_core");
	
	private final String materiaComponent;
	
	MateriaComponent(String materiaComponent)
	{
		this.materiaComponent = materiaComponent;
	}
	
	public String getMateriaComponentType()
	{
		return materiaComponent;
	}
	
	public String getKey()
	{
		return this.name();
	}
	
	public String getAsFancyName()
	{
		char color = switch(this)
		{
			case CATALYST -> color = 'e';
			case BASE -> color = '6';
			case BINDING -> color = 'd';
			case ELEMENT_CORE -> color = '3';
		};
		
		String name = getKey().toString().toLowerCase();
		String[] splitName = name.split("_");
		name = "";
		for (String s : splitName)
		{
			char[] chars = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			name += new String(chars);
			name += " ";
		}
		name = name.substring(0, name.length() - 1);
		return ("&"+color+name);
	}
}
