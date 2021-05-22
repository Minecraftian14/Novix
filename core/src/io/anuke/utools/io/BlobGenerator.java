package io.anuke.utools.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

/* renamed from: io.anuke.utools.io.BlobGenerator */
public class BlobGenerator {
    static final float ratio = 0.6f;

    public static void generateImages() {
        float ratio2 = ratio;
        for (int i = 5; i < 70; i++) {
            ratio2 *= 0.985f;
            PixmapIO.writePNG(Gdx.files.local("blobs/blob" + i + ".png"), generateBlob(i, ratio2));
        }
        System.out.println("Done generating blobs.");
    }

    public static Pixmap generateBlob(int width, float ratio2) {
        if (width == 8) {
            ratio2 = 0.5f;
        }
        int height = Math.round(((float) width) * ratio2);
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((sqr((((float) x) - (((float) width) / 2.0f)) + 0.5f) / sqr(((float) width) / 2.0f)) + (sqr((((float) y) - (((float) height) / 2.0f)) + 0.5f) / sqr(((float) height) / 2.0f)) <= 1.0f) {
                    pixmap.drawPixel(x, y, Color.rgba8888(Color.WHITE));
                }
            }
        }
        return pixmap;
    }

    static float sqr(float a) {
        return (float) Math.pow((double) a, 2.0d);
    }

    public static void main(String[] args) {
        generateImages();
    }
}
