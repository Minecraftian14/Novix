package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/* renamed from: io.anuke.novix.scene.ShiftedImage */
public class ShiftedImage extends Image {
    public int offsetx;
    public int offsety;
    private Texture texture;

    public ShiftedImage(Texture tex) {
        super(tex);
        this.texture = tex;
    }

    public void draw(Batch batch, float alpha) {
        setPosition(getX() + (((float) this.offsetx) * (getWidth() / ((float) this.texture.getWidth()))), getY() + (((float) this.offsety) * (getHeight() / ((float) this.texture.getHeight()))));
        super.draw(batch, alpha);
    }
}
