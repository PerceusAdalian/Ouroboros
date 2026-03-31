package com.ouroboros.enums;

public enum ObsColors
{
	// Element colors
    INFERNO("#FF6961"),
    GLACIO("#97C1E6"),
    AERO("#90E4C1"),
    GEO("#E6CCB2"),
    CELESTIO("#FDDC5C"),
    COSMO("#C9A0DC"),
    HERESIO("#4DB560"),
    MORTIO("#B36679"),
    ARCANO("#FFB5C0"),
    ASTRAL("#887CC2"),
    // Combat Colors
    COMBUST("#FF746C"),
    CORROSIVE("#9BB88F"),
    
    // UI / general
    DAMAGE("#FF0000"),
    HP("#005500"),
    ARMOR("#FFD700"),
    KEYWORD("#800080"),
    NULL("#8E8E8E")
	;
	
    private final String hex;

    ObsColors(String hex) 
    { 
    	this.hex = hex; 
    }

    public String getColor() 
    { 
    	return hex; 
    }
}
