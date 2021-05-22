package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;
import java.util.Iterator;

/* renamed from: io.anuke.ucore.graphics.Atlas */
public class Atlas extends TextureAtlas implements RegionProvider {
    TextureAtlas.AtlasRegion error;
    ObjectMap<Texture, Pixmap> pixmaps = new ObjectMap<>();
    ObjectMap<String, TextureAtlas.AtlasRegion> regionmap = new ObjectMap<>();

    public Atlas(FileHandle file) {
        super(file);
        Iterator<TextureAtlas.AtlasRegion> it = super.getRegions().iterator();
        while (it.hasNext()) {
            TextureAtlas.AtlasRegion r = it.next();
            String[] split = r.name.split("/");
            if (split.length > 1) {
                if (this.regionmap.containsKey(split[1])) {
                    Gdx.app.error("Atlas", "Texture conflict! (" + split[1] + ")");
                }
                this.regionmap.put(split[1], r);
                r.name = split[1];
            } else {
                if (this.regionmap.containsKey(split[0])) {
                    Gdx.app.error("Atlas", "Texture conflict! (" + split[0] + ")");
                }
                this.regionmap.put(split[0], r);
            }
            r.name = new String(r.name);
        }
        this.error = findRegion("error");
    }

    public Pixmap getPixmapOf(String name) {
        return getPixmapOf((TextureRegion) findRegion(name));
    }

    public Pixmap getPixmapOf(TextureRegion region) {
        Texture texture = region.getTexture();
        if (this.pixmaps.containsKey(texture)) {
            return this.pixmaps.get(texture);
        }
        texture.getTextureData().prepare();
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        this.pixmaps.put(texture, pixmap);
        return pixmap;
    }

    public TextureAtlas.AtlasRegion addRegion(String name, Texture texture, int x, int y, int width, int height) {
        TextureAtlas.AtlasRegion aregion = super.addRegion(name, texture, x, y, width, height);
        this.regionmap.put(name, aregion);
        return aregion;
    }

    public void setErrorRegion(TextureAtlas.AtlasRegion region) {
        this.error = region;
    }

    public TextureAtlas.AtlasRegion findRegion(String name) {
        TextureAtlas.AtlasRegion r = this.regionmap.get(name);
        if (r == null) {
            return this.error;
        }
        return r;
    }

    public float regionHeight(String name) {
        return (float) findRegion(name).getRegionHeight();
    }

    public float regionWidth(String name) {
        return (float) findRegion(name).getRegionWidth();
    }

    public boolean hasRegion(String s) {
        return !findRegion(s).equals(this.error);
    }

    public void dispose() {
        super.dispose();
        ObjectMap.Values<Pixmap> it = this.pixmaps.values().iterator();
        while (it.hasNext()) {
            ((Pixmap) it.next()).dispose();
        }
    }

    public TextureRegion getRegion(String name) {
        return findRegion(name);
    }
}
