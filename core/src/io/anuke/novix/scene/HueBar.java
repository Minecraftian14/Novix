package io.anuke.novix.scene;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.ucore.graphics.Textures;

/* renamed from: io.anuke.novix.scene.HueBar */
public class HueBar extends BarActor {
    public HueBar() {
        super(false);
        if (!Textures.loaded("huebar")) {
            Textures.put("huebar", PixmapUtils.hueTexture(Input.Keys.NUMPAD_6, 20));
        }
    }

    public void draw(Batch batch, float alpha) {
        drawBorder(batch, alpha);
        batch.setColor(this.brightness, this.brightness, this.brightness, alpha);
        batch.draw(Textures.get("huebar"), border + getX(), border + getY(), getWidth() - (border * 2.0f), getHeight() - (border * 2.0f));
        drawDisabled(batch, getX() + border, getY() + border, getWidth() - (border * 2.0f), getHeight() - (border * 2.0f));
        batch.setColor(Color.WHITE);
        drawSelection(batch, alpha);
    }

    public Color getSelectedColor() {
        return Hue.fromHSB(this.selection, 1.0f, 1.0f);
    }
}
