package io.anuke.utools.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

/* renamed from: io.anuke.utools.io.ShadowGenerator */
public class ShadowGenerator {
    static final float ratio = 0.6f;

    public void generateImages() {
        float ratio2 = ratio;
        for (int i = 3; i < 70; i++) {
            ratio2 *= 0.985f;
            PixmapIO.writePNG(Gdx.files.local("shadows/shadow" + (i * 2) + ".png"), generateShadow(i * 2, ratio2));
        }
        System.out.println("Done generating shadows.");
    }

    public Pixmap generateShadow(int width, float ratio2) {
        if (width == 8) {
            ratio2 = 0.5f;
        }
        int height = Math.round(((float) width) * ratio2);
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((sqr((((float) x) - (((float) width) / 2.0f)) + 0.5f) / sqr(((float) width) / 2.0f)) + (sqr((((float) y) - (((float) height) / 2.0f)) + 0.5f) / sqr(((float) height) / 2.0f)) <= 1.0f) {
                    pixmap.drawPixel(x, y, Color.rgba8888(Color.BLACK));
                }
            }
        }
        return pixmap;
    }

    /* access modifiers changed from: package-private */
    public float sqr(float a) {
        return (float) Math.pow((double) a, 2.0d);
    }
}
