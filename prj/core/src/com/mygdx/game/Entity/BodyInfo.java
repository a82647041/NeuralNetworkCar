package com.mygdx.game.Entity;

import java.util.HashMap;

public class BodyInfo {
    public String name;
    HashMap<String,Object> data;
    public BodyInfo(String name)
    {
        this.name = name;
        data = new HashMap<>();
    }
    public void add(String key, Object value)
    {
        data.put(key, value);
    }
    public Object get(String key)
    {
        return data.get(key);
    }
}
