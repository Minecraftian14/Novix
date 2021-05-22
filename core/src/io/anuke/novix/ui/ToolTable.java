package io.anuke.novix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import java.util.Iterator;

import io.anuke.novix.modules.Core;
import io.anuke.novix.scene.CollapseButton;
import io.anuke.novix.scene.ColorBar;
import io.anuke.novix.scene.SmoothCollapsibleWidget;
import io.anuke.ucore.UCore;
import io.anuke.ucore.graphics.PixmapUtils;
import io.anuke.utools.SceneUtils;
import io.anuke.novix.tools.PixelCanvas;
import io.anuke.novix.tools.Tool;

/* renamed from: io.anuke.novix.ui.ToolTable */
public class ToolTable extends VisTable {
    /* access modifiers changed from: private */
    public static ButtonMenu currentMenu;
    /* access modifiers changed from: private */
    public ColorBar alphabar;
    /* access modifiers changed from: private */
    public VisSlider brushslider;
    /* access modifiers changed from: private */
    public CollapseButton collapsebutton;
    /* access modifiers changed from: private */
    public SmoothCollapsibleWidget collapser;
    /* access modifiers changed from: private */
    public VisImageButton gridbutton;
    /* access modifiers changed from: private */
    public Core main;
    private VisTable menutable;
    private VisTable optionstable;
    public final String selectcolor = "7aaceaff";
    /* access modifiers changed from: private */
    public Tool tool;

    public ToolTable(Core main2) {
        VisTable tooltable = new VisTable();
        tooltable.setFillParent(true);
        main2.stage.addActor(tooltable);
        this.main = main2;
        setBackground("menu");
        this.menutable = new VisTable();
        this.optionstable = new VisTable();
        top().left().add(this.menutable).align(10).padBottom(10.0f).expand().fill().row();
        top().left().add(this.optionstable).expand().fill().row();
        this.optionstable.row();
        setupMenu();
        setupTable(tooltable);
    }

    private void setupTable(final Table tooltable) {
        float size = (float) (Gdx.graphics.getWidth() / Tool.values().length);
        this.collapser = new SmoothCollapsibleWidget(this, false);
        this.collapser.setName("toolcollapser");
        this.collapser.setCollapsed(true);
        this.collapsebutton = new CollapseButton();
        this.collapsebutton.setName("toolcollapsebutton");
        this.collapsebutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ToolTable.this.collapser.setCollapsed(!ToolTable.this.collapser.isCollapsed());
                ToolTable.this.collapsebutton.flip();
                if (!ToolTable.this.main.colorMenuCollapsed() && event != null) {
                    ToolTable.this.main.colormenu.collapse();
                }
            }
        });
        tooltable.bottom().left().add(this.collapser).height(20.0f * UCore.s).colspan(Tool.values().length).fillX().expandX();
        tooltable.row();
        tooltable.bottom().left().add(this.collapsebutton).height(60.0f * UCore.s).colspan(Tool.values().length).fillX().expandX();
        tooltable.row();
        Tool[] tools = Tool.values();
        for (int i = 0; i < tools.length; i++) {
            final Tool ctool = tools[i];
            final VisImageButton button = new VisImageButton((Drawable) null);
            button.setStyle(new VisImageButton.VisImageButtonStyle((VisImageButton.VisImageButtonStyle) VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class)));
            button.getStyle().imageUp = VisUI.getSkin().getDrawable("icon-" + ctool.name());
            button.setGenerateDisabledImage(true);
            button.getImageCell().size(48.0f * UCore.s);
            ctool.button = button;
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    VisImageButton other;
                    ctool.onSelected();
                    if (!ctool.selectable()) {
                        button.setChecked(false);
                        return;
                    }
                    ToolTable.this.tool = ctool;
                    ToolTable.this.tool.onColorChange(ToolTable.this.main.selectedColor(), ToolTable.this.main.drawgrid.canvas);
                    if (!button.isChecked()) {
                        button.setChecked(true);
                    }
                    Iterator<Actor> it = tooltable.getChildren().iterator();
                    while (it.hasNext()) {
                        Actor actor = it.next();
                        if ((actor instanceof VisImageButton) && (other = (VisImageButton) actor) != button) {
                            other.setChecked(false);
                        }
                    }
                }
            });
            if (i == 0) {
                button.setChecked(true);
                this.tool = ctool;
            }
            tooltable.bottom().left().add(button).size(1.0f + size);
        }
        tooltable.pack();
    }

    public void collapse() {
        ((ClickListener) this.collapsebutton.getListeners().get(2)).clicked((InputEvent) null, 0.0f, 0.0f);
    }

    public boolean collapsed() {
        return this.collapser.isCollapsed();
    }

    public CollapseButton getButton() {
        return this.collapsebutton;
    }

    public Tool getTool() {
        return this.tool;
    }

    /* access modifiers changed from: protected */
    public void drawBackground(Batch batch, float parentAlpha, float x, float y) {
        super.drawBackground(batch, parentAlpha, x, y);
        VisUI.getSkin().getDrawable("menu-bg").draw(batch, 0.0f, this.menutable.getY() - 8.0f, getWidth(), this.menutable.getHeight() + 8.0f);
    }

    public void updateColor(Color color) {
        this.alphabar.setRightColor(color);
    }

    public void initialize() {
        this.alphabar.fire(new ChangeListener.ChangeEvent());
        this.brushslider.fire(new ChangeListener.ChangeEvent());
    }

    private VisTextButton addMenuButton(String text, String icon) {
        float height = 70.0f * UCore.s;
        VisTextButton button = new VisTextButton(text);
        button.setStyle(new TextButton.TextButtonStyle(button.getStyle()));
        button.getLabelCell().expand(false, false).fill(false, false);
        SceneUtils.addIconToButton(button, new VisImage("icon-" + icon), 20.0f * UCore.s);
        this.menutable.top().left().add(button).height(height).growX().padTop(5.0f * UCore.s).align(10);
        return button;
    }

    private void addMenu(String name, String icon, MenuButton... buttonlist) {
        final ButtonMenu buttons = new ButtonMenu(name);
        buttons.getContentTable().top().left();
        for (MenuButton button : buttonlist) {
            buttons.getContentTable().add(button).width(UCore.s * 350.0f).padTop(UCore.s * 10.0f).row();
        }
        VisTextButton backbutton = new VisTextButton("Back");
        backbutton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ToolTable.currentMenu.hide();
            }
        });
        SceneUtils.addIconToButton(backbutton, new Image(VisUI.getSkin().getDrawable("icon-arrow-left")), UCore.s * 40.0f);
        backbutton.getLabelCell().padRight(40.0f);
        buttons.getContentTable().row();
        buttons.getContentTable().add(new Separator()).padTop(UCore.s * 10.0f).padBottom(5.0f * UCore.s).growX().row();
        buttons.getButtonsTable().add(backbutton).height(60.0f * UCore.s).width(UCore.s * 350.0f);
        addMenuButton(name, icon).addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                buttons.show(ToolTable.this.getStage());
                ToolTable.currentMenu = buttons;
            }
        });
    }

    /* renamed from: io.anuke.novix.ui.ToolTable$MenuButton */
    private static class MenuButton extends VisTextButton {
        public MenuButton(String name, String desc) {
            this(name, desc, "icon-" + name.toLowerCase().replace(" ", ""));
        }

        public MenuButton(String name, String desc, String icon) {
            super(name);
            if (VisUI.getSkin().getAtlas().findRegion(icon) != null) {
                SceneUtils.addIconToButton(this, new VisImage(icon), 20.0f * UCore.s);
                getCells().first().padRight(4.0f * UCore.s);
            }
            getLabel().setAlignment(10);
            left();
            row();
            add();
            row();
            VisLabel desclabel = new VisLabel(desc);
            desclabel.setColor(Color.GRAY);
            add(desclabel).align(10).padTop(15.0f * UCore.s).padBottom(5.0f * UCore.s);
            addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    MenuButton.this.clicked();
                    ToolTable.currentMenu.hide();
                }
            });
            DialogClasses.BaseDialog.addPadding(this);
        }

        public void clicked() {
        }
    }

    /* renamed from: io.anuke.novix.ui.ToolTable$ButtonMenu */
    private static class ButtonMenu extends DialogClasses.BaseDialog {
        public ButtonMenu(String name) {
            super(name);
            addTitleSeperator();
        }
    }

    private void setupMenu() {
        final Stage stage = this.main.stage;
        addMenuButton("Menu", "menu").addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ToolTable.this.main.openProjectMenu();
            }
        });
        addMenu("Image", "image", new MenuButton("Resize", "Change the canvas size.") {
            public void clicked() {
                new DialogClasses.SizeDialog("Resize Canvas") {
                    public void result(int width, int height) {
                        ToolTable.this.main.drawgrid.setCanvas(new PixelCanvas(PixmapUtils.resize(ToolTable.this.main.canvas().pixmap, width, height)), true);
                        ToolTable.this.main.checkGridResize();
                        ToolTable.this.main.updateToolColor();
                    }
                }.show(stage);
            }
        }, new MenuButton("Crop", "Cut out a part of the image.") {
            public void clicked() {
                new DialogClasses.CropDialog().show(stage);
            }
        }, new MenuButton("Clear", "Clear the image.") {
            public void clicked() {
                new DialogClasses.ClearDialog().show(stage);
            }
        }, new MenuButton("Color Fill", "Fill the image with a color.", "icon-clear") {
            public void clicked() {
                new DialogClasses.ColorFillDialog().show(stage);
            }
        }, new MenuButton("Symmetry", "Configure symmetry.") {
            public void clicked() {
                new DialogClasses.SymmetryDialog().show(stage);
            }
        });
        addMenu("Filters", "filter", new MenuButton("Colorize", "Configure image hue,\nbrightness and saturation.") {
            public void clicked() {
                new DialogClasses.ColorizeDialog().show(stage);
            }
        }, new MenuButton("Invert", "Invert the image color.") {
            public void clicked() {
                new DialogClasses.InvertDialog().show(stage);
            }
        }, new MenuButton("Replace", "Replace a color with\nanother color.") {
            public void clicked() {
                new DialogClasses.ReplaceDialog().show(stage);
            }
        }, new MenuButton("Contrast", "Change image contrast.") {
            public void clicked() {
                new DialogClasses.ContrastDialog().show(stage);
            }
        }, new MenuButton("Outline", "Add an outline around\nthe image.") {
            public void clicked() {
                new DialogClasses.OutlineDialog().show(stage);
            }
        }, new MenuButton("Erase Color", "Remove a certain color\nfrom the image.") {
            public void clicked() {
                new DialogClasses.ColorAlphaDialog().show(stage);
            }
        });
        addMenu("Edit", "edit", new MenuButton("Flip", "Flip the image.") {
            public void clicked() {
                new DialogClasses.FlipDialog().show(stage);
            }
        }, new MenuButton("Rotate", "Rotate the image.") {
            public void clicked() {
                new DialogClasses.RotateDialog().show(stage);
            }
        }, new MenuButton("Scale", "Scale the image.") {
            public void clicked() {
                new DialogClasses.ScaleDialog().show(stage);
            }
        }, new MenuButton("Shift", "Move the image.") {
            public void clicked() {
                new DialogClasses.ShiftDialog().show(stage);
            }
        });
        addMenu("File", "file", new MenuButton("Export", "Export the image as a PNG.") {
            public void clicked() {
                FileChooser.FileHandleFilter fileHandleFilter = FileChooser.pngFilter;
//                final Stage stage = stage;
                new FileChooser(fileHandleFilter, false) {
                    public void fileSelected(FileHandle file) {
                        new DialogClasses.ExportDialog(file).show(stage);
                    }
                }.show(stage);
            }
        }, new MenuButton("Open", "Load an image file\ninto this project.") {
            public void clicked() {
                FileChooser.FileHandleFilter fileHandleFilter = FileChooser.jpegFilter;
//                final Stage stage = stage;
                new FileChooser(fileHandleFilter, true) {
                    public void fileSelected(FileHandle file) {
                        try {
                            ToolTable.this.main.drawgrid.setCanvas(new PixelCanvas(new Pixmap(file)), true);
                            ToolTable.this.tool.onColorChange(ToolTable.this.main.selectedColor(), ToolTable.this.main.drawgrid.canvas);
                        } catch (Exception e) {
                            e.printStackTrace();
                            DialogClasses.showError(stage, e);
                        }
                    }
                }.show(stage);
            }
        });
        this.brushslider = new VisSlider(1.0f, 10.0f, 0.01f, true) {
            public float getPrefWidth() {
                return 30.0f * UCore.s;
            }
        };
        DialogClasses.scaleSlider(this.brushslider);
        this.brushslider.setValue((float) this.main.prefs.getInteger("brushsize", 1));
        final VisLabel brushlabel = new VisLabel("Brush Size: " + this.brushslider.getValue());
        this.brushslider.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                brushlabel.setText("Brush Size: [#7aaceaff]" + ((int) ToolTable.this.brushslider.getValue()));
                ToolTable.this.main.prefs.put("brushsize", (int) ToolTable.this.brushslider.getValue());
                ToolTable.this.main.drawgrid.brushSize = (int) ToolTable.this.brushslider.getValue();
            }
        });
        this.alphabar = new ColorBar(true);
        this.alphabar.setName("alphabar");
        this.alphabar.setColors(Color.CLEAR.cpy(), Color.WHITE);
        this.alphabar.setSize(50.0f * UCore.s, 300.0f * UCore.s);
        this.alphabar.setSelection(this.main.prefs.getFloat("opacity", 1.0f));
        this.optionstable.bottom().left();
        VisLabel visLabel = new VisLabel("Opacity: " + ((int) (this.alphabar.getSelection() * 100.0f)) + "%");
        final VisLabel visLabel2 = visLabel;
        this.alphabar.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                visLabel2.setText("Opacity: [#7aaceaff]" + ((int) (ToolTable.this.alphabar.getSelection() * 100.0f)) + "%");
                ToolTable.this.main.drawgrid.canvas.setAlpha(ToolTable.this.alphabar.getSelection());
                ToolTable.this.main.prefs.put("opacity", ToolTable.this.alphabar.getSelection());
            }
        });
        VisTextButton visTextButton = new VisTextButton("Menu");
        visTextButton.setName("menubutton");
        VisTextButton visTextButton2 = new VisTextButton("Settings");
        visTextButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ToolTable.this.main.openProjectMenu();
            }
        });
        visTextButton2.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                ToolTable.this.main.openSettingsMenu();
            }
        });
        VisImageButton.VisImageButtonStyle style = (VisImageButton.VisImageButtonStyle) VisUI.getSkin().get("toggle", VisImageButton.VisImageButtonStyle.class);
        VisImageButton.VisImageButtonStyle visImageButtonStyle = new VisImageButton.VisImageButtonStyle(style);
        VisImageButton.VisImageButtonStyle visImageButtonStyle2 = new VisImageButton.VisImageButtonStyle(style);
        visImageButtonStyle.imageUp = VisUI.getSkin().getDrawable("icon-cursor");
        visImageButtonStyle2.imageUp = VisUI.getSkin().getDrawable("icon-grid");
        final VisLabel cursorlabel = new VisLabel();
        VisLabel gridlabel = new VisLabel();
        VisImageButton modebutton = new VisImageButton(visImageButtonStyle);
        modebutton.setChecked(this.main.prefs.getBoolean("cursormode", true));
        modebutton.setName("modebutton");
        modebutton.getImageCell().size(48.0f * UCore.s);
        final VisImageButton visImageButton = modebutton;
        modebutton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                cursorlabel.setText("Mode: " + (visImageButton.isChecked() ? "[CORAL]Cursor" : "[PURPLE]Touch"));
                ToolTable.this.main.prefs.put("cursormode", visImageButton.isChecked());
                ToolTable.this.main.prefs.save();
            }
        });
        modebutton.fire(new ChangeListener.ChangeEvent());
        this.gridbutton = new VisImageButton(visImageButtonStyle2);
        this.gridbutton.setChecked(this.main.prefs.getBoolean("grid", true));
        this.gridbutton.setName("gridbutton");
        this.gridbutton.getImageCell().size(48.0f * UCore.s);
        final VisLabel visLabel3 = gridlabel;
        this.gridbutton.addListener(new ChangeListener() {
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                visLabel3.setText("Grid: " + (ToolTable.this.gridbutton.isChecked() ? "[CORAL]On" : "[PURPLE]Off"));
                ToolTable.this.main.prefs.put("grid", ToolTable.this.gridbutton.isChecked());
                ToolTable.this.main.prefs.save();
            }
        });
        this.gridbutton.fire(new ChangeListener.ChangeEvent());
        VisTable visTable = new VisTable();
        VisTable visTable2 = new VisTable();
        this.optionstable.top().left();
        this.optionstable.add(visTable).growY();
        this.optionstable.add(visTable2).grow();
        visTable.top().left();
        visTable.add(cursorlabel).align(10).padTop(12.0f * UCore.s).row();
        visTable.add(modebutton).size(80.0f * UCore.s).align(10).padTop(12.0f * UCore.s).row();
        visTable.add(gridlabel).align(10).padTop(8.0f * UCore.s).row();
        visTable.add(this.gridbutton).size(80.0f * UCore.s).align(10).padTop(12.0f * UCore.s).row();
        visTable2.bottom().right();
        visTable2.add(brushlabel).padRight(10.0f * UCore.s).minWidth(150.0f * UCore.s).align(1);
        visTable2.add(visLabel).minWidth(150.0f * UCore.s).align(1);
        visTable2.row();
        visTable2.add(this.brushslider).growY().padTop(20.0f * UCore.s).padBottom(20.0f * UCore.s).padRight(15.0f * UCore.s);
        visTable2.add(this.alphabar).padTop(20.0f * UCore.s).padBottom(20.0f * UCore.s);
    }

    public float getBarAlphaValue() {
        return this.alphabar.selection;
    }

    public VisImageButton getGridButton() {
        return this.gridbutton;
    }

    public float getPrefWidth() {
        return (float) Gdx.graphics.getWidth();
    }
}
