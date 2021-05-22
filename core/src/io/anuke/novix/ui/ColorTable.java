package io.anuke.novix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.anuke.novix.modules.Core;
import io.anuke.novix.scene.CollapseButton;
import io.anuke.novix.scene.ColorBox;
import io.anuke.novix.scene.ColorWidget;
import io.anuke.novix.scene.SmoothCollapsibleWidget;
import io.anuke.ucore.UCore;
import io.anuke.utools.MiscUtils;
import io.anuke.utools.SceneUtils;

/* renamed from: io.anuke.novix.ui.ColorTable */
public class ColorTable extends VisTable {
    /* access modifiers changed from: private */
    public ColorBox[] boxes;
    /* access modifiers changed from: private */
    public CollapseButton collapsebutton;
    /* access modifiers changed from: private */
    public SmoothCollapsibleWidget collapser;
    private VisTable colortable = new VisTable();
    /* access modifiers changed from: private */
    public int paletteColor;
    private PaletteMenu palettemenu;
    /* access modifiers changed from: private */
    public ColorWidget picker;

    public ColorTable(final Core c) {
        background("button-window-bg");
        this.palettemenu = new PaletteMenu(c);
        this.picker = new ColorWidget() {
            public void onColorChanged() {
                if (c.colormenu != null) {
                    ColorTable.this.updateSelectedColor(ColorTable.this.picker.getSelectedColor());
                }
            }
        };
        this.colortable.setName("colortable");
        this.colortable.setFillParent(true);
        c.stage.addActor(this.colortable);
        this.colortable.top().left();
        this.collapsebutton = new CollapseButton();
        this.collapsebutton.setName("colorcollapsebutton");
        this.collapsebutton.flip();
        this.collapser = new SmoothCollapsibleWidget(this);
        c.stage.addActor(this.collapser);
        this.collapsebutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (!ColorTable.this.collapser.isCollapsed()) {
                    ColorTable.this.picker.setSelectedColor(ColorTable.this.picker.getSelectedColor());
                    c.tool().onColorChange(c.selectedColor(), c.drawgrid.canvas);
                }
                ColorTable.this.collapser.setCollapsed(!ColorTable.this.collapser.isCollapsed());
                ColorTable.this.collapsebutton.flip();
                if (!c.toolMenuCollapsed() && event != null) {
                    c.toolmenu.collapse();
                }
            }
        });
        updateColorMenu();
        VisTextButton palettebutton = new VisTextButton("Palettes...");
        palettebutton.setName("palettebutton");
        SceneUtils.addIconToButton(palettebutton, new VisImage("icon-palette"), 42.0f * UCore.s);
        palettebutton.getLabelCell().expand(false, false);
        palettebutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ColorTable.this.openPaletteMenu();
            }
        });
        add().grow().row();
        this.picker.pack();
        Cell<?> cell = add(this.picker).expand().fill().padBottom(UCore.s * 10.0f).padTop(0.0f).padBottom(20.0f * UCore.s);
        row();
        center().add(palettebutton).align(1).padBottom(UCore.s * 10.0f).height(60.0f * UCore.s).growX();
        this.collapser.setY(Core.i.toolmenu.getButton().getTop());
        this.collapser.toBack();
        this.collapser.resetY();
        Vector2 pos = this.picker.localToStageCoordinates(new Vector2());
        float tpad = (((((float) Gdx.graphics.getHeight()) - ((pos.y + this.picker.getPrefHeight()) + (90.0f * UCore.s))) - this.collapsebutton.getPrefHeight()) - (65.0f * UCore.s)) / 2.0f;
        cell.padTop((((float) Gdx.graphics.getHeight()) - (pos.y + this.picker.getPrefHeight())) - this.collapsebutton.getPrefHeight());
        cell.padBottom(tpad / 2.0f);
        this.picker.getPadCell().padTop(tpad / 2.0f);
        this.picker.invalidateHierarchy();
        pack();
        this.collapser.setCollapsed(true, false);
        setupBoxColors();
    }

    public float getBoxBorder() {
        return (float) this.boxes[0].getBorderThickness();
    }

    public void collapse() {
        ((ClickListener) this.collapsebutton.getListeners().get(2)).clicked((InputEvent) null, 0.0f, 0.0f);
    }

    public boolean collapsed() {
        return this.collapser.isCollapsed();
    }

    public void openPaletteMenu() {
        this.palettemenu.update();
        this.palettemenu.show(Core.i.stage);
    }

    public void setSelectedColor(int color) {
        ((ClickListener) Core.i.colormenu.boxes[color].getListeners().get(0)).clicked((InputEvent) null, 0.0f, 0.0f);
    }

    public void addRecentColor(Color color) {
        this.picker.addRecentColor(color);
    }

    public Color getSelectedColor() {
        return Core.i.getCurrentPalette().colors[this.paletteColor];
    }

    public void resetPaletteColor() {
        this.paletteColor = 0;
    }

    public ColorBox[] getRecentColors() {
        return this.picker.getRecentColors();
    }

    public int getPaletteColor() {
        return this.paletteColor;
    }

    public ColorBox getSelectedBox() {
        return this.boxes[this.paletteColor];
    }

    public void updateColorMenu() {
        int i;
        this.colortable.clear();
        int maxcolorsize = (int) (65.0f * UCore.s);
        int mincolorsize = (int) (30.0f * UCore.s);
        int perow = 0;
        int colorsize = Math.min(maxcolorsize, (Gdx.graphics.getWidth() / Core.i.getCurrentPalette().size()) - MiscUtils.densityScale(3));
        if (colorsize < mincolorsize) {
            colorsize = mincolorsize;
            perow = Gdx.graphics.getWidth() / colorsize;
        }
        Cell fillX = this.colortable.add(this.collapsebutton).expandX().fillX();
        if (perow == 0) {
            i = Core.i.getCurrentPalette().size();
        } else {
            i = perow;
        }
        fillX.colspan(i + 2).height(50.0f * UCore.s);
        this.collapsebutton.setZIndex(this.collapser.getZIndex() + 10);
        this.colortable.row();
        this.colortable.add().growX();
        this.boxes = new ColorBox[Core.i.getCurrentPalette().size()];
        for (int i2 = 0; i2 < Core.i.getCurrentPalette().size(); i2++) {
            final int index = i2;
            final ColorBox box = new ColorBox();
            this.boxes[i2] = box;
            this.colortable.add(box).size((float) colorsize);
            box.setColor(Core.i.getCurrentPalette().colors[i2]);
            box.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    ColorTable.this.picker.addRecentColor(ColorTable.this.boxes[ColorTable.this.paletteColor].getColor().cpy());
                    ColorTable.this.boxes[ColorTable.this.paletteColor].selected = false;
                    ColorTable.this.paletteColor = index;
                    Core.i.prefs.put("palettecolor", ColorTable.this.paletteColor);
                    box.selected = true;
                    box.toFront();
                    ColorTable.this.setSelectedColor(box.getColor());
                }
            });
            if (perow != 0 && i2 % perow == perow - 1) {
                this.colortable.add().growX();
                this.colortable.row();
                this.colortable.add().growX();
            }
        }
        if (perow == 0) {
            this.colortable.add().growX();
        }
    }

    public void updateSelectedColor(Color color) {
        this.boxes[this.paletteColor].setColor(color.cpy());
        Core.i.getCurrentPalette().colors[this.paletteColor] = color.cpy();
        Core.i.toolmenu.updateColor(color.cpy());
        Core.i.updateToolColor();
    }

    public void setSelectedColor(Color color) {
        updateSelectedColor(color);
        this.picker.setSelectedColor(color);
        Core.i.updateToolColor();
    }

    public void setupBoxColors() {
        this.paletteColor = Core.i.prefs.getInteger("palettecolor", 0);
        for (ColorBox box : this.boxes) {
            box.getColor().a = 1.0f;
        }
        if (this.paletteColor > this.boxes.length) {
            this.paletteColor = 0;
        }
        this.picker.setRecentColors(this.boxes);
        this.boxes[this.paletteColor].selected = true;
        this.boxes[this.paletteColor].toFront();
        this.picker.setSelectedColor(Core.i.getCurrentPalette().colors[this.paletteColor]);
    }

    public float getPrefWidth() {
        return (float) Gdx.graphics.getWidth();
    }
}
