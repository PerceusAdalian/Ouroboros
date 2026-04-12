package com.ouroboros.utils;

import org.bukkit.Location;

public class WarpData
{
    private final int index;
    private Location location;
    private String nickname;

    public WarpData(int index, Location location, String nickname)
    {
        this.index = index;
        this.location = location;
        this.nickname = nickname;
    }

    public int getIndex()     
    { 
    	return index; 
    }
    public Location getLocation() 
    { 
    	return location; 
    }
    
    public String getNickname()   
    { 
    	return nickname; 
    }

    public void setLocation(Location location) 
    { 
    	this.location = location; 
    }
    
    public void setNickname(String nickname)   
    { 
    	this.nickname = nickname; 
    }

    public boolean isEmpty() 
    { 
    	return location == null; 
    }
}