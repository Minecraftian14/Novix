package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

/* renamed from: io.anuke.ucore.graphics.FrameBufferMap */
public class FrameBufferMap implements Disposable {
    private ObjectMap<String, FrameBuffer> buffers = new ObjectMap<>();

    public FrameBuffer get(String name) {
        if (!this.buffers.containsKey(name)) {
            return add(name, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
        }
        return this.buffers.get(name);
    }

    public FrameBuffer add(String name, int width, int height) {
        if (this.buffers.containsKey(name)) {
            return this.buffers.get(name);
        }
        FrameBuffer buffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, true);
        ((Texture) buffer.getColorBufferTexture()).setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.buffers.put(name, buffer);
        return buffer;
    }

    public void begin(String name) {
        get(name).begin();
    }

    public void end(String name) {
        get(name).end();
    }

    public Texture texture(String name) {
        return (Texture) get(name).getColorBufferTexture();
    }

    public void remove(String name) {
        get(name).dispose();
        this.buffers.remove(name);
    }

    public void dispose() {
        ObjectMap.Values<FrameBuffer> it = this.buffers.values().iterator();
        while (it.hasNext()) {
            ((FrameBuffer) it.next()).dispose();
        }
    }
}
