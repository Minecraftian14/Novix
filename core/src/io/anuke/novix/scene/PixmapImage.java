package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/* renamed from: io.anuke.novix.scene.PixmapImage */
public class PixmapImage extends Actor {
    public final Pixmap pixmap;
    private Texture texture;

    public PixmapImage(Pixmap pixmap2) {
        this.pixmap = pixmap2;
        this.texture = new Texture(pixmap2);
    }

    public void draw(Batch batch, float alpha) {
        batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        batch.draw(this.texture, getX(), getY(), getWidth(), getHeight());
    }

    public void updateTexture() {
        this.texture.draw(this.pixmap, 0, 0);
    }
}
