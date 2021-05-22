package io.anuke.novix.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTextButton;
import io.anuke.novix.modules.Core;
import io.anuke.novix.modules.Tutorial;
import io.anuke.novix.scene.CollapseButton;
import io.anuke.novix.scene.ColorBar;
import io.anuke.novix.scene.SmoothCollapsibleWidget;
import io.anuke.novix.ui.ProjectMenu;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.ShapeUtils;

/* renamed from: io.anuke.novix.tools.TutorialStage */
public enum TutorialStage {
    colormenu {
        /* access modifiers changed from: protected */
        public void draw() {
            CollapseButton button = (CollapseButton) find("colorcollapsebutton");
            button.draw(this.batch, 1.0f);
            shade(0.0f, 0.0f, (float) Gdx.graphics.getWidth(), ((float) Gdx.graphics.getHeight()) - button.getHeight());
            color(Color.WHITE);
            text((float) (width / 2), (float) ((height / 2) - 10), "To start, tap\non the top menu.", 1);
            color(select);
            rect(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            arrow((float) (width / 2), (float) (height / 2), ((float) (height / 2)) - (button.getHeight() * 1.5f), true);
            if (!Core.i.colorMenuCollapsed()) {
                next();
            }
        }
    },
    colors {
        /* access modifiers changed from: protected */
        public void draw() {
            CollapseButton button = (CollapseButton) find("colorcollapsebutton");
            shade(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            shade(0.0f, 0.0f, (float) Gdx.graphics.getWidth(), find("toolcollapsebutton").getTop());
            Core.i.getSelectedBox().draw(this.batch, 1.0f);
            color(Color.WHITE);
            text((float) (width / 2), ((float) height) - (120.0f * UCore.s), "This is this the color menu.\nHere you can edit the color palette.");
            color(Color.PURPLE);
            text((float) (width / 2), (float) (height / 2), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    colors2 {
        /* access modifiers changed from: protected */
        public void draw() {
            CollapseButton button = (CollapseButton) find("colorcollapsebutton");
            shade(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            shade(0.0f, 0.0f, (float) Gdx.graphics.getWidth(), find("toolcollapsebutton").getTop());
            color(Color.WHITE);
            Core.i.getSelectedBox().draw(this.batch, 1.0f);
            text((float) (width / 2), ((float) height) - (70.0f * UCore.s), "< Tap any color in this bar to edit it. >");
            color(Color.PURPLE);
            text((float) (width / 2), (float) (height / 2), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    colors3 {
        /* access modifiers changed from: protected */
        public void draw() {
            CollapseButton button = (CollapseButton) find("colorcollapsebutton");
            VisTextButton palettebutton = (VisTextButton) find("palettebutton");
            palettebutton.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
            shade(button.getX(), button.getY(), button.getWidth(), button.getHeight());
            shade(0.0f, 0.0f, (float) Gdx.graphics.getWidth(), find("toolcollapsebutton").getTop());
            color(Color.WHITE);
            text((float) (width / 2), this.temp.y + (palettebutton.getHeight() * 1.5f), "Tap this button to access your palettes.");
            color(select);
            rect(palettebutton);
            if (palettebutton.getClickListener().getTapCount() > 0) {
                next();
            }
            Core.i.getSelectedBox().draw(this.batch, 1.0f);
        }
    },
    palettes {
        /* access modifiers changed from: protected */
        public void draw() {
            color(Color.WHITE);
            text((float) (width / 2), ((float) height) - (90.0f * UCore.s), "You can use this menu to resize,\ndelete and add new palettes.");
            color(Color.PURPLE);
            text((float) (width / 2), 120.0f * UCore.s, "<tap to continue>");
        }

        public void tap(int x, int y) {
            if (Core.i.getCurrentDialog() != null) {
                Core.i.getCurrentDialog().hide();
            }
            Core.i.collapseColorMenu();
            next();
        }
    },
    selectcolors {
        /* access modifiers changed from: protected */
        public void draw() {
            shade(0.0f, 0.0f, (float) width, (float) height);
            color(Color.WHITE);
            text((float) (width / 2), ((float) height) - (55.0f * UCore.s), "< Tap any color in this bar to set>\nit as your current color.");
            color(Color.PURPLE);
            text((float) (width / 2), 120.0f * UCore.s, "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    canvas {
        /* access modifiers changed from: protected */
        public void draw() {
            ColorBar bar = (ColorBar) Core.i.stage.getRoot().findActor("alphabar");
            if (!MathUtils.isEqual(bar.getSelection(), 1.0f)) {
                bar.setSelection(1.0f);
                bar.fire(new ChangeListener.ChangeEvent());
            }
            shade(0.0f, 0.0f, (float) width, find("toolcollapsebutton").getTop());
            float h = colorHeight();
            shade(0.0f, ((float) height) - h, (float) width, h);
            if (Core.i.getCurrentDialog() != null) {
                Core.i.getCurrentDialog().hide();
            }
            if (!Core.i.colorMenuCollapsed()) {
                Core.i.collapseColorMenu();
            }
            if (!Core.i.toolMenuCollapsed()) {
                Core.i.collapseToolMenu();
            }
            Core.i.drawgrid.zoom = 1.0f;
            color(Color.WHITE);
            text((float) (width / 2), (float) (height / 2), "This is the canvas.");
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) - (80.0f * UCore.s), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    canvasmodes {
        /* access modifiers changed from: protected */
        public void draw() {
            shade(0.0f, 0.0f, (float) width, find("toolcollapsebutton").getTop());
            float h = colorHeight();
            shade(0.0f, ((float) height) - h, (float) width, h);
            color(Color.WHITE);
            text((float) (width / 2), ((float) (height / 2)) + (UCore.s * 80.0f), "There are two drawing modes:\n[CORAL]cursor[] and [PURPLE]touch.");
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) - (UCore.s * 80.0f), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    canvascursormode {
        /* access modifiers changed from: protected */
        public void draw() {
            VisImageButton button = (VisImageButton) Core.i.stage.getRoot().findActor("modebutton");
            if (!button.isChecked()) {
                ((ClickListener) button.getListeners().get(0)).clicked((InputEvent) null, 0.0f, 0.0f);
            }
            shade(0.0f, 0.0f, (float) width, find("toolcollapsebutton").getTop());
            float h = colorHeight();
            shade(0.0f, ((float) height) - h, (float) width, h);
            color(Color.WHITE);
            text((float) (width / 2), (float) (height - 15), "The [CORAL]cursor mode[] works like this:\nUse one finger to [PURPLE]move the cursor[]\nand hold another finger anywhere on\nthe screen to[PURPLE] draw[].\n[GREEN]Try it out.");
            color(Color.PURPLE);
            text((float) (width / 2), 70.0f * UCore.s, "<tap here to continue>");
        }

        public void tap(int x, int y) {
            if (((float) y) < 132.0f * UCore.s) {
                next();
            }
        }
    },
    canvastouchmode {
        /* access modifiers changed from: protected */
        public void draw() {
            VisImageButton button = (VisImageButton) Core.i.stage.getRoot().findActor("modebutton");
            if (button.isChecked()) {
                ((ClickListener) button.getListeners().get(0)).clicked((InputEvent) null, 0.0f, 0.0f);
            }
            shade(0.0f, 0.0f, (float) width, find("toolcollapsebutton").getTop());
            float h = colorHeight();
            shade(0.0f, ((float) height) - h, (float) width, h);
            color(Color.WHITE);
            text((float) (width / 2), ((float) height) - (50.0f * UCore.s), "The [PURPLE]touch mode[] is simple:\njust touch the place where you\nwant to draw.\n[GREEN]Try it out.");
            color(Color.PURPLE);
            text((float) (width / 2), 70.0f * UCore.s, "<tap here to continue>");
        }

        public void tap(int x, int y) {
            if (((float) y) < 132.0f * UCore.s) {
                next();
            }
        }
    },
    tools {
        Tool selected;

        /* access modifiers changed from: protected */
        public void draw() {
            if (Core.i.toolMenuCollapsed()) {
                Tool.pencil.button.setChecked(false);
                Core.i.collapseToolMenu();
            }
            float f = ((float) width) / ((float) Tool.values().length);
            shade(0.0f, f, (float) width, ((float) height) - f);
            color(Color.WHITE);
            text((float) (width / 2), ((float) (height / 2)) + (20.0f * UCore.s), "These are the drawing tools you can use.\nTap one of the icons to see what it does.");
            if (this.selected != null) {
                color(select);
                text((float) (width / 2), ((float) (height / 2)) - (80.0f * UCore.s), "This is the " + this.selected.name() + " tool.");
                rect(((float) this.selected.ordinal()) * (0.5f + f), 0.0f, f + 1.0f, f + 1.0f);
            }
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) + (180.0f * UCore.s), "[tap to continue]");
        }

        public void tap(int x, int y) {
            if (this.selected != null) {
                this.selected.button.setChecked(false);
            }
            if (y < width / Tool.values().length) {
                this.selected = Tool.values()[x / (width / Tool.values().length)];
                this.selected.button.setChecked(false);
            }
            if (((float) y) > ((float) (height / 2)) + (40.0f * UCore.s)) {
                Core.i.tool().button.setChecked(true);
                next();
            }
        }
    },
    tooloptions {
        /* access modifiers changed from: protected */
        public void draw() {
            float f = find("toolcollapsebutton").getTop();
            shade(0.0f, 0.0f, (float) width, f);
            shade(0.0f, (((((SmoothCollapsibleWidget) find("toolcollapser")).getDone() * Core.i.toolmenu.getPrefHeight()) + f) - (78.0f * UCore.s)) - 10.0f, (float) width, ((float) height) - f);
            Actor modebutton = Core.i.stage.getRoot().findActor("modebutton");
            Actor gridbutton = Core.i.stage.getRoot().findActor("gridbutton");
            modebutton.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
            color(Color.WHITE);
            text(this.temp.x + modebutton.getWidth() + (UCore.s * 5.0f), this.temp.y + modebutton.getHeight(), "Use this button\nto change the [CORAL]draw\nmode.", 8);
            gridbutton.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
            text(this.temp.x + modebutton.getWidth() + (UCore.s * 5.0f), this.temp.y + modebutton.getHeight(), "Use this button\nto toggle the [GREEN]grid[].", 8);
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) + (180.0f * UCore.s), "[tap to continue]");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    toolmenu {
        /* access modifiers changed from: protected */
        public void draw() {
            if (Core.i.toolMenuCollapsed()) {
                Core.i.collapseToolMenu();
            }
            VisTextButton button = (VisTextButton) ((Table) Core.i.toolmenu.getChildren().first()).getChildren().first();
            float f = ((float) (width / Tool.values().length)) + (61.0f * UCore.s);
            color(select);
            rect(button);
            shade(0.0f, 0.0f, (float) width, (Core.i.toolmenu.getPrefHeight() + f) - (UCore.s * 77.0f));
            shade(0.0f, Core.i.toolmenu.getPrefHeight() + f, (float) width, ((float) height) - f);
            shade(button.getWidth() + (4.0f * UCore.s), (Core.i.toolmenu.getPrefHeight() + f) - (UCore.s * 77.0f), (float) width, button.getHeight() + (7.0f * UCore.s));
            color(select);
            rect(button);
            color(Color.WHITE);
            text((float) (width / 2), ((float) (height / 2)) + (220.0f * UCore.s), "Press this button to\ncontinue to the menu.");
            if (Core.i.projectmenu.getStage() != null) {
                next();
            }
        }
    },
    projectmenu {
        /* access modifiers changed from: protected */
        public void draw() {
            shade(0.0f, 0.0f, (float) width, (float) height);
            color(Color.WHITE);
            text((float) (width / 2), ((float) height) - (20.0f * UCore.s), "This is the project menu.\nYou can use it to easily store\nand switch canvases.\n\n[GOLD]Projects are saved [GREEN]automatically[ROYAL], so\nyou don't have to worry\nabout losing your work.");
            color(Color.PURPLE);
            text((float) (width / 2), (float) (height / 2), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    projectsettings {
        String[] names;
        int stage;

        /* access modifiers changed from: protected */
        public void draw() {
            VisScrollPane pane = (VisScrollPane) Core.i.stage.getRoot().findActor("projectpane");
            pane.setSmoothScrolling(false);
            pane.setScrollPercentY(0.0f);
            ProjectMenu.ProjectTable table = Core.i.projectmenu.getFirstTable();
            project(table);
            shade(0.0f, 0.0f, (float) width, this.temp.y);
            clearshade(0.0f, 0.0f, (float) width, (float) height);
            shade(0.0f, this.temp.y + table.getHeight(), (float) width, ((float) height) - (this.temp.y + table.getHeight()));
            color(Color.WHITE);
            rectarrow((VisImageButton) pane.findActor(String.valueOf(Core.i.projectmanager.getProjects().iterator().next().name) + this.names[this.stage] + "button"));
            text((float) (width / 2), ((float) (height / 2)) + (50.0f * UCore.s), "Use this button to " + this.names[this.stage] + " a project.");
            color(select);
            color(Color.PURPLE);
            text((float) (width / 2), (float) (height / 2), "<tap to continue>");
        }

        public void tap(int x, int y) {
            this.stage++;
            if (this.stage >= this.names.length) {
                this.stage = this.names.length - 1;
                next();
            }
        }

        public void end() {
            ((VisScrollPane) Core.i.getCurrentDialog().getContentTable().findActor("projectpane")).setSmoothScrolling(true);
        }
    },
    newproject {
        /* access modifiers changed from: protected */
        public void draw() {
            VisTextButton button = (VisTextButton) Core.i.stage.getRoot().findActor("newproject");
            button.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
            this.temp.y -= 6.0f;
            shade(0.0f, 0.0f, (float) width, this.temp.y);
            clearshade(0.0f, this.temp.y, (float) width, ((float) height) - this.temp.y);
            color(Color.WHITE);
            text((float) (width / 2), ((float) (height / 2)) + (180.0f * UCore.s), "This button allows you\nto create a new project.\nYou can either specify the project\nsize, or load an image file.");
            color(select);
            rectarrow(button);
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) - (40.0f * UCore.s), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    settings {
        /* access modifiers changed from: protected */
        public void draw() {
            VisTextButton button = (VisTextButton) Core.i.stage.getRoot().findActor("settings");
            button.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
            this.temp.y -= 6.0f;
            shade(0.0f, 0.0f, (float) width, this.temp.y);
            clearshade(0.0f, this.temp.y, (float) width, ((float) height) - this.temp.y);
            color(Color.WHITE);
            text((float) (width / 2), ((float) (height / 2)) + (180.0f * UCore.s), "This button opens up the settings.\nThere, you can customize\ncursor speed, size, and so on.");
            color(select);
            rectarrow(button);
            color(Color.PURPLE);
            text((float) (width / 2), ((float) (height / 2)) - (40.0f * UCore.s), "<tap to continue>");
        }

        public void tap(int x, int y) {
            next();
        }
    },
    tutorialend {
        /* access modifiers changed from: protected */
        public void draw() {
            shade(0.0f, 0.0f, (float) width, (float) height);
            color(Color.WHITE);
            text((float) (width / 2), (float) (height / 2), "And that concludes the tutorial!\nIf you'd like to re-take it,\nyou can do so in the\n[GREEN]setting menu.");
        }

        public void tap(int x, int y) {
            ((ClickListener) Core.i.stage.getRoot().findActor("modebutton").getListeners().get(0)).clicked((InputEvent) null, 0.0f, 0.0f);
            if (Core.i.getCurrentDialog() != null) {
                Core.i.getCurrentDialog().hide();
            }
            Core.i.collapseToolMenu();
            ((Tutorial) Core.i.getModule(Tutorial.class)).end();
        }
    };

    public static Rectangle[] cliprects = new Rectangle[]{new Rectangle(), new Rectangle(), new Rectangle()};
    static int height;
    public static final Color select = null;
    static int width;
    protected Batch batch;
    public boolean next;
    private int shades;
    protected Vector2 temp;
    public float trans;

    /* access modifiers changed from: protected */
    public abstract void draw();

    public final void draw(Batch batch2) {
        this.shades = 0;
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        this.batch = batch2;
        draw();
        color(select);
        text(((float) width) - (UCore.s * 30.0f), UCore.s * 30.0f, "Exit");
    }

    /* access modifiers changed from: package-private */
    public float colorHeight() {
        return ((Table) find("colortable")).getPrefHeight() + Core.i.colormenu.getBoxBorder();
    }

    public void color(Color color) {
        color(color.r, color.g, color.b, color.a);
    }

    public void color(float r, float g, float b, float a) {
        this.batch.setColor(r, g, b, this.trans * a);
        VisUI.getSkin().getFont("border-font").setColor(r, g, b, this.trans * a);
    }

    /* access modifiers changed from: protected */
    public void next() {
        this.next = true;
    }

    public void shade(float x, float y, float width2, float height2) {
        color(0.0f, 0.0f, 0.0f, 0.7f * this.trans);
        tex(x, y, width2, height2);
        clearshade(x, y, width2, height2);
    }

    public void clearshade(float x, float y, float width2, float height2) {
        cliprects[this.shades].set(x, y, width2, height2);
        this.shades++;
    }

    public void project(Actor actor) {
        actor.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
    }

    public void tex(float x, float y, float width2, float height2) {
        this.batch.draw(VisUI.getSkin().getRegion("white"), x, y, width2, height2);
    }

    public void rect(Actor actor) {
        actor.localToStageCoordinates(this.temp.set(0.0f, 0.0f));
        rect(this.temp.x, this.temp.y, actor.getWidth(), actor.getHeight());
    }

    public void rectarrow(Actor actor) {
        rect(actor);
        arrow(this.temp.x + (actor.getWidth() / 2.0f), this.temp.y - 80.0f, 60.0f, true);
    }

    public void arrow(float x, float y, float length, boolean up) {
        line(x, y, x, y + length);
        if (up) {
            line(x, y + length, x + 20.0f, (y - 20.0f) + length);
            line(x, y + length, x - 20.0f, (y - 20.0f) + length);
            return;
        }
        line(x, y, x + 20.0f, y + 20.0f);
        line(x, y, x - 20.0f, y + 20.0f);
    }

    public void rect(float x, float y, float width2, float height2) {
        ShapeUtils.rect(this.batch, x, y, width2, height2, 4);
    }

    public void line(float x, float y, float x2, float y2) {
        ShapeUtils.line(this.batch, x, y, x2, y2);
    }

    public void text(float x, float y, String text, int align) {
        VisUI.getSkin().getFont("border-font").getCache().clear();
        VisUI.getSkin().getFont("border-font").getCache().addText(text, x, y, 0.0f, align, false);
        VisUI.getSkin().getFont("border-font").getCache().setAlphas(Core.i.stage.getBatch().getColor().a);
        VisUI.getSkin().getFont("border-font").getCache().draw(this.batch);
    }

    public void text(float x, float y, String text) {
        text(x, y, text, 1);
    }

    public <T extends Actor> T find(String name) {
        return Core.i.stage.getRoot().findActor(name);
    }

    public void tap(int x, int y) {
    }

    public void end() {
    }
}
