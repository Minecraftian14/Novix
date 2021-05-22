package io.anuke.novix.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ObjectSet;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.ColorUtils;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.anuke.novix.modules.Core;
import io.anuke.ucore.graphics.Hue;
import io.anuke.utools.MiscUtils;

/* renamed from: io.anuke.novix.scene.ColorWidget */
public class ColorWidget extends VisTable {
    public static final int palettewidth = 16;
    ColorBox currentBox;
    boolean expandPalette;
    HueBar hbar;
    VisTextButton hexbutton;
    ColorBox lastBox;
    Color lastColor;
    VisImageButton lock;
    Cell<?> padCell;
    ColorBox[] recentColors;
    ColorBar sbar;
    Color selectedColor;
    ObjectSet<Color> usedColors;
    ColorBar vbar;

    public ColorWidget() {
        this(true);
    }

    public ColorWidget(boolean expandPalette2) {
        this.usedColors = new ObjectSet<>();
        this.expandPalette = expandPalette2;
        setupUI();
    }

    public void setupUI() {
        float s = MiscUtils.densityScale();
        float width = (((float) Gdx.graphics.getWidth()) - (BarActor.selectionWidth * 2.0f)) - (70.0f * s);
        float height = 60.0f * s;
        float spacing = 14.0f * s;
        this.hexbutton = new VisTextButton("");
        this.hexbutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                /*
                AndroidTextFieldDialog dialog = new AndroidTextFieldDialog();
                dialog.setConfirmButtonLabel("OK").setText(ColorWidget.this.hexbutton.getText());
                dialog.setCancelButtonLabel("Cancel");
                dialog.setMaxLength(7);
                dialog.setTextPromptListener(new AndroidTextFieldDialog.TextPromptListener() {
                    public void confirm(String text) {
                        try {
                            ColorWidget.this.setSelectedColor(Color.valueOf(text));
                            VisTextButton visTextButton = ColorWidget.this.hexbutton;
                            if (!text.startsWith("#")) {
                                text = "#" + text;
                            }
                            visTextButton.setText(text);
                        } catch (Exception e) {
                            Gdx.app.postRunnable(new Runnable() {
                                public void run() {
                                    new DialogClasses.InfoDialog("Error", "Invalid hex code!").show(ColorWidget.this.getStage());
                                }
                            });
                        }
                    }
                });
                dialog.show();
                */

                System.out.println("NOTIFICATION: Not Implemented");
            }
        });
        this.hbar = new HueBar() {
            public void onSelectionUpdated() {
                ColorWidget.this.sbar.setRightColor(getSelectedColor());
                ColorWidget.this.vbar.setRightColor(getSelectedColor());
                ColorWidget.this.vbar.setRightColor(ColorWidget.this.sbar.getSelectedColor());
                ColorWidget.this.internalColorChanged();
            }
        };
        this.hbar.setSize(width, height);
        this.sbar = new ColorBar() {
            public void onSelectionUpdated() {
                ColorWidget.this.vbar.setRightColor(getSelectedColor());
                ColorWidget.this.internalColorChanged();
            }
        };
        this.sbar.setSize(width, height);
        this.sbar.setColors(Color.WHITE, Color.RED);
        this.vbar = new ColorBar() {
            public void onSelectionUpdated() {
                ColorWidget.this.sbar.brightness = this.selection;
                ColorWidget.this.hbar.brightness = this.selection;
                ColorWidget.this.internalColorChanged();
            }
        };
        this.vbar.setSize(width, height);
        this.vbar.setColors(Color.BLACK, Color.RED);
        top().center().add(this.hbar).padTop(spacing / 2.0f).row();
        add(this.sbar).padTop(spacing).row();
        add(this.vbar).padTop(spacing);
        row();
        Table colordisplay = new VisTable();
        add(colordisplay).expand().fill().padTop(10.0f * s);
        this.lastBox = new ColorBox();
        this.currentBox = new ColorBox();
        this.lastBox.addListener(new BoxListener(this.lastBox));
        final Table colors = new VisTable();
        VisImageButton.VisImageButtonStyle style = new VisImageButton.VisImageButtonStyle((VisImageButton.VisImageButtonStyle) VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class));
        style.imageChecked = VisUI.getSkin().getDrawable("icon-lock");
        style.imageUp = VisUI.getSkin().getDrawable("icon-lock-open");
        style.up = null;
        style.down = null;
        style.over = null;
        style.checked = null;
        this.lock = new VisImageButton(style);
        this.lock.setChecked(Core.instance.prefs.getBoolean("lock"));
        this.lock.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                ColorWidget.this.hbar.setDisabled(ColorWidget.this.lock.isChecked());
                ColorWidget.this.sbar.setDisabled(ColorWidget.this.lock.isChecked());
                ColorWidget.this.vbar.setDisabled(ColorWidget.this.lock.isChecked());
                ColorWidget.this.hexbutton.setDisabled(ColorWidget.this.lock.isChecked());
                colors.setTouchable(ColorWidget.this.lock.isChecked() ? Touchable.disabled : Touchable.childrenOnly);
                colors.setColor(ColorWidget.this.lock.isChecked() ? new Color(1.0f, 1.0f, 1.0f, 0.5f) : Color.WHITE);
                Core.instance.prefs.put("lock", ColorWidget.this.lock.isChecked());
            }
        });
        this.lock.fire(new ChangeListener.ChangeEvent());
        this.lock.getImageCell().size(48.0f * s);
        colordisplay.add().size(70.0f * s);
        colordisplay.add(this.currentBox).size(70.0f * s);
        colordisplay.add(this.lock).size(70.0f * s);
        row();
        add(this.hexbutton).padBottom(5.0f * s).padTop(5.0f * s).minWidth(150.0f * s).height(50.0f * s).top();
        row();
        this.padCell = add();
        row();
        this.recentColors = new ColorBox[16];
        add(colors).expand().fill();
        float size = (float) ((Gdx.graphics.getWidth() / 8) - 4);
        for (int x = 0; x < 16; x++) {
            ColorBox box = new ColorBox();
            box.setDisabled(true);
            this.recentColors[x] = box;
            box.addListener(new BoxListener(box));
            if (x % 8 == 0) {
                colors.row();
            }
            colors.add(box).size(size);
        }
    }

    public Cell<?> getPadCell() {
        return this.padCell;
    }

    public boolean isLocked() {
        return this.lock.isChecked();
    }

    /* renamed from: io.anuke.novix.scene.ColorWidget$BoxListener */
    private class BoxListener extends InputListener {
        private ColorBox box;

        public BoxListener(ColorBox box2) {
            this.box = box2;
        }

        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            this.box.hovered = true;
            this.box.setZIndex(999);
            return true;
        }

        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            ColorWidget.this.setNewColor(this.box.getColor());
            this.box.hovered = false;
        }
    }

    /* access modifiers changed from: private */
    public void internalColorChanged() {
        this.selectedColor = Hue.fromHSB(this.hbar.selection, this.sbar.selection, this.vbar.selection);
        this.currentBox.setColor(this.selectedColor);
        this.hexbutton.setText("#" + this.selectedColor.toString().substring(0, 6));
        onColorChanged();
    }

    public void onColorChanged() {
    }

    public ColorBox[] getRecentColors() {
        return this.recentColors;
    }

    public void setRecentColors(Color... colors) {
        int i = 0;
        while (i < 16 && i < colors.length) {
            this.recentColors[i].setColor(colors[i]);
            this.recentColors[i].setDisabled(false);
            i++;
        }
    }

    public void setRecentColors(ColorBox... colors) {
        int i = 0;
        while (i < 16 && i < colors.length) {
            if (!colors[i].isDisabled()) {
                this.usedColors.add(colors[i].getColor());
                this.recentColors[i].setColor(colors[i].getColor());
                this.recentColors[i].setDisabled(false);
            }
            i++;
        }
    }

    public Color getSelectedColor() {
        return this.selectedColor.cpy();
    }

    public void pushPalette() {
        addRecentColor(this.selectedColor);
    }

    public void addRecentColor(Color color) {
        if (!this.usedColors.contains(color)) {
            for (int i = 14; i >= 0; i--) {
                this.recentColors[i + 1].setColor(this.recentColors[i].getColor());
                this.recentColors[i + 1].setDisabled(this.recentColors[i].isDisabled());
            }
            this.recentColors[0].setColor(color);
            this.usedColors.add(color);
        }
    }

    public void setSelectedColor(Color color) {
        if (this.selectedColor != null && this.expandPalette) {
            pushPalette();
        }
        this.lastColor = color.cpy();
        this.lastBox.setColor(color);
        setNewColor(color);
    }

    /* access modifiers changed from: private */
    public void setNewColor(Color color) {
        int[] ints = ColorUtils.RGBtoHSV(color);
        this.hbar.selection = ((float) ints[0]) / 360.0f;
        this.sbar.selection = ((float) ints[1]) / 100.0f;
        this.vbar.selection = ((float) ints[2]) / 100.0f;
        this.hbar.onSelectionUpdated();
        this.sbar.onSelectionUpdated();
        this.vbar.onSelectionUpdated();
        this.currentBox.setColor(color);
    }
}
