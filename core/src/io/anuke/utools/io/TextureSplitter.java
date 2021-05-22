package io.anuke.utools.io;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import io.anuke.ucore.graphics.PixmapUtils;

/* renamed from: io.anuke.utools.io.TextureSplitter */
public class TextureSplitter {
    private FileHandle file;
    private int size;

    public TextureSplitter(FileHandle file2, int size2) {
        this.size = size2;
        this.file = file2;
    }

    public void split(FileHandle folder, int scale, int zoom) {
        Pixmap pixmap = new Pixmap(this.file);
        int gwidth = pixmap.getWidth() / this.size;
        int gheight = pixmap.getHeight() / this.size;
        for (int gx = 0; gx < gwidth; gx++) {
            for (int gy = 0; gy < gheight; gy++) {
                Pixmap clip = new Pixmap(this.size, this.size, Pixmap.Format.RGBA8888);
                for (int x = gx * this.size; x < (this.size * gx) + this.size; x++) {
                    for (int y = gy * this.size; y < (this.size * gy) + this.size; y++) {
                        clip.drawPixel(x - (this.size * gx), y - (this.size * gy), pixmap.getPixel(x, y));
                    }
                }
                Pixmap scaled = PixmapUtils.scale(clip, (float) scale);
                Pixmap zoomed = PixmapUtils.zoom(scaled, zoom);
                PixmapIO.writePNG(folder.child("patch-" + gx + "," + gy + ".png"), zoomed);
                clip.dispose();
                scaled.dispose();
                zoomed.dispose();
            }
        }
    }
}
