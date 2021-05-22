package io.anuke.novix.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import java.nio.ByteBuffer;

import io.anuke.novix.Novix;
import io.anuke.novix.modules.Core;
import io.anuke.ucore.graphics.PixmapUtils;

/* renamed from: io.anuke.novix.tools.PixelCanvas */
public class PixelCanvas implements Disposable {
    private static final ObjectMap<Integer, ByteBuffer> buffers = new ObjectMap<>();
    private DrawAction action = new DrawAction();
    private float alpha = 1.0f;
    private Color color;
    private boolean drawn;
    public final String name;
    public final Pixmap pixmap;
    private Color temp = new Color();
    public final Texture texture;

    public PixelCanvas(Pixmap pixmap2) {
        this.pixmap = pixmap2;
        this.name = Core.i.getCurrentProject().name;
        this.texture = new Texture(pixmap2);
        updateTexture();
    }

    public PixelCanvas(int width, int height) {
        this.pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        this.texture = new Texture(this.pixmap);
        this.name = Core.i.getCurrentProject().name;
        updateTexture();
    }

    public void drawPixel(int x, int y, boolean update) {
        int preColor = getIntColor(x, y);
        this.pixmap.drawPixel(x, (height() - 1) - y);
        this.action.push(x, y, preColor, getIntColor(x, y));
        if (update) {
            PixmapUtils.drawPixel(this.texture, x, (height() - 1) - y, getIntColor(x, y));
        } else {
            this.drawn = true;
        }
    }

    public void drawPixelBlendless(int x, int y, int color2) {
        int preColor = getIntColor(x, y);
        this.pixmap.drawPixel(x, (height() - 1) - y, color2);
        this.action.push(x, y, preColor, color2);
    }

    public void erasePixel(int x, int y, boolean update) {
        int preColor = getIntColor(x, y);
        this.temp.set(preColor);
        float newalpha = this.temp.a - this.alpha;
        if (newalpha <= 0.0f || MathUtils.isEqual(newalpha, 0.0f)) {
            newalpha = 0.0f;
            this.temp.set(0.0f, 0.0f, 0.0f, 0.0f);
        }
        int newcolor = Color.rgba8888(this.temp.r, this.temp.g, this.temp.b, newalpha);
        pixmap.setBlending(Pixmap.Blending.None);
        this.pixmap.drawPixel(x, (height() - 1) - y, newcolor);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        if (update) {
            PixmapUtils.drawPixel(this.texture, x, (height() - 1) - y, newcolor);
        }
        this.action.push(x, y, preColor, newcolor);
        this.drawn = true;
    }

    public void erasePixelFullAlpha(int x, int y) {
        int color2 = getIntColor(x, y);
        pixmap.setBlending(Pixmap.Blending.None);
        this.pixmap.drawPixel(x, (height() - 1) - y, 0);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        this.action.push(x, y, color2, 0);
    }

    public void drawRadius(int x, int y, int rad) {
        this.texture.bind();
        for (int rx = -rad; rx <= rad; rx++) {
            for (int ry = -rad; ry <= rad; ry++) {
                if (Vector2.dst((float) rx, (float) ry, 0.0f, 0.0f) < ((float) rad) - 0.5f) {
                    drawPixel(x + rx, y + ry, false);
                }
            }
        }
        this.drawn = false;
        update(x - rad, y - rad, rad * 2, rad * 2);
    }

    public void eraseRadius(int x, int y, int rad) {
        this.texture.bind();
        for (int rx = -rad; rx <= rad; rx++) {
            for (int ry = -rad; ry <= rad; ry++) {
                if (Vector2.dst((float) rx, (float) ry, 0.0f, 0.0f) < ((float) rad) - 0.5f) {
                    erasePixel(x + rx, y + ry, false);
                }
            }
        }
        this.drawn = false;
        update(x - rad, y - rad, rad * 2, rad * 2);
    }

    private void update(int x, int y, int w, int h) {
        ByteBuffer buffer;
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        if (x >= width()) {
            x = width() - (w / 2);
        }
        if (y >= height()) {
            y = height() - (h / 2);
        }
        if (x + w >= width()) {
            w = width() - x;
        }
        if (y + h >= height()) {
            h = height() - y;
        }
        int size = w * h;
        if (!buffers.containsKey(Integer.valueOf(size))) {
            buffer = ByteBuffer.allocateDirect(size * 4);
            buffers.put(Integer.valueOf(size), buffer);
        } else {
            buffer = buffers.get(Integer.valueOf(size));
        }
        buffer.clear();
        buffer.position(0);
        for (int cy = h - 1; cy >= 0; cy--) {
            for (int cx = 0; cx < w; cx++) {
                buffer.putInt(this.pixmap.getPixel(x + cx, ((height() - 1) - y) - cy));
            }
        }
        buffer.position(0);
        Gdx.gl.glTexSubImage2D(this.texture.glTarget, 0, x, (height() - y) - h, w, h, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, buffer);
    }

    public void drawPixel(int x, int y, Color color2) {
        setColor(color2);
        drawPixel(x, y, true);
    }

    public void drawPixelActionless(int x, int y, int color2) {
        this.pixmap.setColor(color2);
        this.pixmap.drawPixel(x, (height() - 1) - y);
    }

    public void updatePixmapColor() {
        this.pixmap.setColor(this.color);
    }

    public Color getColor(int x, int y) {
        return new Color(this.pixmap.getPixel(x, (height() - 1) - y));
    }

    public int getIntColor(int x, int y) {
        return this.pixmap.getPixel(x, (height() - 1) - y);
    }

    public void setAlpha(float alpha2) {
        this.alpha = alpha2;
        setColor(this.color);
    }

    public void setColor(Color color2) {
        setColor(color2, false);
    }

    public void setColor(Color color2, boolean ignoreAlpha) {
        this.color = color2;
        if (!ignoreAlpha) {
            color2.a = this.alpha;
        }
        this.pixmap.setColor(color2);
    }

    public void update() {
        if (this.drawn) {
            this.drawn = false;
            updateTexture();
        }
    }

    public void updateTexture() {
        if (!Core.i.projectmanager.isSavingProject()) {
            Novix.log("Updating...");
            this.texture.draw(this.pixmap, 0, 0);
            this.drawn = false;
            return;
        }
        Novix.log("skipping drawing...");
        this.drawn = true;
    }

    public void updateAndPush() {
        this.texture.draw(this.pixmap, 0, 0);
        pushActions();
    }

    public void pushActions() {
        if (this.action.positions.size != 0) {
            Core.i.actionStack().add(this.action);
            this.action = new DrawAction();
        }
    }

    public int width() {
        return this.pixmap.getWidth();
    }

    public int height() {
        return this.pixmap.getHeight();
    }

    public void drawPixmap(Pixmap pixmap2) {
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                this.action.push(x, y, getIntColor(x, y), pixmap2.getPixel(x, (height() - 1) - y));
            }
        }
        pixmap.setBlending(Pixmap.Blending.None);
        this.pixmap.drawPixmap(pixmap2, 0, 0);
        pixmap.setBlending(Pixmap.Blending.SourceOver);
        updateAndPush();
    }

    public float getAlpha() {
        return this.alpha;
    }

    public void dispose() {
        Novix.log("DISPOSING canvas! " + this.name);
        this.texture.dispose();
        this.pixmap.dispose();
    }
}
