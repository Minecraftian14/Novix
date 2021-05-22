package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/* renamed from: io.anuke.ucore.graphics.TextureMap */
public class TextureMap implements Disposable {
    public String errortexture = "error";
    private ObjectMap<String, Texture> map = new ObjectMap<>();
    public String path = "";

    public TextureMap(String path2) {
        this.path = path2;
    }

    public void repeatWrap(String... textures) {
        for (String string : textures) {
            try {

                get(string).setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
            } catch (Exception t) {
                t.printStackTrace();
            }
        }
    }

    public Texture get(String name) {
        if (!this.map.containsKey(name)) {
            try {
                this.map.put(name, new Texture(String.valueOf(this.path) + name + ".png"));
            } catch (Exception e) {
                this.map.put(name, null);
            }
        }
        return this.map.get(name);
    }

    public void put(String name, Texture texture) {
        this.map.put(name, texture);
    }

    public boolean has(String name) {
        return this.map.containsKey(name);
    }

    public void dispose() {
        ObjectMap.Values<Texture> it = this.map.values().iterator();
        while (it.hasNext()) {
            ((Texture) it.next()).dispose();
        }
    }
}
