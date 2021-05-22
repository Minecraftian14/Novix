package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.VisUI;

/* renamed from: io.anuke.novix.scene.GridImage */
public class GridImage extends Actor {
    private int imageHeight;
    private int imageWidth;

    public GridImage(int w, int h) {
        this.imageWidth = w;
        this.imageHeight = h;
    }

    public void draw(Batch batch, float alpha) {
        TextureRegion blank = VisUI.getSkin().getAtlas().findRegion("white");
        float xspace = getWidth() / ((float) this.imageWidth);
        float yspace = getHeight() / ((float) this.imageHeight);
        int x = 0;
        while (x <= this.imageWidth) {
            batch.draw(blank, (float) ((int) ((getX() + (((float) x) * xspace)) - 1.0f)), getY() - 1.0f, 2.0f, getHeight() + ((float) (x == this.imageWidth ? 1 : 0)));
            x++;
        }
        for (int y = 0; y <= this.imageHeight; y++) {
            batch.draw(blank, getX() - 1.0f, (float) ((int) ((getY() + (((float) y) * yspace)) - 1.0f)), getWidth(), 2.0f);
        }
    }

    public void setImageSize(int w, int h) {
        this.imageWidth = w;
        this.imageHeight = h;
    }
}
