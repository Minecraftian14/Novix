package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.kotcrab.vis.ui.VisUI;
import io.anuke.ucore.graphics.Textures;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.BrushSizeWidget */
public class BrushSizeWidget extends Widget {
    int brushsize = 1;
    int gridsize = 9;

    public BrushSizeWidget() {
        setSize(((float) (this.gridsize * 15)) * MiscUtils.densityScale(), ((float) (this.gridsize * 15)) * MiscUtils.densityScale());
        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                BrushSizeWidget.this.updateSize(x, y);
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                BrushSizeWidget.this.updateSize(x, y);
            }
        });
    }

    public void draw(Batch batch, float alpha) {
        batch.draw(Textures.get("alpha"), getX(), getY(), getWidth(), getHeight(), 0.0f, 0.0f, (float) this.gridsize, (float) this.gridsize);
        batch.setColor(getColor());
        for (int x = 0; x < this.gridsize; x++) {
            for (int y = 0; y < this.gridsize; y++) {
                if (Vector2.dst((float) (this.gridsize / 2), (float) (this.gridsize / 2), (float) x, (float) y) < ((float) this.brushsize) - 0.5f) {
                    Batch batch2 = batch;
                    batch2.draw((TextureRegion) VisUI.getSkin().getAtlas().findRegion("white"), (((float) x) * blockSize()) + getX(), (((float) y) * blockSize()) + getY(), blockSize(), blockSize());
                }
            }
        }
        batch.setColor(Color.WHITE);
        batch.draw(Textures.get("grid_25"), getX(), getY(), getWidth(), getHeight(), 0.0f, 0.0f, (float) this.gridsize, (float) this.gridsize);
    }

    /* access modifiers changed from: private */
    public void updateSize(float x, float y) {
    }

    public int getBrushSize() {
        return this.brushsize;
    }

    public void setBrushSize(int size) {
        this.brushsize = size;
    }

    public float blockSize() {
        return getWidth() / ((float) this.gridsize);
    }

    public float getPrefWidth() {
        return getWidth();
    }

    public float getPrefHeight() {
        return getHeight();
    }
}
