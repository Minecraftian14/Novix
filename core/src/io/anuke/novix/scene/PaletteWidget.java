package io.anuke.novix.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import io.anuke.novix.graphics.Palette;
import io.anuke.novix.ui.DialogClasses;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Hue;

/* renamed from: io.anuke.novix.scene.PaletteWidget */
public class PaletteWidget extends VisTable {
    public static final float defaultHeight = (140.0f * UCore.s);
    public static final float defaultWidth = (300.0f * UCore.s);
    public VisImageButton extrabutton;
    public final Palette palette;
    private boolean selected;

    public PaletteWidget(Palette palette2, boolean selected2) {
        this.palette = palette2;
        this.selected = selected2;
        setup();
    }

    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (this.selected) {
            batch.setColor(1.0f, 1.0f, 1.0f, parentAlpha);
            getSkin().getDrawable("border").draw(batch, getX(), getY(), getWidth(), getHeight());
        }
    }

    private void setup() {
        float maxsize = 36.0f * UCore.s;
        setColor(Hue.lightness(0.87f));
        top().left();
        VisLabel label = new VisLabel(this.palette.name);
        label.setColor(Color.LIGHT_GRAY);
        add(label).align(8);
        this.extrabutton = new VisImageButton(VisUI.getSkin().getDrawable("icon-dots"));
        this.extrabutton.setStyle(new VisImageButton.VisImageButtonStyle(this.extrabutton.getStyle()));
        this.extrabutton.getStyle().down = this.extrabutton.getStyle().up;
        this.extrabutton.getStyle().over = this.extrabutton.getStyle().up;
        this.extrabutton.getStyle().imageDown = VisUI.getSkin().getDrawable("icon-dots-down");
        this.extrabutton.setColor(getColor());
        this.extrabutton.getImageCell().size(44.0f * UCore.s);
        this.extrabutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        add(this.extrabutton).size(46.0f * UCore.s).align(18).padRight(0.0f);
        row();
        top().left().add(generatePaletteTable(maxsize, getPrefWidth(), this.palette.colors)).grow().colspan(2).padTop(5.0f);
        setSelected(this.selected);
        DialogClasses.BaseDialog.addPadding(this);
    }

    public void setSelected(boolean selected2) {
        this.selected = selected2;
        background(selected2 ? "button-over" : "button");
        this.extrabutton.getStyle().up = selected2 ? this.extrabutton.getStyle().checked : ((VisImageButton.VisImageButtonStyle) VisUI.getSkin().get(VisImageButton.VisImageButtonStyle.class)).up;
        this.extrabutton.getStyle().over = this.extrabutton.getStyle().up;
        this.extrabutton.getStyle().down = this.extrabutton.getStyle().up;
        setTouchable(selected2 ? Touchable.childrenOnly : Touchable.enabled);
    }

    public void addExtraButtonListener(EventListener listener) {
        this.extrabutton.addListener(listener);
    }

    public static Table generatePaletteTable(float size, float width, Color[] colors) {
        VisTable table = new VisTable();
        ColorBox[] boxes = new ColorBox[colors.length];
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new ColorBox(colors[i]);
        }
        float rowsize = width / ((float) boxes.length);
        int perow = (int) (width / size);
        table.top().left();
        if (rowsize < size) {
            for (int i2 = 0; i2 < boxes.length; i2++) {
                table.add(boxes[i2]).size(size);
                if (i2 % perow == perow - 1) {
                    table.row();
                }
            }
        } else {
            for (ColorBox add : boxes) {
                table.add(add).size(size);
            }
            for (int i3 = 0; i3 < perow - boxes.length; i3++) {
                table.add().size(size);
            }
        }
        table.row();
        table.add().height(5.0f);
        return table;
    }

    public float getPrefWidth() {
        return defaultWidth;
    }

    public float getPrefHeight() {
        return defaultHeight;
    }
}
