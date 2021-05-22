package io.anuke.novix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import java.util.Arrays;

import io.anuke.novix.graphics.Palette;
import io.anuke.novix.modules.Core;
import io.anuke.novix.scene.PaletteWidget;
import io.anuke.novix.scene.TallMenuItem;
import io.anuke.ucore.UCore;
import io.anuke.utools.SceneUtils;

/* renamed from: io.anuke.novix.ui.PaletteMenu */
public class PaletteMenu extends DialogClasses.BaseDialog {
    /* access modifiers changed from: private */
    public PaletteWidget currentWidget = null;
    /* access modifiers changed from: private */
    public Core main;

    public PaletteMenu(Core main2) {
        super("Palettes");
        this.main = main2;
        setMovable(false);
        addCloseButton();
        setStage(main2.stage);
    }

    public void update() {
        float scrolly;
        boolean z;
        if (getContentTable().getChildren().size == 0) {
            scrolly = 0.0f;
        } else {
            scrolly = ((ScrollPane) getContentTable().getChildren().first()).getScrollPercentY();
        }
        getContentTable().clearChildren();
        getButtonsTable().clearChildren();
        VisTable palettetable = new VisTable();
        final VisScrollPane pane = new VisScrollPane(palettetable);
        pane.setFadeScrollBars(false);
        pane.setOverscroll(false, false);
        getContentTable().add(pane).left().grow().maxHeight((float) (Gdx.graphics.getHeight() / 2));
        for (final Palette palette : this.main.palettemanager.getPalettes()) {
            if (palette == this.main.getCurrentPalette()) {
                z = true;
            } else {
                z = false;
            }
            final PaletteWidget widget = new PaletteWidget(palette, z);
            if (this.main.getCurrentPalette() == palette) {
                this.currentWidget = widget;
            }
            widget.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    if (!widget.extrabutton.isPressed() && PaletteMenu.this.currentWidget != widget) {
                        PaletteMenu.this.currentWidget.setSelected(false);
                        PaletteMenu.this.currentWidget = widget;
                        widget.setSelected(true);
                        PaletteMenu.this.main.setPalette(palette);
                    }
                }
            });
            widget.addExtraButtonListener(new ClickListener() {
                Palette palette = widget.palette;
//                PaletteWidget widget;

                public void clicked(InputEvent event, float x, float y) {
                    PopupMenu menu = new PopupMenu();
                    menu.addItem(new TallMenuItem("Resize", new ChangeListener() {
                        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                            new DialogClasses.NumberInputDialog("Resize Palette", new StringBuilder(String.valueOf(palette.size())).toString(), "Size: ") {
                                public void result(int size) {
                                    Color[] newcolors = new Color[size];
                                    Arrays.fill(newcolors, Color.WHITE.cpy());
                                    int i = 0;
                                    while (i < size && i < palette.size()) {
                                        newcolors[i] = palette.colors[i];
                                        i++;
                                    }
                                    palette.colors = newcolors;
                                    PaletteMenu.this.update();
                                    PaletteMenu.this.main.colormenu.updateColorMenu();
                                }
                            }.show(PaletteMenu.this.getStage());
                        }
                    }));
                    menu.addItem(new TallMenuItem("Rename", new ChangeListener() {
                        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                            new DialogClasses.InputDialog("Rename Palette", palette.name, "Name: ") {
                                public void result(String string) {
                                    palette.name = string;
                                    PaletteMenu.this.update();
                                }
                            }.show(PaletteMenu.this.getStage());
                        }
                    }));
                    menu.addItem(new TallMenuItem("Delete", new ChangeListener() {
                        public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                            if (widget != PaletteMenu.this.currentWidget) {
                                new DialogClasses.ConfirmDialog("Delete Palette", "Are you sure you want\nto delete this palette?") {
                                    public void result() {
                                        PaletteMenu.this.main.palettemanager.removePalette(palette);
                                        PaletteMenu.this.update();
                                    }
                                }.show(PaletteMenu.this.getStage());
                            } else {
                                DialogClasses.showInfo(PaletteMenu.this.getStage(), "You cannot delete the\npalette you are using!");
                            }
                        }
                    }));
                    Vector2 coords = widget.extrabutton.localToStageCoordinates(new Vector2());
                    menu.showMenu(PaletteMenu.this.getStage(), (coords.x - menu.getWidth()) + widget.extrabutton.getWidth(), coords.y);
                }
            });
            palettetable.add(widget).padBottom(6.0f * UCore.s);
            palettetable.row();
        }
        VisTextButton backbutton = new VisTextButton("Back");
        backbutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                PaletteMenu.this.hide();
            }
        });
        VisTextButton addpalettebutton = new VisTextButton("New Palette");
        addpalettebutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                new DialogClasses.InputDialog("New Palette", "", "Name:") {
                    protected VisCheckBox box;
                    protected VisTextField numberfield = new VisTextField("8");

                    {
                        this.numberfield.setTextFieldFilter(new VisTextField.TextFieldFilter.DigitsOnlyFilter());
                        this.box = new VisCheckBox("Auto-Generate", PaletteMenu.this.main.prefs.getBoolean("genpalettes", true));
                        this.box.getImageStackCell().size(UCore.s * 40.0f);
                        getContentTable().center();
                        getContentTable().row();
                        getContentTable().add(new VisLabel("Size:")).padTop(0.0f).padBottom(UCore.s * 20.0f);
                        getContentTable().add(this.numberfield).pad(UCore.s * 20.0f).padBottom(UCore.s * 20.0f).padLeft(0.0f).padTop(0.0f).size(160.0f * UCore.s, UCore.s * 40.0f).row();
                        getContentTable().add(this.box).colspan(2).padBottom(25.0f * UCore.s);
                        new SceneUtils.TextFieldEmptyListener(this.f326ok, this.textfield, this.numberfield);
                    }

                    public void result(String string) {
                        if (Integer.parseInt(this.numberfield.getText()) > 32) {
                            DialogClasses.showInfo(getStage(), "A palette may not have\nmore than 32 colors.");
                            return;
                        }
                        PaletteMenu.this.main.palettemanager.addPalette(new Palette(string, PaletteMenu.this.main.palettemanager.generatePaletteID(), Integer.parseInt(this.numberfield.getText()), this.box.isChecked()));
                        PaletteMenu.this.update();
                        PaletteMenu.this.main.prefs.put("genpalettes", this.box.isChecked());
                    }
                }.show(PaletteMenu.this.getStage());
            }
        });
        SceneUtils.addIconToButton(addpalettebutton, new Image(VisUI.getSkin().getDrawable("icon-plus")), UCore.s * 40.0f);
        SceneUtils.addIconToButton(backbutton, new Image(VisUI.getSkin().getDrawable("icon-arrow-left")), UCore.s * 40.0f);
        getButtonsTable().add(backbutton).size(150.0f * UCore.s, UCore.s * 50.0f).padRight(UCore.s);
        getButtonsTable().add(addpalettebutton).size(200.0f * UCore.s, UCore.s * 50.0f);
        pack();
        this.main.stage.setScrollFocus(pane);
        pane.setSmoothScrolling(false);
        pane.setScrollPercentY(scrolly);
        pane.addAction(Actions.sequence(Actions.delay(0.01f), new Action() {
            public boolean act(float delta) {
                pane.setSmoothScrolling(true);
                return true;
            }
        }));
        centerWindow();
    }

    public VisDialog show(Stage stage) {
        super.show(stage);
        stage.setScrollFocus(getContentTable().getChildren().first());
        return this;
    }
}
