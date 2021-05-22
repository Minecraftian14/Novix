package io.anuke.novix.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.anuke.novix.modules.Core;
import io.anuke.ucore.UCore;

/* renamed from: io.anuke.novix.ui.SettingsMenu */
public class SettingsMenu extends DialogClasses.BaseDialog {
    /* access modifiers changed from: private */
    public Core main;

    public SettingsMenu(Core main2) {
        super("Settings");
        this.main = main2;
        addTitleSeperator();
        getContentTable().add().height(20.0f).row();
        VisTextButton back = new VisTextButton("Back");
        back.add(new Image(VisUI.getSkin().getDrawable("icon-arrow-left"))).size(UCore.f331s * 40.0f).center();
        back.getCells().reverse();
        back.getLabelCell().padRight(UCore.f331s * 40.0f);
        getButtonsTable().add(back).width((float) Gdx.graphics.getWidth()).height(60.0f * UCore.f331s);
        setObject(back, false);
    }

    public void addScrollSetting(final String name, int min, int max, int value) {
        final VisLabel label = new VisLabel(String.valueOf(name) + ": " + this.main.prefs.getInteger(name, value));
        final VisSlider slider = new VisSlider((float) min, (float) max, 1.0f, false);
        slider.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                label.setText(String.valueOf(name) + ": " + slider.getValue());
                SettingsMenu.this.main.prefs.put(name, (int) slider.getValue());
            }
        });
        DialogClasses.scaleSlider(slider);
        slider.setValue((float) this.main.prefs.getInteger(name));
        Table table = getContentTable();
        table.top().left().add(label).align(8);
        table.row();
        table.add(slider).width(200.0f * UCore.f331s).padBottom(40.0f * UCore.f331s);
        table.row();
    }

    public void addPercentScrollSetting(final String name) {
        final VisLabel label = new VisLabel();
        final VisSlider slider = new VisSlider(0.0f, 2.0f, 0.01f, false) {
            public float getPrefHeight() {
                return 50.0f * UCore.f331s;
            }
        };
        DialogClasses.scaleSlider(slider);
        slider.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                label.setText(String.valueOf(name) + ": [SKY]" + ((int) (slider.getValue() * 100.0f)) + "%");
                SettingsMenu.this.main.prefs.put(SettingsMenu.this.convert(name), slider.getValue());
            }
        });
        slider.setValue(this.main.prefs.getFloat(convert(name), 1.0f));
        slider.fire(new ChangeListener.ChangeEvent());
        Table table = getContentTable();
        table.top().left().add(label).align(8);
        table.row();
        table.add(slider).width(300.0f * UCore.f331s);
        table.row();
        table.add(new Separator()).colspan(1).padTop(10.0f * UCore.f331s).padBottom(5.0f * UCore.f331s).growX().row();
    }

    public void addCheckSetting(final String name, boolean value) {
        final VisLabel label = new VisLabel(name);
        final VisCheckBox box = new VisCheckBox("", this.main.prefs.getBoolean(convert(name), value));
        Table row = new VisTable();
        row.left();
        box.getImageStackCell().size(40.0f * UCore.f331s);
        box.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                SettingsMenu.this.main.prefs.put(SettingsMenu.this.convert(name), box.isChecked());
                label.setText(String.valueOf(name) + ": " + (box.isChecked() ? "[CORAL]On" : "[PURPLE]Off"));
            }
        });
        box.fire(new ChangeListener.ChangeEvent());
        Table table = getContentTable();
        row.top().left().add(label).align(8);
        row.add(box).padLeft(150.0f * UCore.f331s);
        table.add(row).padTop(UCore.f331s * 5.0f).padBottom(UCore.f331s * 5.0f).align(8);
        table.row();
        table.add(new Separator()).colspan(1).padTop(10.0f * UCore.f331s).growX().row();
    }

    public void addButton(String name, ChangeListener listener) {
        VisTextButton button = new VisTextButton(name);
        button.addListener(listener);
        Table table = getContentTable();
        table.top().left().add(button).size(200.0f * UCore.f331s, 60.0f * UCore.f331s).padTop(40.0f * UCore.f331s).align(8);
        table.add().align(8);
    }

    public VisDialog show(Stage stage) {
        super.show(stage);
        int i = Gdx.app.getType() == Application.ApplicationType.Desktop ? 0 : 1;
        setSize(stage.getWidth(), stage.getHeight() - ((float) i));
        setY((float) i);
        return this;
    }

    /* access modifiers changed from: private */
    public String convert(String name) {
        return name.replace(" ", "").toLowerCase();
    }

    public void result(Object o) {
        this.main.prefs.save();
    }
}
