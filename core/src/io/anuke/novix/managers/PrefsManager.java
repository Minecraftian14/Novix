package io.anuke.novix.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import io.anuke.novix.modules.Core;

/* renamed from: io.anuke.novix.managers.PrefsManager */
public class PrefsManager {
    private Core core;
    private Preferences prefs = Gdx.app.getPreferences("pixeleditor");

    public PrefsManager(Core core2) {
        this.core = core2;
    }

    public void put(String name, boolean value) {
        this.prefs.putBoolean(name, value);
        if (name.equals("grid") && this.core.toolmenu != null) {
            this.core.toolmenu.getGridButton().setChecked(value);
        }
    }

    public void put(String name, String value) {
        this.prefs.putString(name, value);
    }

    public void put(String name, float value) {
        this.prefs.putFloat(name, value);
    }

    public void put(String name, int value) {
        this.prefs.putInteger(name, value);
    }

    public void put(String name, long value) {
        this.prefs.putLong(name, value);
    }

    public boolean getBoolean(String name) {
        return getBoolean(name, false);
    }

    public boolean getBoolean(String name, boolean val) {
        return this.prefs.getBoolean(name, val);
    }

    public float getFloat(String name) {
        return getFloat(name, 0.0f);
    }

    public float getFloat(String name, float def) {
        return this.prefs.getFloat(name, def);
    }

    public int getInteger(String name) {
        return getInteger(name, 0);
    }

    public int getInteger(String name, int i) {
        return this.prefs.getInteger(name, i);
    }

    public String getString(String name) {
        return getString(name, (String) null);
    }

    public String getString(String name, String def) {
        return this.prefs.getString(name, def);
    }

    public long getLong(String name) {
        return this.prefs.getLong(name);
    }

    public void save() {
        this.prefs.flush();
    }
}
