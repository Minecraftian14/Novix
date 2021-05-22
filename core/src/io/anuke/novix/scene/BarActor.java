package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.kotcrab.vis.ui.VisUI;
import io.anuke.ucore.util.Mathf;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.BarActor */
public abstract class BarActor extends Actor implements Disableable {

    /* renamed from: s */
    public static final float f322s = MiscUtils.densityScale();
    public static final float selectionWidth = (20.0f * f322s);
    public float brightness = 1.0f;
    /* access modifiers changed from: private */
    public boolean disabled;
    protected boolean selected;
    public float selection = 1.0f;
    protected final boolean vertical;

    protected static final float border = (f322s * 5.0f);
    public static Color borderColor = Color.valueOf("29323d");
    protected static final int[] colors = {2, 7, 12, 17};
    protected static final float margin = (f322s * 5.0f);

    public abstract Color getSelectedColor();

    public BarActor(boolean vertical2) {
        this.vertical = vertical2;
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (BarActor.this.disabled) {
                    return false;
                }
                BarActor.this.updateSelection(x, y);
                BarActor.this.selected = true;
                return true;
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!BarActor.this.disabled) {
                    BarActor.this.updateSelection(x, y);
                }
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!BarActor.this.disabled && pointer == 0) {
                    BarActor.this.selected = false;
                }
            }
        });
    }

    public void setDisabled(boolean isDisabled) {
        this.disabled = isDisabled;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void drawDisabled(Batch batch, float x, float y, float width, float height) {
        if (this.disabled) {
            float color = batch.getPackedColor();
            batch.setColor(0.0f, 0.0f, 0.0f, batch.getColor().a * 0.5f);
            batch.draw(VisUI.getSkin().getRegion("white"), x, y, width, height);
            batch.setColor(color);
        }
    }

    /* access modifiers changed from: private */
    public void updateSelection(float x, float y) {
        this.selection = !this.vertical ? (x - (selectionWidth / 2.0f)) / (getWidth() - selectionWidth) : (y - (selectionWidth / 2.0f)) / (getHeight() - selectionWidth);
        this.selection = Mathf.clamp(this.selection, 0.0f, 1.0f);
        fire(new ChangeListener.ChangeEvent());
        onSelectionUpdated();
    }

    public void onSelectionUpdated() {
    }

    /* access modifiers changed from: protected */
    public void setSpriteBounds(Sprite sprite) {
        if (this.vertical) {
            sprite.setBounds(((getX() + border) + (getWidth() / 2.0f)) - (getHeight() / 2.0f), ((getY() + border) + (getHeight() / 2.0f)) - (getWidth() / 2.0f), getHeight() - (border * 2.0f), getWidth() - (border * 2.0f));
            sprite.setOriginCenter();
            sprite.setRotation(90.0f);
            return;
        }
        sprite.setBounds(getX() + border, getY() + border, getWidth() - (border * 2.0f), getHeight() - (border * 2.0f));
    }

    /* access modifiers changed from: protected */
    public void drawBorder(Batch batch, float alpha) {
        batch.setColor(borderColor);
        MiscUtils.setBatchAlpha(batch, alpha);
        batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);
    }

    /* access modifiers changed from: protected */
    public void drawSelection(Batch batch, float alpha) {
        float size = selectionWidth;
        if (this.disabled) {
            batch.setColor(0.7f, 0.7f, 0.7f, 1.0f);
        }
        MiscUtils.setBatchAlpha(batch, alpha);
        String region = this.selected ? "slider-knob-down" : "slider-knob";
        if (!this.vertical) {
            batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion(region), (this.selection * (getWidth() - size)) + getX(), getY() - margin, size, (margin * 2.0f) + getHeight());
        } else {
            batch.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion(region), getX() - margin, getY() + (this.selection * (getHeight() - size)), getWidth() + (margin * 2.0f), size);
        }
    }
}
