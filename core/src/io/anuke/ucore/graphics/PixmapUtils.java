package io.anuke.ucore.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import java.nio.ByteBuffer;

import io.anuke.ucore.UCore;

/* renamed from: io.anuke.ucore.graphics.PixmapUtils */
public class PixmapUtils {
    private static ByteBuffer bytes = ByteBuffer.allocateDirect(4);

    /* renamed from: io.anuke.ucore.graphics.PixmapUtils$PixmapTraverser */
    public interface PixmapTraverser {
        void traverse(int i, int i2);
    }

    public static Pixmap copy(Pixmap input) {
        Pixmap pixmap = new Pixmap(input.getWidth(), input.getHeight(), Pixmap.Format.RGBA8888);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                pixmap.drawPixel(x, y, input.getPixel(x, y));
            }
        }
        return pixmap;
    }

    public static Pixmap scale(Pixmap input, float scale) {
        return scale(input, scale, scale);
    }

    public static Pixmap scale(Pixmap input, float scalex, float scaley) {
        Pixmap pixmap = new Pixmap((int) (((float) input.getWidth()) * scalex), (int) (((float) input.getHeight()) * scaley), Pixmap.Format.RGBA8888);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                pixmap.drawPixel(x, y, input.getPixel((int) (((float) x) / scalex), (int) (((float) y) / scaley)));
            }
        }
        return pixmap;
    }

    public static Pixmap outline(Pixmap input, Color color) {
        Pixmap pixmap = copy(input);
        pixmap.setColor(color);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                if (!empty(input.getPixel(x, y + 1)) || !empty(input.getPixel(x, y - 1)) || !empty(input.getPixel(x - 1, y)) || !empty(input.getPixel(x + 1, y))) {
                    pixmap.drawPixel(x, y);
                }
            }
        }
        return pixmap;
    }

    public static Pixmap zoom(Pixmap input, int scale) {
        Pixmap pixmap = new Pixmap(input.getWidth(), input.getHeight(), Pixmap.Format.RGBA8888);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                pixmap.drawPixel(x, y, input.getPixel((x / scale) + ((pixmap.getWidth() / 2) / scale), (y / scale) + ((pixmap.getHeight() / 2) / scale)));
            }
        }
        return pixmap;
    }

    public static Pixmap resize(Pixmap input, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(input, (width / 2) - (input.getWidth() / 2), (height / 2) - (input.getHeight() / 2));
        return pixmap;
    }

    public static Pixmap crop(Pixmap input, int x, int y, int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.drawPixmap(input, 0, 0, x, y, width, height);
        return pixmap;
    }

    public static Pixmap rotate(Pixmap input, float angle) {
        Vector2 vector = new Vector2();
        Pixmap pixmap = new Pixmap(input.getHeight(), input.getWidth(), Pixmap.Format.RGBA8888);
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                vector.set((((float) x) - (((float) input.getWidth()) / 2.0f)) + 0.5f, ((float) y) - (((float) input.getHeight()) / 2.0f));
                vector.rotate(-angle);
                pixmap.drawPixel((((int) ((vector.x + (((float) input.getWidth()) / 2.0f)) + 0.01f)) - (input.getWidth() / 2)) + (pixmap.getWidth() / 2), (((int) ((vector.y + (((float) input.getHeight()) / 2.0f)) + 0.01f)) - (input.getHeight() / 2)) + (pixmap.getHeight() / 2), input.getPixel(x, y));
            }
        }
        return pixmap;
    }

    private static boolean empty(int i) {
        return (i & 255) == 0;
    }

    public static boolean isDisposed(Pixmap pix) {
        return ((Boolean) UCore.getPrivate(pix, "disposed")).booleanValue();
    }

    public static void traverse(Pixmap input, PixmapTraverser t) {
        for (int x = 0; x < input.getWidth(); x++) {
            for (int y = 0; y < input.getHeight(); y++) {
                t.traverse(x, y);
            }
        }
    }

    public static Pixmap huePixmap(int width, int height) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int x = 0; x < width; x++) {
            pixmap.setColor(Hue.fromHSB(((float) x) / ((float) width), 1.0f, 1.0f));
            for (int y = 0; y < height; y++) {
                pixmap.drawPixel(x, y);
            }
        }
        return pixmap;
    }

    public static Texture hueTexture(int width, int height) {
        return new Texture(huePixmap(width, height));
    }

    public static Pixmap blankPixmap() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        return pixmap;
    }

    public static Texture blankTexture() {
        Texture texture = new Texture(blankPixmap());
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        return texture;
    }

    public static TextureRegion blankTextureRegion() {
        return new TextureRegion(blankTexture());
    }

    public static void drawPixel(Texture texture, int x, int y, int color) {
        bytes.clear();
        bytes.position(0);
        bytes.putInt(color);
        bytes.position(0);
        Gdx.gl.glTexSubImage2D(texture.glTarget, 0, x, y, 1, 1, GL20.GL_RGBA, GL20.GL_UNSIGNED_BYTE, bytes);
    }
}
