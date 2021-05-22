package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/* renamed from: io.anuke.ucore.graphics.ShapeUtils */
public class ShapeUtils {
    public static TextureRegion region = PixmapUtils.blankTextureRegion();
    public static float thickness = 1.0f;

    public static void polygon(Batch batch, Vector2[] vertices, float offsetx, float offsety, float scl) {
        int i = 0;
        while (i < vertices.length) {
            Vector2 current = vertices[i];
            Vector2 next = i == vertices.length + -1 ? vertices[0] : vertices[i + 1];
            line(batch, (current.x * scl) + offsetx, (current.y * scl) + offsety, (next.x * scl) + offsetx, (next.y * scl) + offsety);
            i++;
        }
    }

    public static void polygon(Batch batch, float[] vertices, float offsetx, float offsety, float scl) {
        float x2;
        float y2;
        for (int i = 0; i < vertices.length / 2; i++) {
            float x = vertices[i * 2];
            float y = vertices[(i * 2) + 1];
            if (i == (vertices.length / 2) - 1) {
                x2 = vertices[0];
                y2 = vertices[1];
            } else {
                x2 = vertices[(i * 2) + 2];
                y2 = vertices[(i * 2) + 3];
            }
            line(batch, (x * scl) + offsetx, (y * scl) + offsety, (x2 * scl) + offsetx, (y2 * scl) + offsety);
        }
    }

    public static void line(Batch batch, float x, float y, float x2, float y2) {
        Batch batch2 = batch;
        batch2.draw(region, x - (thickness / 2.0f), y - (thickness / 2.0f), thickness / 2.0f, thickness / 2.0f, Vector2.dst(x, y, x2, y2) + (thickness / 2.0f), thickness, 1.0f, 1.0f, ((float) Math.atan2((double) (y2 - y), (double) (x2 - x))) * 57.295776f);
    }

    public static void rect(Batch batch, float x, float y, float width, float height, int thickness2) {
        rect(batch, x, y, width, height, thickness2, 0);
    }

    public static void rect(Batch batch, float x, float y, float width, float height, int thickness2, int space) {
        rect(batch, x, y, width, height, thickness2, space, space);
    }

    public static void rect(Batch batch, float x, float y, float width, float height, int thickness2, int xspace, int yspace) {
        float x2 = x - ((float) xspace);
        float y2 = y - ((float) yspace);
        float width2 = width + ((float) (xspace * 2));
        float height2 = height + ((float) (yspace * 2));
        batch.draw(region, x2, y2, width2, (float) thickness2);
        Batch batch2 = batch;
        float f = x2;
        batch2.draw(region, f, y2 + height2, width2, (float) (-thickness2));
        Batch batch3 = batch;
        batch3.draw(region, x2 + width2, y2, (float) (-thickness2), height2);
        batch.draw(region, x2, y2, (float) thickness2, height2);
    }
}
