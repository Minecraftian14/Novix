package io.anuke.ucore.graphics;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/* renamed from: io.anuke.ucore.graphics.SolidPolygon */
public class SolidPolygon {
    private PolygonRegion region;
    private PolygonSprite sprite;
    private TextureRegion texture;
    private EarClippingTriangulator tri = new EarClippingTriangulator();

    public SolidPolygon(TextureRegion texture2, Array<Vector2> vertices) {
        this.texture = texture2;
        setVertices(vertices);
    }

    public void setVertices(Array<Vector2> vertices) {
        float[] floats = new float[(vertices.size * 2)];
        for (int i = 0; i < vertices.size; i++) {
            floats[i * 2] = vertices.get(i).x;
            floats[(i * 2) + 1] = vertices.get(i).y;
        }
        this.region = new PolygonRegion(this.texture, floats, this.tri.computeTriangles(floats).toArray());
        if (this.sprite == null) {
            this.sprite = new PolygonSprite(this.region);
        }
        this.sprite.setRegion(this.region);
    }

    public void draw(PolygonSpriteBatch batch) {
        this.sprite.draw(batch);
    }

    public PolygonSprite sprite() {
        return this.sprite;
    }

    public void setPosition(float x, float y) {
        this.sprite.setPosition(x, y);
    }
}
