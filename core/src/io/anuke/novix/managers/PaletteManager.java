package io.anuke.novix.managers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import io.anuke.novix.Novix;
import io.anuke.novix.graphics.Palette;
import io.anuke.novix.modules.Core;

/* renamed from: io.anuke.novix.managers.PaletteManager */
public class PaletteManager {
    private Palette currentPalette;
    private Json json = new Json();
    private Core main;
    private ObjectMap<String, Palette> palettes = new ObjectMap<>();
    private Array<Palette> palettesort = new Array<>();

    public PaletteManager(Core main2) {
        this.main = main2;
    }

    public Palette getCurrentPalette() {
        return this.currentPalette;
    }

    public void setCurrentPalette(Palette palette) {
        this.currentPalette = palette;
    }

    public Iterable<Palette> getPalettes() {
        this.palettesort.clear();
        ObjectMap.Values<Palette> it = this.palettes.values().iterator();
        while (it.hasNext()) {
            this.palettesort.add((Palette) it.next());
        }
        this.palettesort.sort();
        return this.palettesort;
    }

    public void removePalette(Palette palette) {
        this.palettes.remove(palette.id);
    }

    public void addPalette(Palette palette) {
        this.palettes.put(palette.id, palette);
    }

    public void savePalettes() {
        try {
            this.main.paletteFile.writeString(this.json.toJson(this.palettes), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadPalettes() {
        try {
            this.palettes = (ObjectMap) this.json.fromJson(ObjectMap.class, this.main.paletteFile);
            String id = this.main.prefs.getString("lastpalette");
            if (id != null) {
                this.currentPalette = this.palettes.get(id);
            }
            Novix.log("Palettes loaded.");
        } catch (Exception e) {
            e.printStackTrace();
            Novix.log("Palette file nonexistant or corrupt.");
        }
        if (this.currentPalette == null) {
            this.currentPalette = new Palette("Untitled", generatePaletteID(), 8, true);
            this.palettes.put(this.currentPalette.id, this.currentPalette);
            this.main.prefs.put("lastpalette", this.currentPalette.id);
        }
    }

    public String generatePaletteID() {
        return new StringBuilder(String.valueOf(MathUtils.random(9223372036854775806L))).toString();
    }
}
