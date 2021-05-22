package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.anuke.ucore.graphics.Textures;

/* renamed from: io.anuke.novix.scene.AlphaImage */
public class AlphaImage extends Actor {
    private float imageHeight;
    private float imageWidth;

    public AlphaImage(float w, float h) {
        this.imageWidth = w;
        this.imageHeight = h;
    }

    public void draw(Batch batch, float alpha) {
        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        batch.draw(Textures.get("alpha"), getX(), getY(), getWidth(), getHeight(), 0.0f, 0.0f, this.imageWidth, this.imageHeight);
    }

    public void setImageSize(int w, int h) {
        this.imageWidth = (float) w;
        this.imageHeight = (float) h;
    }
}
