package io.anuke.utools.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import java.util.Iterator;
import io.anuke.ucore.graphics.PixmapUtils;

/* renamed from: io.anuke.utools.io.SimpleAtlasUnpacker */
public class SimpleAtlasUnpacker {
    public static void unpack(TextureAtlas atlas, FileHandle directory) {
        ObjectMap<Texture, Pixmap> pixmaps = new ObjectMap<>();
        ObjectSet.ObjectSetIterator<Texture> it = atlas.getTextures().iterator();
        while (it.hasNext()) {
            Texture texture = (Texture) it.next();
            texture.getTextureData().prepare();
            pixmaps.put(texture, texture.getTextureData().consumePixmap());
        }
        Iterator<TextureAtlas.AtlasRegion> it2 = atlas.getRegions().iterator();
        while (it2.hasNext()) {
            TextureAtlas.AtlasRegion region = it2.next();
            Pixmap output = PixmapUtils.crop(pixmaps.get(region.getTexture()), region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
            PixmapIO.writePNG(directory.child(region.name), output);
            output.dispose();
        }
        ObjectMap.Values<Pixmap> it3 = pixmaps.values().iterator();
        while (it3.hasNext()) {
            ((Pixmap) it3.next()).dispose();
        }
    }
}
