package com.eol.enums;

public enum MateriaType 
{
	WOOD("wooden"),
	IRON("iron"),
	STONE("stone"),
	COPPER("copper"),
	GOLD("golden"),
	DIAMOND("diamond"),
	NETHERITE("netherite"),
	
	STRING("string"),
	LEATHER("leather"),
	
	CATALYST("catalyst"),
	
	CELESTIO("celestio"),
	MORTIO("mortio"),
	INFERNO("inferno"),
	GLACIO("glacio"),
	GEO("geo"),
	AERO("aero"),
	COSMO("cosmo"),
	HERESIO("heresio"),
	;
	
	private final String materiaCategory;
	
	MateriaType(String materiaCategory)
	{
		this.materiaCategory = materiaCategory;
	}
	
	public String getMateriaComponentType()
	{
		return materiaCategory;
	}
	
	public String getKey()
	{
		return this.name();
	}
	
	public String getAsFancyName()
	{
		
		
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
		return name;
	}
}
