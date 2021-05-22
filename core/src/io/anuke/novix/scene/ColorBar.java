package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.NumberUtils;
import com.kotcrab.vis.ui.VisUI;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.graphics.Textures;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.ColorBar */
public class ColorBar extends BarActor {
    protected static Color temp = new Color();
    protected Color leftColor;
    protected Color rightColor;
    protected Sprite sprite;

    public ColorBar(boolean vertical) {
        super(vertical);
        this.sprite = new Sprite((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"));
    }

    public ColorBar() {
        this(false);
    }

    public void draw(Batch batch, float alpha) {
        drawBorder(batch, alpha);
        setSpriteBounds(this.sprite);
        internalSetColors(this.leftColor.cpy().sub(1.0f - this.brightness, 1.0f - this.brightness, 1.0f - this.brightness, 0.0f), this.rightColor.cpy().sub(1.0f - this.brightness, 1.0f - this.brightness, 1.0f - this.brightness, 0.0f));
        setAlpha(alpha);
        if (this.leftColor.a <= 0.99f || this.rightColor.a <= 0.99f) {
            batch.draw(Textures.get("alpha"), border + getX(), border + getY(), getWidth() - (border * 2.0f), getHeight() - (border * 2.0f), 0.0f, 0.0f, (getWidth() - (border * 2.0f)) / MiscUtils.densityScale(20.0f), (getHeight() - (border * 2.0f)) / MiscUtils.densityScale(20.0f));
        }
        this.sprite.draw(batch);
        drawDisabled(batch, this.sprite.getX(), this.sprite.getY(), this.sprite.getWidth(), this.sprite.getHeight());
        internalSetColors(this.leftColor, this.rightColor);
        drawSelection(batch, alpha);
    }

    public void setColors(Color left, Color right) {
        this.leftColor = left;
        this.rightColor = right;
        internalSetColors(left, right);
    }

    private void internalSetColors(Color left, Color right) {
        this.sprite.getVertices()[2] = left.toFloatBits();
        this.sprite.getVertices()[7] = left.toFloatBits();
        this.sprite.getVertices()[12] = right.toFloatBits();
        this.sprite.getVertices()[17] = right.toFloatBits();
    }

    public void setRightColor(Color right) {
        this.rightColor = right;
        this.sprite.getVertices()[12] = right.toFloatBits();
        this.sprite.getVertices()[17] = right.toFloatBits();
    }

    public Color getRightColor() {
        return this.rightColor;
    }

    public void setAlpha(float a) {
        for (int colorvertice : colors) {
            int intBits = NumberUtils.floatToIntColor(this.sprite.getVertices()[colorvertice]);
            Color color = temp;
            color.r = ((float) (intBits & 255)) / 255.0f;
            color.g = ((float) ((intBits >>> 8) & 255)) / 255.0f;
            color.b = ((float) ((intBits >>> 16) & 255)) / 255.0f;
            color.a = ((float) ((intBits >>> 24) & 255)) / 255.0f;
            temp.a *= a;
            this.sprite.getVertices()[colorvertice] = temp.toFloatBits();
        }
    }

    public void setSelection(float s) {
        this.selection = s;
    }

    public float getSelection() {
        return this.selection;
    }

    public Color getSelectedColor() {
        return Hue.mix(this.leftColor, this.rightColor, this.selection);
    }
}
