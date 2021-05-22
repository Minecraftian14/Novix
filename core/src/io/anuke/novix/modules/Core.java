package io.anuke.novix.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.FocusManager;
import com.kotcrab.vis.ui.Sizes;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.*;

import java.lang.reflect.Field;

import io.anuke.novix.Novix;
import io.anuke.novix.graphics.Palette;
import io.anuke.novix.managers.PaletteManager;
import io.anuke.novix.managers.PrefsManager;
import io.anuke.novix.managers.ProjectManager;
import io.anuke.novix.scene.ColorBox;
import io.anuke.novix.tools.ActionStack;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.Textures;
import io.anuke.ucore.modules.Module;
import io.anuke.utools.SceneUtils;
//import io.anuke.novix.android.AndroidKeyboard;
import io.anuke.novix.ui.ColorTable;
import io.anuke.novix.ui.DialogClasses;
import io.anuke.novix.ui.DrawingGrid;
import io.anuke.novix.ui.ProjectMenu;
import io.anuke.novix.ui.SettingsMenu;
import io.anuke.novix.ui.ToolTable;
import io.anuke.novix.tools.PixelCanvas;
import io.anuke.novix.tools.Project;
import io.anuke.novix.tools.Tool;

/* renamed from: io.anuke.novix.modules.Core */
public class Core extends Module<Novix> {

    /* renamed from: i */
    public static Core i;
    public final Color clearcolor = Color.valueOf("12161b");
    public ColorTable colormenu;
    public DrawingGrid drawgrid;
    public final int largeImageSize = 10000;
    public final FileHandle paletteFile = Gdx.files.local("palettes.json");
    public PaletteManager palettemanager;
    public PrefsManager prefs;
    public final FileHandle projectDirectory = Gdx.files.absolute(Gdx.files.getExternalStoragePath()).child("NovixProjects");
    public final FileHandle projectFile = Gdx.files.local("projects.json");
    public ProjectManager projectmanager;
    public ProjectMenu projectmenu;
    public SettingsMenu settingsmenu;
    public Stage stage;
    public ToolTable toolmenu;

    public void update() {

        clearScreen(this.clearcolor);
        if (FocusManager.getFocusedWidget() != null && !(FocusManager.getFocusedWidget() instanceof VisTextField)) {
            FocusManager.resetFocus(this.stage);
        }
        this.toolmenu.getTool().update(this.drawgrid);
        this.stage.act(Gdx.graphics.getDeltaTime() > 0.033333335f ? 0.016666668f : Gdx.graphics.getDeltaTime());
        this.stage.draw();
        if ((this.stage.getKeyboardFocus() instanceof Button) || this.stage.getKeyboardFocus() == null || (this.stage.getKeyboardFocus() instanceof VisDialog)) {
            this.stage.setKeyboardFocus(this.drawgrid);
        }
    }

    /* access modifiers changed from: package-private */
    public void setupExtraMenus() {
        this.settingsmenu = new SettingsMenu(this);
        this.settingsmenu.addPercentScrollSetting("Cursor Size");
        this.settingsmenu.addPercentScrollSetting("Cursor Speed");
        this.settingsmenu.addCheckSetting("Gestures", true);
        this.settingsmenu.addButton("Re-take Tutorial", new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Core.this.settingsmenu.hide();
                Core.this.projectmenu.hide();
                Core.this.collapseToolMenu();
                ((Tutorial) Core.this.getModule(Tutorial.class)).begin();
            }
        });
        this.projectmenu = new ProjectMenu(this);
        this.projectmenu.update(true);
        this.colormenu = new ColorTable(this);
    }

    /* access modifiers changed from: package-private */
    public void setupTools() {
        this.toolmenu = new ToolTable(this);
    }

    public void openSettingsMenu() {
        this.settingsmenu.show(this.stage);
    }

    /* access modifiers changed from: package-private */
    public void setupCanvas() {
        this.drawgrid = new DrawingGrid(this);
        this.drawgrid.setCanvas(new PixelCanvas(getCurrentProject().getCachedPixmap()), false);
        this.stage.addActor(this.drawgrid);
    }

    /* access modifiers changed from: package-private */
    public void checkTutorial() {
        if (!this.prefs.getBoolean("tutorial")) {
            DialogClasses.MenuDialog dialog = new DialogClasses.MenuDialog("Tutorial") {
                {
                    VisLabel header = new VisLabel("Welcome to Novix!");
                    Label.LabelStyle style = new Label.LabelStyle(header.getStyle());
                    style.font = VisUI.getSkin().getFont("large-font");
                    style.fontColor = Color.CORAL;
                    header.setStyle(style);
                    getContentTable().add(header).pad(UCore.s * 20.0f).row();
                    VisImage image = new VisImage("icon");
                    getContentTable().add(image).size(image.getPrefWidth() * UCore.s, image.getPrefHeight() * UCore.s).row();
                    getContentTable().add((CharSequence) "Would you like to take the tutorial?").pad(UCore.s * 20.0f);
                }

                public void result() {
                    ((Tutorial) Core.this.getModule(Tutorial.class)).begin();
                }
            };
            dialog.addTitleSeperator();
            dialog.show(this.stage);
        }
        this.prefs.put("tutorial", true);
    }

    public void setPalette(Palette palette) {
        this.colormenu.resetPaletteColor();
        this.palettemanager.setCurrentPalette(palette);
        this.prefs.put("palettecolor", 0);
        this.prefs.put("lastpalette", palette.id);
        this.prefs.save();
        this.colormenu.updateColorMenu();
        this.colormenu.setSelectedColor(palette.colors[0]);
        this.colormenu.setupBoxColors();
    }

    public void openProjectMenu() {
        final ProjectMenu.ProjectTable table = this.projectmenu.update(false);
        this.projectmenu.startLoading();
        this.projectmenu.show(this.stage);
        new Thread(new Runnable() {
            public void run() {
                Core.this.projectmanager.saveProject();
                Core.this.projectmenu.notifyLoaded();
                table.loaded = true;
            }
        }).start();
    }

    public boolean loadingProject() {
        return this.projectmenu.isLoading();
    }

    public Color selectedColor() {
        return this.colormenu.getSelectedColor().cpy();
    }

    public void updateToolColor() {
        if (this.toolmenu.getTool() != null && this.drawgrid != null) {
            this.toolmenu.getTool().onColorChange(selectedColor(), this.drawgrid.canvas);
            this.drawgrid.canvas.setAlpha(this.toolmenu.getBarAlphaValue());
        }
    }

    public Project getCurrentProject() {
        return this.projectmanager.getCurrentProject();
    }

    public Palette getCurrentPalette() {
        return this.palettemanager.getCurrentPalette();
    }

    public boolean toolMenuCollapsed() {
        return this.toolmenu.collapsed();
    }

    public boolean colorMenuCollapsed() {
        return this.colormenu.collapsed();
    }

    public void collapseToolMenu() {
        if (!this.colormenu.collapsed() && this.toolmenu.collapsed()) {
            collapseColorMenu();
        }
        this.toolmenu.collapse();
    }

    public void collapseColorMenu() {
        if (this.colormenu.collapsed() && !this.toolmenu.collapsed()) {
            collapseToolMenu();
        }
        this.colormenu.collapse();
    }

    public boolean menuOpen() {
        return !colorMenuCollapsed() || !toolMenuCollapsed();
    }

    public boolean isImageLarge() {
        return this.drawgrid.canvas.width() * this.drawgrid.canvas.height() > 10000;
    }

    public Tool tool() {
        return this.toolmenu.getTool();
    }

    public int getColorIndex() {
        return this.colormenu.getPaletteColor();
    }

    public ColorBox getSelectedBox() {
        return this.colormenu.getSelectedBox();
    }

    public VisDialog getCurrentDialog() {
        if (this.stage.getScrollFocus() != null) {
            Actor actor = SceneUtils.getTopParent(i.stage.getScrollFocus());
            if (actor instanceof VisDialog) {
                return (VisDialog) actor;
            }
        }
        return null;
    }

    public void checkGridResize() {
        ((VisImageButton) this.stage.getRoot().findActor("gridbutton")).setProgrammaticChangeEvents(true);
        if (this.drawgrid.canvas.width() * this.drawgrid.canvas.height() >= 10000) {
            ((VisImageButton) this.stage.getRoot().findActor("gridbutton")).setChecked(false);
        }
    }

    public PixelCanvas canvas() {
        return this.drawgrid.canvas;
    }

    public ActionStack actionStack() {
        return this.drawgrid.actions;
    }

    public void loadSkin() {
        FileHandle skinFile = Gdx.files.internal("ui/uiskin.json");
        Skin skin = new Skin();
        FileHandle atlasFile = skinFile.sibling(String.valueOf(skinFile.nameWithoutExtension()) + ".atlas");
        if (atlasFile.exists()) {
            TextureAtlas atlas = new TextureAtlas(atlasFile);
            try {
                Field field = skin.getClass().getDeclaredField("atlas");
                field.setAccessible(true);
                field.set(skin, atlas);
                skin.addRegions(atlas);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/smooth.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter normalparameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        normalparameter.size = (int) (22.0f * UCore.s);
        FreeTypeFontGenerator.FreeTypeFontParameter largeparameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        largeparameter.size = (int) (26.0f * UCore.s);
        FreeTypeFontGenerator.FreeTypeFontParameter borderparameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        borderparameter.size = (int) (26.0f * UCore.s);
        borderparameter.borderWidth = 2.0f * UCore.s;
        borderparameter.borderColor = this.clearcolor;
        borderparameter.spaceX = -2;
        BitmapFont font = generator.generateFont(normalparameter);
        font.getData().markupEnabled = true;
        BitmapFont largefont = generator.generateFont(largeparameter);
        BitmapFont borderfont = generator.generateFont(borderparameter);
        borderfont.getData().markupEnabled = true;
        skin.add("default-font", font);
        skin.add("large-font", largefont);
        skin.add("border-font", borderfont);
        skin.load(skinFile);
        VisUI.load(skin);
        ((Window.WindowStyle) skin.get(Window.WindowStyle.class)).titleFont = largefont;
        ((Window.WindowStyle) skin.get(Window.WindowStyle.class)).titleFontColor = Color.CORAL;
        ((Window.WindowStyle) skin.get("dialog", Window.WindowStyle.class)).titleFont = largefont;
        ((Window.WindowStyle) skin.get("dialog", Window.WindowStyle.class)).titleFontColor = Color.CORAL;
        generator.dispose();
    }

    public Core() {
        Gdx.graphics.setContinuousRendering(false);
        i = this;
        this.projectDirectory.mkdirs();
        this.prefs = new PrefsManager(this);
        this.palettemanager = new PaletteManager(this);
        this.palettemanager.loadPalettes();
        Textures.load("textures/");
        Textures.repeatWrap("alpha", "stripe");
        this.stage = new Stage();
        this.stage.setViewport(new ScreenViewport());
        this.projectmanager = new ProjectManager(this);
        loadSkin();
//        AndroidKeyboard.setListener(new DialogKeyboardMoveListener());
        this.projectmanager.loadProjects();
        setupTools();
        setupCanvas();
        setupExtraMenus();
        updateToolColor();
        this.toolmenu.initialize();
        Timer.schedule(new Timer.Task() {
            public void run() {
                new Thread(new Runnable() {
                    public void run() {
                        Core.this.projectmanager.saveProject();
                        Core.this.palettemanager.savePalettes();
                        Core.this.prefs.save();
                    }
                }).start();
            }
        }, 20.0f, 20.0f);
        Timer.schedule(new Timer.Task() {
            public void run() {
                Core.this.checkTutorial();
            }
        }, 0.1f);
    }

    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    public void pause() {
        Novix.log("Pausing and saving everything.");
        this.projectmanager.saveProject();
        this.palettemanager.savePalettes();
        this.prefs.save();
    }

    public void dispose() {
        pause();
        VisUI.dispose();
        Textures.dispose();
    }
}
