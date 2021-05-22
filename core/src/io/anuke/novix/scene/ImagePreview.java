package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import io.anuke.novix.modules.Core;

/* renamed from: io.anuke.novix.scene.ImagePreview */
public class ImagePreview extends Group {
    public final PixmapImage image;
    protected Stack stack = new Stack();

    public ImagePreview(Pixmap pixmap) {
        this.image = new PixmapImage(pixmap);
        int scale = pixmap.getWidth();
        AlphaImage alpha = new AlphaImage((float) scale, (float) ((int) (((float) scale) * (1.0f / (((float) pixmap.getWidth()) / ((float) pixmap.getHeight()))))));
        GridImage grid = new GridImage(Core.instance.drawgrid.canvas.width(), Core.instance.drawgrid.canvas.height());
        BorderImage border = new BorderImage();
        border.setColor(Color.CORAL);
        this.stack.add(alpha);
        this.stack.add(this.image);
        if (Core.instance.prefs.getBoolean("grid")) {
            this.stack.add(grid);
        }
        this.stack.add(border);
        addActor(this.stack);
    }

    public void draw(Batch batch, float alpha) {
        super.draw(batch, alpha);
        this.stack.setBounds(0.0f, 0.0f, getWidth(), getHeight());
    }

    public PixmapImage getImage() {
        return this.image;
    }
}
