package io.anuke.ucore.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ObjectMap;

/* renamed from: io.anuke.ucore.util.ValueMap */
public class ValueMap {
    private ObjectMap<String, Object> map = new ObjectMap<>();

    public void put(String name, Object object) {
        this.map.put(name, object);
    }

    public Object get(String name) {
        return this.map.get(name);
    }

    public boolean has(String name) {
        return this.map.containsKey(name);
    }

    public int getInt(String name) {
        if (!has(name)) {
            return 0;
        }
        return ((Integer) get(name)).intValue();
    }

    public float getFloat(String name) {
        if (!has(name)) {
            return 0.0f;
        }
        return ((Float) get(name)).floatValue();
    }

    public boolean getBoolean(String name) {
        if (!has(name)) {
            return false;
        }
        return ((Boolean) get(name)).booleanValue();
    }

    public Color getColor(String name) {
        return (Color) get(name);
    }
}
