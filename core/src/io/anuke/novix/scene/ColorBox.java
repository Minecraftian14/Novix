package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.kotcrab.vis.ui.VisUI;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.graphics.Textures;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.ColorBox */
public class ColorBox extends Widget implements Disableable {
    public Color backgroundColor;
    private boolean disabled;
    public Color disabledColor;
    public Color hoverColor;
    public boolean hovered;
    public boolean selected;
    public Color selectedColor;

    /* renamed from: io.anuke.novix.scene.ColorBox$Style */
    public static class Style {
        public Color box = Color.DARK_GRAY;
        public Color disabled = Hue.lightness(0.15f);
        public Color hovered = Color.GRAY;
        public Color selected = Color.CORAL;
    }

    public ColorBox() {
        this.backgroundColor = Color.valueOf("29323d");
        this.disabledColor = Color.valueOf("0f1317");
        this.hoverColor = Color.GRAY;
        this.selectedColor = Color.CORAL;
    }

    public ColorBox(Color color) {
        this();
        setColor(color);
    }

    public void addSelectListener() {
        addListener(new SelectListener(this, (SelectListener) null));
    }

    public void draw(Batch batch, float alpha) {
        int grayborder = ((int) getWidth()) / 14;
        int border = 0;
        if (this.selected) {
            border = getBorderThickness();
        }
        if (this.hovered) {
            border = ((int) getWidth()) / 14;
        }
        batch.setColor(!this.selected ? this.backgroundColor : this.selectedColor);
        if (this.hovered) {
            batch.setColor(this.hoverColor);
        }
        MiscUtils.setBatchAlpha(batch, alpha);
        batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), getX() - ((float) border), getY() - ((float) border), ((float) (border * 2)) + getWidth(), ((float) (border * 2)) + getHeight());
        if (MathUtils.isEqual(alpha, 1.0f) || getColor().a < 1.0f) {
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
            batch.draw(Textures.get("alpha"), ((float) grayborder) + getX(), ((float) grayborder) + getY(), getWidth() - ((float) (grayborder * 2)), getHeight() - ((float) (grayborder * 2)), 0.0f, 0.0f, 2.0f, 2.0f);
        }
        batch.setColor(this.disabled ? this.disabledColor : getColor());
        MiscUtils.setBatchAlpha(batch, alpha);
        batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), ((float) grayborder) + getX(), ((float) grayborder) + getY(), getWidth() - ((float) (grayborder * 2)), (getHeight() - ((float) (grayborder * 2))) + 1.0f);
    }

    /* renamed from: io.anuke.novix.scene.ColorBox$SelectListener */
    private class SelectListener extends InputListener {
        private SelectListener() {
        }

        /* synthetic */ SelectListener(ColorBox colorBox, SelectListener selectListener) {
            this();
        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            ColorBox.this.hovered = true;
            ColorBox.this.toFront();
            return true;
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            ColorBox.this.hovered = false;
        }
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled2) {
        setTouchable(disabled2 ? Touchable.disabled : Touchable.enabled);
        this.disabled = disabled2;
    }

    public int getBorderThickness() {
        return ((int) getWidth()) / 6;
    }
}
